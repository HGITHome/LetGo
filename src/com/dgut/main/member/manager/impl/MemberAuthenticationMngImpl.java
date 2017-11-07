package com.dgut.main.member.manager.impl;

import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.web.session.SessionProvider;
import com.dgut.main.dao.AuthenticationDao;
import com.dgut.main.entity.base.BaseAuthentication;
import com.dgut.main.manager.AuthenticationMng;

import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.MemberAuthentication;
import com.dgut.main.member.manager.MemberMng;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by PUNK on 2017/1/22.
 */
@Service
public class MemberAuthenticationMngImpl implements AuthenticationMng {

    private static final Logger log = LoggerFactory.getLogger(MemberAuthenticationMngImpl.class);

    // 刷新时间。
    private long refreshTime = getNextRefreshTime(System.currentTimeMillis(),
            this.interval);



    @Override
    public BaseAuthentication findById(String id) {
        return dao.findById(id);
    }


    /**
     * 获得下一个刷新时间
     * @param current
     * @param interval
     * @return 随机间隔时间
     */
    private long getNextRefreshTime(long current, int interval) {
        return current + interval;
        // 为了防止多个应用同时刷新，间隔时间=interval+RandomUtils.nextInt(interval/4);
        // return current + interval + RandomUtils.nextInt(interval / 4);
    }

    @Override
    public BaseAuthentication retrieve(String authId) {
        long current = System.currentTimeMillis();
        // 是否刷新数据库
        if (refreshTime < current) {
            refreshTime = getNextRefreshTime(current, interval);
            int count = dao.deleteExpire(new Date(current - expire));
            log.info("refresh Authentication, delete count: {}", count);
        }
        BaseAuthentication auth = findById(authId);
        if (auth != null && auth.getUpdateTime().getTime() + expire > current) {
            auth.setUpdateTime(new Timestamp(current));
            return auth;
        } else {
            return null;
        }
    }

    /**
     * 适合短连接 session20分钟后过期
     * @param session
     * @param request
     * @return
     */
    @Override
    public Integer retrieveUserIdFromSession(SessionProvider session, HttpServletRequest request) {
        String authId = (String) session.getAttribute(request, Member_AUTH_KEY);
        if (authId == null) {
            return null;
        }
       BaseAuthentication auth = retrieve(authId);
        if (auth == null) {
            return null;
        }
        return auth.getUid();
    }

    //适合长连接
    @Override
    public Integer retrieveUserIdFromToken(String token,
                                           HttpServletRequest request) {

        if (token == null) {
            return null;
        }
        BaseAuthentication auth = retrieve(token);
        if (auth == null) {
            return null;
        }
        return auth.getUid();
    }

    @Override
    public BaseAuthentication deleteById(String id) {
       BaseAuthentication bean = dao.deleteById(id);
        return bean;
    }

    public void storeAuthIdToSession(SessionProvider session,
                                     HttpServletRequest request, HttpServletResponse response,
                                     String authId) {
        session.setAttribute(request, response, Member_AUTH_KEY, authId);
    }

    @Override
    public BaseAuthentication login(String username, String password, String ip, HttpServletRequest request, HttpServletResponse response, SessionProvider session) throws BadCredentialsException, UsernameNotFoundException {
        Member user = memberMng.login(username, password, ip);
        MemberAuthentication auth = new MemberAuthentication();
        auth.setUid(user.getId());

        auth.setLoginIP(ip);
        save(auth);
        session.setAttribute(request, response, Member_AUTH_KEY, auth.getId());
        return auth;

    }

    @Override
    public BaseAuthentication save(BaseAuthentication bean) {
        bean.setId(StringUtils.remove(UUID.randomUUID().toString(), '-'));
        bean.init();
        dao.save(bean);
        return bean;
    }


    @Resource(name="memberAuthenticationDao")
    private AuthenticationDao dao;

    @Autowired
    private MemberMng memberMng;
}
