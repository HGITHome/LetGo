package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.TalkComment;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
public interface TalkCommentDao {

    TalkComment findById(Integer id);

    TalkComment updateByUpdater(Updater<TalkComment> updater);

    Pagination getList(String username, String status,int pageNo, int pageSize);

    List<Object[]> getSexInfo();
}
