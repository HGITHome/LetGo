package com.dgut.main.manager.impl;

import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.web.session.SessionProvider;
import com.dgut.main.dao.AuthenticationDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AdminAuthentication;
import com.dgut.main.entity.base.BaseAuthentication;
import com.dgut.main.manager.AdminMng;
import com.dgut.main.manager.AuthenticationMng;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Created by PUNK on 2017/1/15.
 */
@Service
@Transactional
public class AdminAuthenticationMngImpl implements AuthenticationMng{
    private Logger log = LoggerFactory.getLogger(AdminAuthenticationMngImpl.class);


    private AdminMng adminMng;

    @Autowired
    public void setAdminMng(AdminMng adminMng) {
        this.adminMng = adminMng;
    }

    private AuthenticationDao authenticationDao;

    @Resource(name = "adminAuthenticationDao")
    public void setAuthenticationDao(AuthenticationDao authenticationDao) {
        this.authenticationDao = authenticationDao;
    }



    // 刷新时间。
    private long refreshTime = getNextRefreshTime(System.currentTimeMillis(),
            this.interval);





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
    public BaseAuthentication findById(String id) {
        return authenticationDao.findById(id);
    }

    @Override
    public BaseAuthentication retrieve(String authId) {
        long current = System.currentTimeMillis();
        // 是否刷新数据库
        if (refreshTime < current) {
            refreshTime = getNextRefreshTime(current, interval);
            int count = authenticationDao.deleteExpire(new Date(current - timeout));
            log.info("refresh Authentication, delete count: {}", count);
        }
        BaseAuthentication auth = findById(authId);
        if (auth != null && auth.getUpdateTime().getTime() + timeout > current) {
            auth.setUpdateTime(new Timestamp(current));
            return auth;
        } else {
            return null;
        }
    }

    @Override
    public Integer retrieveUserIdFromSession(SessionProvider session, HttpServletRequest request) {
        String authId = (String) session.getAttribute(request, AUTH_KEY);
        if (authId == null) {
            return null;
        }
        BaseAuthentication auth = retrieve(authId);
        if (auth == null) {
            return null;
        }
        return auth.getUid();
    }

    @Override
    public BaseAuthentication deleteById(String id) {
        BaseAuthentication bean = authenticationDao.deleteById(id);
        return bean;
    }

    @Override
    public BaseAuthentication login(String username, String password, String ip, HttpServletRequest request, HttpServletResponse response, SessionProvider session) throws BadCredentialsException, UsernameNotFoundException {
        Admin admin = adminMng.login(username, password);
        BaseAuthentication auth = new AdminAuthentication();
        auth.setUid(admin.getId());
//        auth.setUsername(admin.getUsername());
        auth.setLoginIP(ip);
        save(auth);
        session.setAttribute(request, response, AUTH_KEY, auth.getId());
        return auth;
    }

    @Override
    public  BaseAuthentication save(BaseAuthentication bean) {
        bean.setId(StringUtils.remove(UUID.randomUUID().toString(), '-'));
        bean.init();

        bean=authenticationDao.save(bean);
        return bean;
    }

    @Override
    public Integer retrieveUserIdFromToken(String token, HttpServletRequest request) {
        if (token == null) {
            return null;
        }
        BaseAuthentication auth = retrieve(token);
        if (auth == null) {
            return null;
        }
        return auth.getUid();
    }
}
