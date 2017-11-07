package com.dgut.app.service;

import com.dgut.app.helper.UserWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.app.utils.LetterUtils;
import com.dgut.common.pck.Encrypt;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.DisabledException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.security.encoder.PwdEncoder;
import com.dgut.common.util.StrUtils;
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.RequestUtils;
import com.dgut.common.web.session.SessionProvider;
import com.dgut.common.web.springmvc.MessageResolver;
import com.dgut.main.Constants;
import com.dgut.main.entity.base.BaseAuthentication;
import com.dgut.main.manager.AuthenticationMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.dao.CityDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.MemberAuthentication;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.impl.MemberAuthenticationMngImpl;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PUNK on 2017/1/22.
 */
@Service
public class UserService {
    /**
     * opt=1 用户注册
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String regist(HttpServletRequest request,
                         HttpServletResponse response, Map<String, String> parameters)
            throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        String mobile = parameters.get("mobile");
        String password =  parameters.get("pwd");
        String code = parameters.get("code");

        String username= parameters.get("username");

        if(StringUtils.isBlank(username) || StringUtils.isBlank(mobile) || StringUtils.isBlank(password) || StringUtils.isBlank(code)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "注册参数不完整，请检查用户名、手机、密码、验证码是否存在");
            return JSONUtils.printObject(jsonMap);
        }

        //手机号码校验
        jsonMap = validateMobile(mobile,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return JSONUtils.printObject(jsonMap);
        }

        //验证码校验
        jsonMap =validateSNSCode(mobile,code,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return JSONUtils.printObject(jsonMap);
        }


        boolean notPhoneExist = memberMng.mobileNotExist(mobile);
        if (!notPhoneExist) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "该手机号码已注册");
            return JSONUtils.printObject(jsonMap);
        }
        boolean notNameExist=memberMng.usernameNotExist(username);
        if (!notNameExist) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "用户名已存在");

            return JSONUtils.printObject(jsonMap);
        }

        //对密码进行解密
        password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);
        String ip = RequestUtils.getIpAddr(request);

        Member member = memberMng.saveMember(username,mobile, password, ip);

        String easemob_name= LetterUtils.getRandomLetter()+member.getId();
        member.setEasemob_name(easemob_name);


        //在本地服务器注册完用户后，在环信平台注册一个用户(用户名为四位字母加主键),此处注册失败将交由定时器批量注册
        JSONObject result=easemobService.registUser(easemob_name, username, password);
        if(result.getInt("responseStatus")!=200){
           member.setEasemob_flag(false);
        }
        else{
            member.setEasemob_flag(true);
        }
        memberMng.updateMember(member);
        jsonMap.put("state", 0);
        jsonMap.put("msg", "注册成功");

        return JSONUtils.printObject(jsonMap);
    }





    /**
     * opt=2 用户登陆
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String login(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        String mobile = parameters.get("mobile");
        String password = parameters.get("pwd");

        Map<String,Object> jsonMap = new HashMap<String,Object>();
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(password)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","登陆参数不完整，请检查手机号和密码是否存在");
            return JSONUtils.printObject(jsonMap);
        }

        jsonMap = validateMobile(mobile,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return JSONUtils.printObject(jsonMap);
        }
        password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);

        String ip = RequestUtils.getIpAddr(request);
        Member member;
        BaseAuthentication auth;
        try {
            auth = authenticationMng.login(mobile, password, ip, request,
                    response, session);

            member = memberMng.findById(auth.getUid());
            if (member.getDisabled()) {
                // 如果已经禁用，则退出登录。
                authenticationMng.deleteById(auth.getId());
                session.logout(request, response);
                throw new DisabledException("user disabled");
            }
            //记录登陆日志
            memberLogMng.loginSuccess(request, mobile, "登录成功");


        } catch (UsernameNotFoundException e) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "用户名不存在");
            return JSONUtils.printObject(jsonMap);
        } catch (BadCredentialsException e) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "用户名或密码错误");
            memberLogMng.loginFailure(request,mobile, "cms.member.login.badCredentials","username="+mobile+",password="+password);
            return JSONUtils.printObject(jsonMap);
        } catch (DisabledException e) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "账户被禁用，请联系管理员");
            memberLogMng.loginFailure(request,mobile,"cms.member.login.disabled",null);
            return JSONUtils.printObject(jsonMap);
        }
        jsonMap.put("state", 0);
        jsonMap.put("msg", "登录成功");

        Map<String,Object> resultMap=null;
       /* resultMap.put("username", member.getUsername());
        resultMap.put("easemob_name", member.getEasemob_name());
        resultMap.put("userid", Encrypt.encrypt3DES(member.getId()+"", Constants.ENCRYPTION_KEY));
        resultMap.put("icon", member.getIcon());*/
        resultMap=UserWrapper.convertMemberInfo(member);

        jsonMap.put("result", resultMap);

        String token=(String) session.getAttribute(request, AuthenticationMng.Member_AUTH_KEY);
        CookieUtils.addCookie(request, response, "token",
                token, AuthenticationMng.expire/1000, null);

//		System.out.println("登陆："+JSONUtils.printObject(jsonMap));
        return JSONUtils.printObject(jsonMap);
    }

    /**
     * opt=3 忘记密码
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String forgetPwd(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {


        String mobile = parameters.get("mobile");
        String password = parameters.get("pwd");
        String code = parameters.get("code");

        Map<String,Object> jsonMap = new HashMap<String,Object>();

        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(password) || StringUtils.isBlank(code)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","参数不完整，请检查手机号、密码、验证码是否存在");
            return JSONUtils.printObject(jsonMap);
        }

       jsonMap =validateMobile(mobile,jsonMap);
       if(jsonMap.keySet().size()!=0){
           return JSONUtils.printObject(jsonMap);
       }

      jsonMap = validateSNSCode(mobile,code,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return JSONUtils.printObject(jsonMap);
        }



        password = Encrypt.decrypt3DES(password, Constants.ENCRYPTION_KEY);



        Member member= memberMng.findByMobile(mobile);
        if (member==null) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "手机号码未注册");
            return JSONUtils.printObject(jsonMap);
        }
        memberMng.updateMemberPwd(member, password);
        jsonMap.put("state", 0);
        jsonMap.put("msg", "更新密码成功");
        memberLogMng.operating(request,"cms.member.login.forgetPwd",null);
        return JSONUtils.printObject(jsonMap);
    }




    /**
     * 校验手机号
     * @param mobile
     * @return
     */
    private Map<String, Object> validateMobile(String mobile,Map<String,Object> jsonMap)  {

        if(StringUtils.isBlank(mobile)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "手机不能为空");
            return jsonMap;
        }
        if (!StrUtils.isMobileNum(mobile)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "请输入正确的手机号码");
            return jsonMap;
        }

        return jsonMap;
    }


    /**
     * 校验验证码,前提手机号、验证码不为空
     * @param mobile
     * @param code
     * @param jsonMap
     * @return
     */
    private Map<String,Object> validateSNSCode(String mobile, String code,Map<String,Object> jsonMap) {

       /* if(StringUtils.isBlank(mobile) || StringUtils.isBlank(code)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","请检查手机号和验证码是否存在");
            return jsonMap;
        }

        jsonMap = validateMobile(mobile,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return  jsonMap;
        }
        else {*/
            Map<String, String> map = snsService.codeVerify(mobile, code);
            if (map.get("state").equals(-1)) {
                jsonMap.put("state", -1);
                jsonMap.put("msg", map.get("msg"));
            }
            return jsonMap;
//        }
    }



    /**
     * opt=4 修改密码
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String editPwd(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        String mobile = parameters.get("mobile");
        String code = parameters.get("code");
        String oldPwd = parameters.get("oldPwd");
        String newPwd = parameters.get("newPwd");

       Map<String,Object> jsonMap = new HashMap<String,Object>();
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(code) || StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","参数不完整，请检查手机号、验证码、旧密码以及新密码是否存在");
            return JSONUtils.printObject(jsonMap);
        }

        jsonMap = validateMobile(mobile,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return JSONUtils.printObject(jsonMap);
        }

        jsonMap = validateSNSCode(mobile,code,jsonMap);
        if(jsonMap.keySet().size()!=0){
            return JSONUtils.printObject(jsonMap);
        }

        Member member = memberMng.findByMobile(mobile);
        if(member==null){
            jsonMap.put("state",-1);
            jsonMap.put("msg","用户不存在");
            return JSONUtils.printObject(jsonMap);
        }

        if(!encoder.isPasswordValid(member.getPassword(),oldPwd)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","原密码有误");
            return JSONUtils.printObject(jsonMap);
        }

        memberMng.updateMemberPwd(member,newPwd);
        jsonMap.put("state",0);
        jsonMap.put("msg","修改密码成功");
        memberLogMng.operating(request,"cms.member.login.editPwd",null);
        return JSONUtils.printObject(jsonMap);
    }

    /**
     * opt = 6 修改个人信息
     * @param request
     * @param response
     * @param parameters
     * @param b
     * @return
     */
    public String editUserInfo(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters, boolean b) throws IOException {
        Member member= CmsUtils.getMember(request);
        Map<String, Object> jsonMap = new HashMap<String, Object>();


        String city_id=parameters.get("city_id");
        String  username = parameters.get("username");
        if(StringUtils.isNotBlank(city_id)){
            city_id=Encrypt.decrypt3DES(city_id, Constants.ENCRYPTION_KEY);
        }


        if(StringUtils.isNotBlank(username) && StrUtils.isInvalidWord(username)){
            jsonMap.put("state", "-1");
            jsonMap.put("msg", "昵称包含非法字符");
        }


        //如果检验没有错，更新用户信息,真名和身份证填写仅有一次机会
        else{

            if(StringUtils.isNotBlank(city_id)){
                member.setCity(cityDao.findById(Integer.parseInt(city_id)));
            }

           member.setUsername(username);
            memberMng.updateMember(member);

            jsonMap.put("state", "0");
            jsonMap.put("msg", "修改用户信息成功 ");

            memberLogMng.operating(request, "cms.member.editInfo",
                    "修改个人信息");

        }
        return JSONUtils.printObject(jsonMap);
    }






    @Autowired
    private SNSService snsService;

    @Autowired
    private MemberMng memberMng;

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;

    @Autowired
    private EasemobService easemobService;

    @Resource(name="memberAuthenticationMng")
    private AuthenticationMng authenticationMng;

    @Autowired
    private SessionProvider session;

    @Autowired
    private PwdEncoder encoder;

    @Autowired
    private CityDao cityDao;


}
