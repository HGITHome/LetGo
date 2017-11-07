package com.dgut.app.service;


import com.dgut.common.easemob.api.ChatGroupAPI;
import com.dgut.common.easemob.api.IMUserAPI;
import com.dgut.common.easemob.comm.ClientContext;
import com.dgut.common.easemob.comm.EasemobRestAPIFactory;
import com.dgut.common.easemob.comm.body.ChatGroupBody;
import com.dgut.common.easemob.comm.body.GroupOwnerTransferBody;
import com.dgut.common.easemob.comm.body.IMUserBody;
import com.dgut.common.easemob.comm.body.UserNamesBody;
import com.dgut.common.easemob.comm.wrapper.BodyWrapper;
import com.dgut.common.easemob.comm.wrapper.ResponseWrapper;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * 调用环信的service层
 * Created by PUNK on 2017/1/22.
 */

public class EasemobService {

    private static EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();

   /* @Autowired
    private static EasemobRestAPIFactory factory;*/

    private static IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);

    static ChatGroupAPI group = (ChatGroupAPI)factory.newInstance(EasemobRestAPIFactory.CHATGROUP_CLASS);

    //	objectnode转json字符串适配器
    ObjectMapper mapper=new ObjectMapper();

    /**
     * api注册用户
     * @param username
     * @param nickname
     * @param password
     * @return
     */
    public JSONObject registUser(String username, String nickname, String password){
		/*EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();

		IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);*/
        BodyWrapper userBody = new IMUserBody(username, password, nickname);

        Object result=user.createNewIMUserSingle(userBody);
        System.out.println(result);

        return JSONObject.fromObject(result);
    }

    /**
     * 添加好友关系
     * @param username
     * @param friendName
     * @return
     */
    public JSONObject addFriend(String username,String friendName){
		/*EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();

		IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);*/

        Object result=user.addFriendSingle(username, friendName);
        return JSONObject.fromObject(result);
    }


    /**
     * 拉黑好友
     * @param username
     * @param friendName
     * @return
     */
    public JSONObject addToBlackList(String username,String friendName){
        String [] users={friendName};
        UserNamesBody body=new UserNamesBody(users);

        Object result=user.addToBlackList(username, body);
        return JSONObject.fromObject(result);
    }

    /**
     * 得到用户的好友列表
     * @param username
     * @return
     * @throws JsonProcessingException
     */
    public JSONArray getFriends(String username) throws JsonProcessingException {
        ResponseWrapper result=(ResponseWrapper) user.getFriends(username);
        String body=mapper.writeValueAsString(result.getResponseBody());

        JSONObject obj=JSONObject.fromObject(body);
        return  (JSONArray) obj.get("data");

    }


    /**
     * 将好友从黑名单中移除
     * @param username
     * @param blackName
     * @return
     */
    public JSONObject removeFromBlackList(String username,String blackName){
        Object result=user.removeFromBlackList(username, blackName);
        return JSONObject.fromObject(result);
    }

    /**
     * 解除好友关系
     * @param username
     * @param friendName
     * @return
     */
    public JSONObject removeFriendship(String username,String friendName){
		/*EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();

		IMUserAPI user = (IMUserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);*/

        Object result=user.deleteFriendSingle(username, friendName);
        return JSONObject.fromObject(result);
    }

    /**
     * 创建群组
     * @param groupName
     * @param desc
     * @param isPublic
     * @param capacity
     * @param owner
     * @return
     */
    public String createGroup(String groupName,String desc,boolean isPublic,boolean approval,Long capacity,Member owner,List<Member> memberList) throws JsonProcessingException {

        String []members = null;

        if(memberList.size()!=0){
            members = new String[memberList.size()];
            for(int i =0;i<memberList.size();i++){
                members[i] = memberList.get(i).getEasemob_name();
            }
        }

        Boolean approval_flag = null;
       /* if("0".equals(isPublic)){
            approval_flag = true;
        }
        else{
            approval_flag = "1".equals(approval);
        }*/


        BodyWrapper groupBody = new ChatGroupBody(groupName,desc,isPublic, capacity,approval,owner.getEasemob_name(),members);


//        Object object = member.group.createChatGroup(groupBody);
        ResponseWrapper result=(ResponseWrapper) group.createChatGroup(groupBody);
        String body = mapper.writeValueAsString(result);

        JSONObject obj = JSONObject.fromObject(body);

        return obj.getJSONObject("responseBody").getJSONObject("data").getString("groupid");
    }


    /**
     * 修改群组信息
     * @param chatGroup
     * @param groupName
     * @param desc
     * @param capacity
     * @return
     */
    public JSONObject editGroup ( ChatGroup chatGroup,String groupName,String desc ,Long capacity){

        String name = groupName==null? chatGroup.getGroupName():groupName;
        String description = StringUtils.isBlank(desc)? chatGroup.getDescription():desc;


        BodyWrapper groupBody = new ChatGroupBody(name, description,capacity);

        Object result = group.modifyChatGroup(chatGroup.getEasemob_id(),groupBody);
        return JSONObject.fromObject(result);
    }

    /**
     * 群组转让
     * @param groupId
     * @param member
     * @return
     */
    public JSONObject changeOwner(String groupId,String member){
        BodyWrapper groupBody = new GroupOwnerTransferBody(member);
        Object result = group.transferChatGroupOwner(groupId,groupBody);
        return JSONObject.fromObject(result);
    }

    public String[] getUserJoinedGroup(String username) throws JsonProcessingException {
        ResponseWrapper result = (ResponseWrapper) user.getUserJoinedGroups(username);
        String body=mapper.writeValueAsString(result.getResponseBody());

        JSONObject obj=JSONObject.fromObject(body);
        JSONArray data = (JSONArray) obj.get("data");

        String[] groups = null;
        JSONObject  group = null;
        if(data.size()>0){
            groups = new String[data.size()];
            for(int i=0;i<data.size();i++ ){
                group = data.getJSONObject(i);
                groups[i] = group.getString("groupid");
            }
        }

        return groups;
    }

    /**
     * 群组批量加人
     * @param groupId
     * @param memberList
     * @return
     */
    public int addBatchUsersToChatGroup(String groupId,List<Member> memberList){
        String []usernames = new String[memberList.size()];
        for(int i=0;i<memberList.size();i++){
            usernames[i]=memberList.get(i).getEasemob_name();
        }
        BodyWrapper body = new UserNamesBody(usernames);
        Object result = (ResponseWrapper) group.addBatchUsersToChatGroup(groupId,body);
        JSONObject obj = JSONObject.fromObject(result);
        return obj.getInt("responseStatus");
    }

    /**
     * 群组批量减人
     * @param groupId
     * @param memberList
     * @return
     */
    public int removeBatchUsersFromChatGroup(String groupId,List<Member> memberList){
        String[] usernames = new String[memberList.size()];
        for(int i=0;i<memberList.size();i++){
            usernames[i] = memberList.get(i).getEasemob_name();
        }
        Object result = (ResponseWrapper) group.removeBatchUsersFromChatGroup(groupId,usernames);
        JSONObject obj = JSONObject.fromObject(result);
        return obj.getInt("responseStatus");
    }

    /**
     * 删除群组
     * @param groupId
     * @return
     */
    public int deleteGroup(String groupId){
        Object result = group.deleteChatGroup(groupId);
        JSONObject obj = JSONObject.fromObject(result);
        return obj.getInt("responseStatus");
    }



}
