package com.dgut.app.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dgut.app.service.*;
import com.dgut.app.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dgut.app.AppConstants;
import com.dgut.app.pck.AppGateway;
import com.dgut.app.pck.GeneralRestGatewayInterface;


import com.dgut.main.Constants;
import com.dgut.main.web.CmsUtils;

@Controller
public class AppController implements GeneralRestGatewayInterface {
    private static final Logger log = LoggerFactory
            .getLogger(AppController.class);

    @RequestMapping(value = "/index.do", method = RequestMethod.GET)
    public void getIndex(HttpServletRequest request,
                         HttpServletResponse response, ModelMap model) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("state", -1);
        jsonMap.put("msg", "请使用post请求");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.write(JSONUtils.printObject(jsonMap));
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/index.do", method = {RequestMethod.POST,
            RequestMethod.OPTIONS})
    public void index(HttpServletRequest request, HttpServletResponse response,
                      ModelMap model) throws IOException {

        StringBuilder errorDescription = new StringBuilder();
        int code = AppGateway.handle(Constants.APP_ENCRYPTION_KEY, 3000, this,
                request, response, errorDescription);
        if (code < 0) {
            log.error("app请求失败:" + errorDescription.toString());
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("state", -1);
            jsonMap.put("msg", errorDescription.toString());
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write(JSONUtils.printObject(jsonMap));
            out.flush();
            out.close();
        }
    }

    @Override
    public String delegateHandleRequest(HttpServletRequest request,
                                        HttpServletResponse response, Map<String, String> parameters,
                                        StringBuilder paramStringBuilder) throws RuntimeException {

        String result = null;

        Integer OPT = Integer.parseInt(parameters.get("opt"));
        // 需要登录
        if (OPT != AppConstants.APP_SNS_CODE
                && OPT != AppConstants.APP_REGISTER
                && OPT != AppConstants.APP_LOGIN
                && OPT != AppConstants.APP_FORGETPWD





                ) {
            if (CmsUtils.getMember(request) == null) {
                Map<String, Object> jsonMap = new HashMap<String, Object>();
                jsonMap.put("state", -2);
                jsonMap.put("msg", "太久未登录，请重新登录");
                try {
                    return JSONUtils.printObject(jsonMap);
                } catch (IOException e) {
                }
            }
        }

        switch (OPT) {
            case AppConstants.APP_SNS_CODE:
                try {
                    result = snsService.sendCode(request, response, parameters);
                } catch (IOException e) {
                    log.error("申请短信验证码:" + e.getMessage());
                }
                break;
            case AppConstants.APP_REGISTER:
                try {
                    result = userService.regist(request, response, parameters);
                } catch (IOException e) {
                    log.error("注册时：" + e.getMessage());
                }
                break;
            case AppConstants.APP_LOGIN:
                try {
                    result = userService.login(request, response, parameters);
                } catch (IOException e) {
                    log.error("登录时：" + e.getMessage());
                }
                break;
            case AppConstants.APP_FORGETPWD:
                try {
                    result = userService.forgetPwd(request, response, parameters);
                } catch (IOException e) {
                    log.error("忘记密码时:" + e.getMessage());
                }
                break;

            case AppConstants.APP_EDITPWD:
                try{
                    result = userService.editPwd(request,response,parameters);
                }catch (IOException e){
                    log.error("修改密码时:"+e.getMessage());
                }
                break;

            case AppConstants.APP_USERINFO:
                try {
                    result = userData.getUserInfo(request, response,parameters);
                } catch (Exception e) {
                    log.error("获取个人信息时:" + e.getMessage());
                }
                break;

            case AppConstants.APP_EDITUSERINFO:
                try {
                    result = userService.editUserInfo(request, response,parameters,true);
                } catch (Exception e) {
                    log.error("修改个人信息时:" + e.getMessage());
                }
                break;

            case AppConstants.APP_PUBLISHAPPLY:
                try{
                    result = applyService.addApplication(request,response,parameters);
                }catch(IOException e){
                    log.error("发起好友申请时:"+e.getMessage());
                }
                break;

            case AppConstants.APP_HANDLE_APPLY:
                try{
                    result = applyService.handleApply(request,response,parameters);
                }catch(IOException e){
                    log.error("处理好友申请时:"+e.getMessage());
                }
                break;

            case AppConstants.APP_PUTBLACKLIST:
                try{
                    result = friendshipService.defriend(request, response, parameters);
                }catch(IOException e){
                    log.error("拉黑好友时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_REMOVEFROMBLACKLIST:
                try{
                    result = friendshipService.pullFromBlackList(request,response,parameters);
                }catch(IOException e){
                    log.error("将好友从黑名单里移除:"+e.getLocalizedMessage());
                }
                break;
            case AppConstants.APP_FRIENDSLIST:
                try{
                    result = friendshipService.getFriendList(request,response,parameters);
                }catch(IOException e){
                    log.error("查看好友列表时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_DELETEFRIEND:
                try{
                    result = friendshipService.deleteFriendship(request,response,parameters);
                }catch(IOException e){
                    log.error("删除好友时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_APPLYLIST:
                try{
                    result = applyService.getApplications(request,response,parameters);
                }catch(IOException e){
                    log.error("查看好友申请列表时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_FINDUSERBYMOBILE:
                try{
                    result = userData.getUserByMobile(request,response,parameters);
                }catch(IOException e){
                    log.error("查找用户时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_USERLIST:
                try{
                    result = userData.getUserList(request,response,parameters);
                }catch(IOException e){
                    log.error("查看用户列表时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_SIGNTALK:
                try{
                    result = talkService.signTalk(request,response,parameters);
                }catch(IOException e){
                    log.error("评论朋友圈时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_DELETECOMMENT:
                try{
                    result =  talkService.deleteComment(request, response, parameters);
                }catch(IOException e){
                    log.error("删除评论时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_DELETETALK:
                try{
                    result=talkService.deleteTalk(request,response,parameters);
                }catch(IOException e){
                    log.error("删除朋友圈时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_TAPLIKETALK:
                try{
                    result=talkService.tapLike(request,response,parameters);
                }catch(IOException e){
                    log.error("点赞朋友圈时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_CANCELTAPLIKE:
                try{
                    result = talkService.cancelTapLike( request,response,parameters);
                }catch(IOException e){
                    log.error("取消点赞时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_USERTALK:
                try{
                    result=talkService.getUserTalk(request, response, parameters);
                }catch (IOException e) {
                    log.error("查看用户的朋友圈时：" + e.getMessage());
                }
                break;
            case AppConstants.APP_TALKINDEX:
                try{
                    result=talkService.getTalkIndex(request,response,parameters);
                }catch(IOException e){
                    log.error("查看首页朋友圈时:"+e.getMessage());
                }
                break;

            case AppConstants.APP_CREATEGROUP:
                try{
                    result = groupService.createGroup(request,response,parameters);
                }catch(IOException e){
                    log.error("创建群组时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_EDITGROUP:
                try{
                    result = groupService.editGroup(request,response,parameters);
                }catch(IOException e){
                    log.error("修改群组时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_CHANGEOWNER:
                try{
                    result = groupService.changeOwner(request,response,parameters);
                }catch(IOException e){
                    log.error("更换群主时:"+e.getMessage());
                }
                break;

            case AppConstants.APP_GROUPADD:
                try {
                    result = groupService.addUser(request, response, parameters);
                }catch (IOException e) {
                    log.error("往群组加人时:" + e.getMessage());
                }
                break;
            case AppConstants.APP_GROUPSUB:
                try{
                    result = groupService.subUser(request,response,parameters);
                }catch (IOException e){
                    log.error("群组减人时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_DELETEGROUP:
                try{
                    result = groupService.deleteGroup(request,response,parameters);
                }catch(IOException e){
                    log.error("删除群组时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_GETALLGROUP:
                try{
                    result = groupService.getGroupByUser (request , response,parameters);
                }catch(IOException e){
                    log.error("获得用户参与的群组时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_ACCUSATION_DATA:
                try{
                    result = accusationService.getData(request,response,parameters);
                }catch(IOException e){
                    log.error("获得举报信息时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_ACCUSERUSER:
                try{
                    result = accusationService.accuseUser(request,response,parameters);
                }catch(IOException e){
                    log.error("举报用户时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_SENDREDBAG:
                try{
                    result = redBagService.sendRedBag(request,response,parameters);
                }catch(IOException e){
                    log.error("发红包时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_GETREDBAG:
                try{
                    result = redBagService.getRedBag(request,response,parameters);
                }catch (IOException e){
                    log.error("拆红包时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_GETREDBAGRECORDS:
                try{
                    result = redBagService.getRecord(request, response, parameters);
                }catch (IOException e){
                    log.error("获取红包记录时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_BINDACCOUNT:
                try {
                    result = rechargeService.bindAccount(request, response,
                            parameters);

                } catch (Exception e) {
                    log.error("绑定账户时:" + e.getMessage());
                }
                break;
            case AppConstants.APP_RECHARGE:
                try {
                    result = rechargeService.rechargeAccount(request, response,
                            parameters);

                } catch (Exception e) {
                    log.error("充值时:" + e.getMessage());
                }
                break;
            case AppConstants.APP_WITHDRAW:
                try{
                    result=withdrawService.addWithdraw(request,response,parameters);
                }catch(Exception e){
                    log.error("提现申请时:"+e.getMessage());
                }
                break;
            case AppConstants.APP_WITHDRAW_LIST:
                try{
                    result = withdrawService.getListByUser(request,response,parameters);
                }catch(IOException e){
                    log.error("查看提现记录时:"+e.getMessage());
                }
                break;


            default:
                break;
        }
        // System.out.println(" 返回结果："+result);
        return result;
    }




    @Autowired
    private RequestData requestData;

    @Autowired
    private SNSService snsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private FriendshipService friendshipService;


    @Autowired
    private UserData userData;

    @Autowired
    private TalkService talkService;

    @Autowired
    private ChatGroupService groupService;


    @Autowired
    private AccusationService accusationService;

    @Autowired
    private RedEnvolopeService redBagService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawService withdrawService;






}
