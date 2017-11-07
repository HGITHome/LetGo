package com.dgut.app.service;

import com.dgut.app.helper.FriendshipWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseFriendship;
import com.dgut.main.member.manager.ApplyMng;
import com.dgut.main.member.manager.FriendshipMng;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PUNK on 2017/1/29.
 */
@Service
public class FriendshipService {

    @Autowired
    private ApplyMng applyMng;

    @Autowired
    private EasemobService easemobService;

    @Autowired
    private FriendshipMng friendshipMng;

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;

    /**
     * opt=23 拉黑好友
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String defriend(HttpServletRequest request,
                           HttpServletResponse response, Map<String, String> parameters)
            throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Member member = CmsUtils.getMember(request);
        //		Member member = memberMng.findById(1);

        String friendship_id = parameters.get("friendship_id");

        if(StringUtils.isBlank(friendship_id)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "好友关系id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        friendship_id = Encrypt.decrypt3DES(friendship_id,
                Constants.ENCRYPTION_KEY);

        Friendship friendship = friendshipMng.findById(Integer
                .parseInt(friendship_id));

        // 判断是否是友谊的持有者
		/*if (!friendship.getOwner().equals(member)
				&& !friendship.getFriend().equals(member)) {
			jsonMap.put("state", -1);
			jsonMap.put("msg", "你没有权限拉黑");
			return JSONUtils.printObject(jsonMap);
		}*/

        if(!member.equals(friendship.getOwner())){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "你没有权限拉黑");
            return JSONUtils.printObject(jsonMap);
        }

        // 检查是否正常状态
        if (!friendship.getFriendship_status().equals(BaseFriendship.Friendship_status.IN_NORMAL)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "当前状态下不能拉黑好友");
            return JSONUtils.printObject(jsonMap);
        }

        //		Member owner = friendship.getOwner();
        Member friend = friendship.getFriend();

        // 环信拉黑
        JSONObject result = easemobService.addToBlackList(
                member.getEasemob_name(),
                friend.getEasemob_name() );

        if (result.getInt("responseStatus") != 200) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "服务器内部错误");
        } else {

            // 服务端拉黑
            friendshipMng.putIntoBlacklist(friendship);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "拉黑好友成功");
            memberLogMng.operating(request,"cms.member.defriend","username="+friendship.getFriend().getUsername());

        }

        return JSONUtils.printObject(jsonMap);
    }


    /**
     * opt=25 查看好友列表
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String getFriendList(HttpServletRequest request,
                                HttpServletResponse response, Map<String, String> parameters) throws IOException {

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.setExcludes(new String[] { "handler","cities","priority",
                "hibernateLazyInitializer" });

        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member = CmsUtils.getMember(request);
        //		Member member = memberMng.findById(1);

        //得到环信的好友列表
        JSONArray friendNames=easemobService.getFriends(member.getEasemob_name());
       /* JSONArray friendNames = new JSONArray();
        friendNames.add("csl");*/
        //		List<Member> friendList=memberMng.getFriends(JSONArray.toList(friendNames));
        if(friendNames.size()!=0){

            List<Friendship> friendships=friendshipMng.getFriendshipsByUser(member, JSONArray.toList(friendNames));

            jsonMap.put("result", FriendshipWrapper.convertFriendListInfo(friendships));
            jsonMap.put("totalCount", friendships.size());
        }
        else{
            jsonMap.put("result", new ArrayList());
            jsonMap.put("totalCount",0);
        }
        jsonMap.put("unhandled_applys", applyMng.countUnhandleApplication(member));
        jsonMap.put("state", 0);
        jsonMap.put("msg", "获取好友列表成功");

        return JSONObject.fromObject(jsonMap, jsonConfig).toString();
    }

    /**
     * opt=24 将好友从黑名单里移除
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String pullFromBlackList(HttpServletRequest request,
                                    HttpServletResponse response, Map<String, String> parameters)
            throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        Member member = CmsUtils.getMember(request);
        //		Member member = memberMng.findById(1);
        String friendship_id = parameters.get("friendship_id");

        if(StringUtils.isBlank(friendship_id)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "好友关系id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        friendship_id = Encrypt.decrypt3DES(friendship_id,
                Constants.ENCRYPTION_KEY);
        Friendship friendship = friendshipMng.findById(Integer
                .parseInt(friendship_id));

        if (!member.equals(friendship.getOwner())){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "非法操作");
            return JSONUtils.printObject(jsonMap);
        }
        else if (!friendship.getFriendship_status().equals(Friendship.Friendship_status.IN_BLACKLIST)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "非法操作");
            return JSONUtils.printObject(jsonMap);
        }

        //		Member owner = friendship.getOwner();
        Member friend = friendship.getFriend();

        // 环信移除
        JSONObject result = easemobService.removeFromBlackList(
                member.getEasemob_name(),
                friend.getEasemob_name());

        if (result.getInt("responseStatus") != 200) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "服务器内部错误");
        } else {

            // 服务端从黑名单中移除
            friendshipMng.removeFromBlacklist(friendship);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "好友从黑名单移除成功");
            memberLogMng.operating(request,"cms.member.removeFromBlackList","username="+friend.getUsername());
        }
        return JSONUtils.printObject(jsonMap);
    }


    /**
     * opt=22 删除好友
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String deleteFriendship(HttpServletRequest request,
                                   HttpServletResponse response, Map<String, String> parameters)
            throws IOException {

        Member member = CmsUtils.getMember(request);
        //				Member member = memberMng.findById(1);
        String friendship_id = parameters.get("friendship_id");
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (StringUtils.isBlank(friendship_id)) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "好友关系id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        friendship_id = Encrypt.decrypt3DES(friendship_id,
                Constants.ENCRYPTION_KEY);

        Friendship friendship = friendshipMng.findById(Integer
                .parseInt(friendship_id));
		/*if (!friendship.getOwner().equals(member)
				&& !friendship.getFriend().equals(member)) {

		}*/

        if(!member.equals(friendship.getOwner())){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "你没有权限解除该好友关系");
            return JSONUtils.printObject(jsonMap);
        }

        // 环信解除
      /*  JSONObject result = easemobService.removeFriendship(member.getEasemob_name(), friendship.getFriend()
                .getEasemob_name());
        if (result.getInt("responseStatus") != 200) {
            jsonMap.put("state", -1);
            jsonMap.put("msg", "服务器内部错误");
        } else {*/

            // 服务端解除
            friendshipMng.removerFriendship(friendship);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "解除好友关系成功");
        memberLogMng.operating(request,"cms.member.cancelFriendship","friendshipId="+friendship.getId());

//        }

        return JSONUtils.printObject(jsonMap);
    }
}
