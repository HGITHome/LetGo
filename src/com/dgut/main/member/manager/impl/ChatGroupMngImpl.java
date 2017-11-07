package com.dgut.main.member.manager.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.ChatGroupDao;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.GroupMember;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.ChatGroupMng;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by PUNK on 2017/2/22.
 */
@Service
@Transactional
public class ChatGroupMngImpl implements ChatGroupMng {

    @Autowired
    private ChatGroupDao dao;



    @Transactional
    @Override
    public ChatGroup createGroup(String name, String desc, Boolean isPublic,Boolean approval, Member owner, String easemob_id,List<Member> members) {

        ChatGroup group =  new ChatGroup();

        group.setGroupName(name);
        group.setCapacity(ChatGroup.CAPACITY);
        group.setCreateTime(new Date());
        group.setDescription(desc);
        group.setDisabled(false);
        group.setEasemob_id(easemob_id);
        /*if("0".equals(isPublic)){
            member.group.setIsPublic(false);
            member.group.setApproval(true);
        }
        else{
            member.group.setIsPublic(true);
            member.group.setApproval(false);
        }*/
        group.setIsPublic(isPublic);
        group.setApproval(approval);
        group.setOwner(owner);

        List<GroupMember> memberList = new ArrayList<GroupMember>();
        GroupMember gm = null;
        if(members!=null) {
            for(Member m : members){
                gm = new GroupMember();
                gm.setEnterTime(new Date());
                gm.setMember(m);
//                gm.setGroup(group);
                gm.setChatGroup(group);
                gm.setDisabled(false);
                memberList.add(gm);
            }

        }

        //添加群主
        gm = new GroupMember();
        gm.setEnterTime(new Date());
        gm.setMember(owner);
        gm.setChatGroup(group);
//        gm.setGroup(group);
        gm.setDisabled(false);

        memberList.add(gm);

        group.setMemberList(memberList);
        return dao.save(group);
    }

    @Override
    public ChatGroup findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public ChatGroup editGroup(String groupName, String desc,ChatGroup group) {
        if(StringUtils.isNotBlank(groupName)){
            group.setGroupName(groupName);
        }

        if(StringUtils.isNotBlank(desc)){
            group.setDescription(desc);
        }

        /*if(StringUtils.isNotBlank(isPublic)){
            if("0".equals(isPublic)){
                member.group.setIsPublic(false);
                member.group.setApproval(true);
            } else if ("1".equals(isPublic)) {

                member.group.setIsPublic(true);
                member.group.setApproval(false);
            }
        }
*/
        return update(group);
    }

    @Transactional
    @Override
    public ChatGroup changeOwner(ChatGroup group, Member newOwner) {
        group.setOwner(newOwner);
        return update(group);
    }

    @Override
    public List<ChatGroup> getUserJoinedGroup(String[] groups) {
        return dao.getUserJoinedGroup(groups);
    }

    @Override
    public void addBatchUserToGroup(ChatGroup group, List<Member> memberList) {
        List<GroupMember> gmList = new ArrayList<>(memberList.size());
        GroupMember gm = null;
        for(Member m : memberList){
            gm = new GroupMember();
            gm.setDisabled(false);
            gm.setEnterTime(new Date());
//            gm.setGroup(group);
            gm.setChatGroup(group);
            gm.setMember(m);

            gmList.add(gm);
        }
        group.getMemberList().addAll(gmList);
        update(group);

    }

    @Override
    public void removeBatchUserFromGroup(ChatGroup group, List<Member> memberList) {
        /*List<GroupMember> gmList = member.group.getMemberList();
        for(GroupMember gm :gmList){
            if(gm.getDisabled()==false && memberList.contains(gm.getMember())){
                gmList.remove(gm);
            }
        }
        member.group.setMemberList(gmList);
        update(member.group);*/
        Integer[] userIds = new Integer[memberList.size()];
        for(int i=0;i<memberList.size();i++){
            userIds[i] = memberList.get(i).getId();
        }
        dao.removeBatchUserFromGroup(group,userIds);
    }

    @Override
    public void deleteGroup(ChatGroup group) {
        group.setDisabled(true);
        update(group);
    }

    @Override
    public Pagination getList(String queryUsername, String queryDisabled, int pageNo, int pageSize) {
       return dao.getList(queryUsername,queryDisabled,pageNo,pageSize);
    }

    @Override
    public Pagination getMemberListByGroupId(Integer group_id,String queryUsername, String queryDisabled, int pageNo, int pageSize) {

        return dao.getMemberList(group_id,queryUsername,queryDisabled,pageNo,pageSize);
    }

    private ChatGroup update(ChatGroup group) {
        Updater<ChatGroup> updater = new Updater<ChatGroup>(group);
        return dao.updateByUpdater(updater);
    }
}
