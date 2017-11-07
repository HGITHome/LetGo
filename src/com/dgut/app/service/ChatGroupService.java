package com.dgut.app.service;

import com.dgut.app.helper.ChatGroupWrapper;
import com.dgut.app.utils.JSONUtils;
import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.GroupMember;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.ChatGroupMng;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.web.CmsUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by PUNK on 2017/2/22.
 */
@Service
public class ChatGroupService {

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;

    @Autowired
    private ChatGroupMng chatGroupMng;

    @Autowired
    private EasemobService easemobService;

    @Autowired
    private MemberMng memberMng;



    /**
     * opt=51 创建群组(私密)
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String createGroup(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member = CmsUtils.getMember(request);

        String groupName = parameters.get("groupName");
        String desc = parameters.get("desc");
//        String isPublic = parameters.get("isPublic");
        String members = parameters.get("memberList");
//        String approval = parameters.get("approval");

        List<Member> memberList =  null;

        if(StringUtils.isBlank(groupName)){
            jsonMap.put("state",-1);
            jsonMap.put("msg", "群组名不能为空");
            return JSONUtils.printObject(jsonMap);
        }

       /* if(StringUtils.isBlank(desc)){
            jsonMap.put("state",-1);
            jsonMap.put("msg", "群组介绍不能为空");
            return JSONUtils.printObject(jsonMap);
        }*/

       /* if(StringUtils.isBlank(approval)){
            jsonMap.put("state",-1);
            jsonMap.put("msg", "群组邀请权限不能为空");
            return JSONUtils.printObject(jsonMap);
        }*/

        if(StringUtils.isNotBlank(members)){
            memberList = convertMember(members);
        }



        String easemob_id =easemobService.createGroup(groupName,desc,false,Boolean.TRUE, ChatGroup.CAPACITY,member,memberList);


        if(StringUtils.isBlank(easemob_id)){
           jsonMap.put("state", -1);
            jsonMap.put("msg" , "群组创建失败，系统内部错误");
            return JSONUtils.printObject(jsonMap);
        }
        else{
//            String body=mapper.writeValueAsString(result.getResponseBody());



            chatGroupMng.createGroup(groupName,desc,false,Boolean.TRUE ,member,easemob_id,memberList);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "群组创建成功");
            memberLogMng.operating(request,"cms.chatGroup.add","群组名为"+groupName);
        }
        return JSONUtils.printObject(jsonMap);
    }

    /**
     * 转换member列表
     * @param members
     * @return
     */
    private List<Member> convertMember(String members) {
        List<Member> list = new ArrayList<>();
        String[] array = members.split(",");
        Member member = null;
        for(String str: array){

            str = Encrypt.decrypt3DES(str,Constants.ENCRYPTION_KEY);
            member = memberMng.findById(Integer.parseInt(str));
            if(member!=null){
                list.add(member);
            }
        }
        return list;
    }

    /**
     * opt=52 修改群组信息
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String editGroup(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member = CmsUtils.getMember(request);

        String groupId = parameters.get("group_id");
        String groupName = parameters.get("groupName");
        String desc = parameters.get("desc");
//        String isPublic  = parameters.get("isPublic");

        if(StringUtils.isBlank(groupId)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        groupId = Encrypt.decrypt3DES(groupId, Constants.ENCRYPTION_KEY);

        ChatGroup group = chatGroupMng.findById(Integer.parseInt(groupId));

        //群公告只能有群主编辑
        if(StringUtils.isNotBlank(desc)){
            if(!member.equals(group.getOwner())){
                jsonMap.put("state" , -1);
                jsonMap.put("msg" , "只有群主才能编辑群公告");
                return JSONUtils.printObject(jsonMap);
            }
        }

       /* if(StringUtils.isNotBlank(isPublic)){
            if(!member.equals(member.group.getOwner())){
                jsonMap.put("state" , -1);
                jsonMap.put("msg" , "只有群主才能修改群权限");
                return JSONUtils.printObject(jsonMap);
            }
        }*/

        JSONObject result = easemobService.editGroup(group,groupName,desc,ChatGroup.CAPACITY);
        if(result.getInt("responseStatus")!=200){
            jsonMap.put("state","-1");
            jsonMap.put("msg","修改群组信息失败");
            return JSONUtils.printObject(jsonMap);
        }
        else {
            group = chatGroupMng.editGroup(groupName, desc, group);
            memberLogMng.operating(request, "cms.chatGroup.edit", "群组名:" + group.getGroupName() + ",群组公告:" + group.getDescription() );

            jsonMap.put("state",0);
            jsonMap.put("msg","修改群组信息成功");
            return JSONUtils.printObject(jsonMap);
        }
    }

    /**
     * opt=53 更换群主
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String changeOwner(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        Member member = CmsUtils.getMember(request);

        String groupId = parameters.get("group_id");
        String userId = parameters.get("user_id");

        if(StringUtils.isBlank(groupId)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        groupId = Encrypt.decrypt3DES(groupId,Constants.ENCRYPTION_KEY);

        if(StringUtils.isBlank(userId)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","新群主id不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        userId = Encrypt.decrypt3DES(userId,Constants.ENCRYPTION_KEY);

        ChatGroup group = chatGroupMng.findById(Integer.parseInt(groupId));

        if(!member.equals(group.getOwner())){
            jsonMap.put("state" ,-1);
            jsonMap.put("msg","你不是群主，没有权限指定新群主");
            return JSONUtils.printObject(jsonMap);
        }

        Member newOwner = memberMng.findById(Integer.parseInt(userId));
        if(group.getOwner().equals(newOwner)){
            jsonMap.put("state", -1);
            jsonMap.put("msg", "你已经是新群主，转让失败");
            return JSONUtils.printObject(jsonMap);
        }



        List<GroupMember> memberList = group.getMemberList();
        if(!isExisted(newOwner,memberList)){
//        if(!memberList.contains(newOwner)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","新群主不在该群组，操作失败");
            return JSONUtils.printObject(jsonMap);
        }

        JSONObject result = easemobService.changeOwner(group.getEasemob_id(),newOwner.getEasemob_name());
        if(result.getInt("responseStatus")!=200){
            jsonMap.put("state","-1");
            jsonMap.put("msg","群组转让失败");
            return JSONUtils.printObject(jsonMap);
        }
        else{
            chatGroupMng.changeOwner(group,newOwner);
            memberLogMng.operating(request,"cms.chatGroup.changeOwner","新群主为"+newOwner.getUsername());
            jsonMap.put("state",0);
            jsonMap.put("msg","群组转让成功");
            return JSONUtils.printObject(jsonMap);
        }
    }

    /**
     * 检查用户是否在群组中
     * @param memnber
     * @param memberList
     * @return
     */
    private boolean isExisted(Member memnber, List<GroupMember> memberList) {
        Boolean isExisted = false;
        for(GroupMember gm : memberList){
            if(gm.getMember().equals(memnber)){
                isExisted = true;
                break;
            }
        }
        return isExisted;
    }

    /**
     * opt=57 获得所有群组
     * @param request
     * @param response
     * @param parameters
     * @return
     * @throws IOException
     */
    public String getGroupByUser(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {

        Map<String,Object> jsonMap = new HashMap<String,Object>();

        Member member = CmsUtils.getMember(request);

        String[] groups = easemobService.getUserJoinedGroup(member.getEasemob_name());

        List<ChatGroup> groupList =  new ArrayList<ChatGroup>();

        if(groups != null){
            groupList = chatGroupMng.getUserJoinedGroup(groups);
            jsonMap.put("result", ChatGroupWrapper.convertChatGroup(groupList,member));
        }
        else{
            jsonMap.put("result", groupList);
        }



        jsonMap.put("total",groupList.size());
        jsonMap.put("state", 0 );
        jsonMap.put("msg", "获取群组成功");

        return JSONUtils.printObject(jsonMap);

    }

    /**
     * opt=54 添加用户到群组
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String addUser(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        Member member = CmsUtils.getMember(request);

        String group_id = parameters.get("group_id");
        String members = parameters.get("memberList");
        if(StringUtils.isBlank(group_id)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组id不能为空");
            return JSONUtils.printObject(jsonMap);
        }
        else{
            group_id = Encrypt.decrypt3DES(group_id,Constants.ENCRYPTION_KEY);
        }

        ChatGroup group = chatGroupMng.findById(Integer.parseInt(group_id));
        if(!group.getOwner().equals(member)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","你不是群主，不能邀请人");
            return  JSONUtils.printObject(jsonMap);
        }

        if(StringUtils.isBlank(members)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","邀请人不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        List<Member> memberList = getNewMember(group.getMemberList(),members);
        Integer flag=easemobService.addBatchUsersToChatGroup(group.getEasemob_id(),memberList);
        if(flag!=200){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组加人失败");
            return JSONUtils.printObject(jsonMap);
        }
        else{
            chatGroupMng.addBatchUserToGroup(group,memberList);
            List<String> usernames = new ArrayList<>(memberList.size());
            for(Member m  :memberList){
                usernames.add(m.getUsername());
            }
            memberLogMng.operating(request,"cms.chatGorup.member.delete",StringUtils.join(usernames.toArray(),","));
            jsonMap.put("state",0);
            jsonMap.put("msg","添加成员成功");
            return JSONUtils.printObject(jsonMap);
        }

    }

    /**
     * 得到新加入的会员
     * @param gmList
     * @param members
     * @return
     */
    private List<Member> getNewMember(List<GroupMember> gmList,String members) {
        List<Member> list = convertMember(members);


        for(GroupMember gm :gmList){
            if(gm.getDisabled()==false && list.contains(gm.getMember())){
                list.remove(gm.getMember());
            }
        }

        return list;

    }

    /**
     * opt=56 删除群组
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String deleteGroup(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap =new HashMap<>();
        Member member = CmsUtils.getMember(request);
        String group_id = parameters.get("group_id");
        if(StringUtils.isBlank(group_id)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组号不能为空");
            return  JSONUtils.printObject(jsonMap);
        }

        group_id = Encrypt.decrypt3DES(group_id,Constants.ENCRYPTION_KEY);

        ChatGroup group = chatGroupMng.findById(Integer.parseInt(group_id));



        if(!group.getOwner().equals(member)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","你不是群主，无法解散群");
            return JSONUtils.printObject(jsonMap);
        }

        Integer flag = easemobService.deleteGroup(group.getEasemob_id());
        if(flag!=200){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组解散失败");
            return JSONUtils.printObject(jsonMap);
        }
        else {
            chatGroupMng.deleteGroup(group);
            memberLogMng.operating(request, "cms.chatGroup.delete", null);
            jsonMap.put("state", 0);
            jsonMap.put("msg", "解散群组成功");
            return JSONUtils.printObject(jsonMap);
        }
    }

    /**
     * opt=55 群组减人
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    public String subUser(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters) throws IOException {
        Map<String,Object> jsonMap = new HashMap<>();
        Member member = CmsUtils.getMember(request);
        String group_id = parameters.get("group_id");
        String members = parameters.get("memberList");

        if(StringUtils.isBlank(group_id)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组号不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        group_id = Encrypt.decrypt3DES(group_id,Constants.ENCRYPTION_KEY);

        if(StringUtils.isBlank(members)){
            jsonMap.put("state",-1);
            jsonMap.put("msg","成员编号不能为空");
            return JSONUtils.printObject(jsonMap);
        }

        ChatGroup group = chatGroupMng.findById(Integer.parseInt(group_id));

        List<Member> memberList = convertMember(members);
        Integer flag = easemobService.removeBatchUsersFromChatGroup(group.getEasemob_id(),memberList);

        if(flag!=200){
            jsonMap.put("state",-1);
            jsonMap.put("msg","群组移除成员失败");
            return  JSONUtils.printObject(jsonMap);
        }
        else{
            chatGroupMng.removeBatchUserFromGroup(group,memberList);
            memberLogMng.operating(request,"cms.chatGorup.member.delete",null);
            jsonMap.put("state",0);
            jsonMap.put("msg","群组移除成员成功");
            return  JSONUtils.printObject(jsonMap);
        }

    }
}
