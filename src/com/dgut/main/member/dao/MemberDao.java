package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;

/**
 * Created by PUNK on 2017/1/22.
 */
public interface MemberDao {

    int countByPhone(String mobile);

    int countByUsername(String username);

    Member findByMobile(String mobile);

    Member findByUsername(String username);

    Member save(Member member);


    Member updateByUpdater(Updater<Member> updater);

    Member findById(Integer id);

    Pagination getList(String username,String disabled,Integer pageNo,Integer pageSize);
}
