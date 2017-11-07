package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Talk;

import java.util.List;

/**
 * Created by PUNK on 2017/1/31.
 */
public interface TalkDao {
    Talk findById(int id);

    Talk updateByUpdater(Updater<Talk> updater);

    Talk save(Talk talk);

    Pagination findTalkByUser(Member member, int pageNo, int pageSize);

    Pagination getTalkList(Member member, int pageNo, int pageSize);

    Pagination getTalkList(String username, String status, int pageNo, int pageSize);

    List<Object[]> getTop3ShareMen();

    List<Object[]> getTop3TapLikeMen();

    List<Object[]> getSexInfo();

    List<Object[]> getTimeInfo();
}
