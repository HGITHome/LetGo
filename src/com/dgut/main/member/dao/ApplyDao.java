package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Member;

/**
 * Created by PUNK on 2017/1/28.
 */
public interface ApplyDao {


    public ApplyFriend getPreviousUnhandledApply(Member publisher, Member receiver) ;

    ApplyFriend updateByUpdater(Updater<ApplyFriend> updater);

    ApplyFriend save(ApplyFriend bean);

    ApplyFriend findById(int id);

    Integer countUnhandleApplication(Member member);

    Pagination findByUser(Integer id, int pageNo, int pageSize);

    Pagination getApplyList(String username, String status, Integer pageNo, Integer pageSize);
}
