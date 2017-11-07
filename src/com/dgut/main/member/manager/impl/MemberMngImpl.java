package com.dgut.main.member.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.security.encoder.PwdEncoder;
import com.dgut.core.entity.Config;
import com.dgut.core.manager.ConfigMng;
import com.dgut.main.member.dao.MemberDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.MemberMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by PUNK on 2017/1/22.
 */
@Service
@Transactional
public class MemberMngImpl implements MemberMng {

    @Autowired
    private MemberDao dao;

    @Autowired
    private PwdEncoder pwdEncoder;

    @Autowired
    private ConfigMng configMng;

    @Override
    public boolean mobileNotExist(String mobile) {
        return dao.countByPhone(mobile) <= 0;
    }

    @Override
    public boolean usernameNotExist(String username) {
        return dao.countByUsername(username) <= 0;
    }

    @Override
    public Member saveMember(String username, String mobile, String password, String ip) {
        Member member = new Member();
        member.setUsername(username);
        member.setMobile(mobile);
        member.setPassword(pwdEncoder.encodePassword(password));
        member.setRegisterIP(ip);
        member.setRegisterTime(new Date());
        member.initialize();
        return save(member);
    }

    @Override
    public Member save(Member member) {
        return dao.save(member);
    }

    @Override
    public Member updateMember(Member member) {
        Updater<Member> updater = new Updater<Member>(member);
        return dao.updateByUpdater(updater);
    }

    @Override
    public Member findByMobile(String mobile) {
        return dao.findByMobile(mobile);
    }

    @Override
    public Member findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public Member updateMemberPwd(Member member, String password) {
        if(!StringUtils.isEmpty(password)) {
            member.setPassword(pwdEncoder.encodePassword(password));
        }
        updateMember(member);
        return member;
    }

    @Override
    public Member login(String mobile, String password, String ip) throws UsernameNotFoundException, BadCredentialsException {
        Member user = findByMobile(mobile);
        if (user == null) {
            throw new UsernameNotFoundException("mobile not found: "
                    + mobile);
        }
        if (!pwdEncoder.isPasswordValid(user.getPassword(), password)) {
            updateLoginError(user.getId(), ip);
            throw new BadCredentialsException("password invalid");
        }
        updateLoginSuccess(user.getId(), ip);
        return user;
    }

    @Override
    public Pagination getList(String username,String disabled,Integer pageNo,Integer pageSize) {
        return dao.getList(username,disabled,pageNo,pageSize);
    }

    public void updateLoginSuccess(Integer userId, String ip) {
        Member user = findById(userId);
        Date now = new Timestamp(System.currentTimeMillis());

        user.setLoginCount(user.getLoginCount() + 1);
        user.setLastLoginIP(ip);
        user.setLastLoginTime(now);

        user.setErrorCount(0);
        user.setErrorTime(null);

    }

    public void updateLoginError(Integer userId, String ip) {
        Member user = findById(userId);
        Date now = new Timestamp(System.currentTimeMillis());
        Config.ConfigLogin configLogin = configMng.getConfigLogin();
        int errorInterval = configLogin.getErrorInterval();
        Date errorTime = user.getErrorTime();


        if (errorTime == null
                || errorTime.getTime() + errorInterval * 60 * 1000 < now
                .getTime()) {
            user.setErrorTime(now);
            user.setErrorCount(1);
        } else {
            user.setErrorCount(user.getErrorCount() + 1);
        }
    }
}
