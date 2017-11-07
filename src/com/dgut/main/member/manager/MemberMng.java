package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.main.member.entity.Member;

/**
 * Created by PUNK on 2017/1/22.
 */
public interface MemberMng {
    boolean mobileNotExist(String mobile);

    boolean usernameNotExist(String username);

    Member saveMember(String username, String mobile, String password, String ip);

    Member save(Member member);

    Member updateMember(Member member);



    Member findByMobile(String mobile);

    Member findById(Integer id);

    Member updateMemberPwd(Member member, String password);



    Member login(String mobile, String password, String ip) throws UsernameNotFoundException, BadCredentialsException;

    Pagination getList(String username,String disabled,Integer pageNo,Integer pageSize);
}
