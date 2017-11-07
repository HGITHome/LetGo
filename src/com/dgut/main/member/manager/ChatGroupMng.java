package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.Member;

import java.util.List;
import java.util.Set;

/**
 * Created by PUNK on 2017/2/22.
 */
public interface ChatGroupMng {

    ChatGroup createGroup(String name, String desc, Boolean isPublic, Boolean approval,Member owner, String easemob_id,List<Member> members);

    ChatGroup findById(Integer id);

    ChatGroup editGroup(String groupName, String desc, ChatGroup group);

    ChatGroup changeOwner(ChatGroup group, Member newOwner);

    List<ChatGroup> getUserJoinedGroup(String[] groups);

    void addBatchUserToGroup(ChatGroup group, List<Member> memberList);

    void removeBatchUserFromGroup(ChatGroup group, List<Member> memberList);

    void deleteGroup(ChatGroup group);

    Pagination getList(String queryUsername, String queryDisabled, int cpn, int pageSize);

    Pagination getMemberListByGroupId(Integer group_id,String queryUsername,String queryDisabled,int pageNo,int pageSize);
}
