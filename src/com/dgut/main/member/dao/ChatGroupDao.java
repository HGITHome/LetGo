package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.ChatGroup;

import java.util.List;

/**
 * Created by PUNK on 2017/2/23.
 */
public interface ChatGroupDao {

    ChatGroup save(ChatGroup bean);

    ChatGroup findById(Integer id);

    ChatGroup updateByUpdater(Updater<ChatGroup> updater);

    List<ChatGroup> getUserJoinedGroup(String[] groups);

    void removeBatchUserFromGroup(ChatGroup group, Integer[] userIds);

    Pagination getList(String queryUsername, String queryDisabled, int pageNo, int pageSize);

    Pagination getMemberList(Integer group_id, String queryUsername, String queryDisabled, int pageNo, int pageSize);
}
