package com.dgut.app.service;

import com.dgut.app.helper.FriendshipWrapper;
import com.dgut.app.helper.UserWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.pck.Encrypt;
import com.dgut.common.util.StrUtils;
import com.dgut.main.Constants;
import com.dgut.main.member.dao.ProvinceDao;
import com.dgut.main.member.entity.City;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Province;
import com.dgut.main.member.entity.base.BaseFriendship;
import com.dgut.main.member.manager.ApplyMng;
import com.dgut.main.member.manager.FriendshipMng;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PUNK on 2017/1/31.
 */
@Service
public class UserData {

    @Autowired
    private MemberMng memberMng;

    @Autowired
    private ApplyMng applyMng;

    @Autowired
    private FriendshipMng friendshipMng;

    @Autowired
    private ProvinceDao provinceDao;

    /**
     * opt 27 根据手机号查找用户
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getUserByMobile(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.setExcludes(new String[] { "handler","cities","priority","province","id",
                "hibernateLazyInitializer" });

        Map<String,Object> jsonMap=new HashMap<String,Object>();
        Member searcher= CmsUtils.getMember(request);
//		Member searcher=memberMng.findById(1);
        String mobile=parameters.get("mobile");
        if (StringUtils.isBlank(mobile)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "请输入手机号码");
            return JSONUtils.printObject(jsonMap);
        }
        if (!StrUtils.isMobileNum(mobile)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "请输入正确的手机号码");
            return JSONUtils.printObject(jsonMap);
        }

        Member member=memberMng.findByMobile(mobile);
        if(member!=null) {
            Map<String, Object> userMap = UserWrapper.convertMemberInfo(member);
            Friendship friendship = FriendshipWrapper.getFriendship(member, searcher.getFriendships());
            userMap.put("is_stranger",friendship==null);
            if(friendship!=null){
                userMap.put("alias", member.getUsername());
                userMap.put("in_blacklist", friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_BLACKLIST));
            }


            jsonMap.put("result", userMap);
        }
        else{
            jsonMap.put("result",null);
        }
        jsonMap.put("state", 0);
        jsonMap.put("msg", "查找用户成功");

        return JSONObject.fromObject(jsonMap, jsonConfig).toString();
    }

    /**
     * opt = 5
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getUserInfo(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Member member=CmsUtils.getMember(request);

        //是否需要城市信息
        String ifNeedCity=parameters.get("city_flag");
//		Member member = memberMng.findById(3);


        JsonConfig config = new JsonConfig();
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

        config.setExcludes(new String[] {
                "province", "priority", "handler",
                "hibernateLazyInitializer" });


        Map<String, Object> userMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(ifNeedCity) && ifNeedCity.trim().equals("1")){
            List<Province> provinceList = provinceDao.getList();
            jsonMap.put("provinceList", provinceList);
        }

        DecimalFormat df   = new DecimalFormat("######0.00");

        String balance= Encrypt.decrypt3DES(member.getBalance(), Constants.ENCRYPTION_KEY);
        userMap.put("balance", df.format(Double.parseDouble(balance)));
        userMap.put("icon", member.getIcon());
        userMap.put("username", member.getUsername());

        City city=member.getCity();
        userMap.put("city_id",city==null? null:city.getId()+"");

        userMap.put("city_name",city==null? null: city.getCity_name() );
        userMap.put("province_name", city==null? null: city.getProvince().getProvince_name());

        userMap.put("gender", member.getGender());


        userMap.put("mobile", member.getMobile());

        userMap.put("isBinded", StringUtils.isBlank(member.getPayAccount())? false:true);
        userMap.put("alipay_account", member.getPayAccount());
        jsonMap.put("state", "0");
        jsonMap.put("msg", "获取个人信息成功");
        jsonMap.put("result", userMap);


        return JSONObject.fromObject(jsonMap, config).toString();
    }


    /**
     * opt=28
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String getUserList(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.setExcludes(new String[] { "handler","cities","priority","province","id",
                "hibernateLazyInitializer" });

        Map<String,Object> jsonMap=new HashMap<String,Object>();

        Member member=CmsUtils.getMember(request);
        //		Member member=memberMng.findById(1);
        String easemod_names=parameters.get("easemob_names");
        if(StringUtils.isBlank(easemod_names)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "环信名不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        List<Friendship> friendships=friendshipMng.getFriendshipsByUser(member, Arrays.asList(easemod_names.split(",")));

        jsonMap.put("result", FriendshipWrapper.convertFriendListInfo(friendships));
        jsonMap.put("totalCount", friendships.size());
        jsonMap.put("state", 0);
        jsonMap.put("msg", "获取用户列表成功");
        return JSONObject.fromObject(jsonMap, jsonConfig).toString();
    }
}
