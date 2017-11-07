package com.dgut.main.manager;

import com.dgut.common.security.BadCredentialsException;
import com.dgut.common.security.UsernameNotFoundException;
import com.dgut.common.web.session.SessionProvider;
import com.dgut.main.entity.AdminAuthentication;
import com.dgut.main.entity.base.BaseAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by PUNK on 2017/1/15.
 */
public interface AuthenticationMng {

    // 过期时间
    static final int timeout = 30 * 60 * 1000; // 30分钟

    // 间隔时间
    static final  int interval = 4 * 60 * 60 * 1000; // 4小时

    //销毁时间
    static final int expire = 24*24 * 60 * 60 * 1000; //cookie 最长时长24天

    /**
     * 认证信息session key
     */
    public static final String AUTH_KEY = "auth_key";

    public static final String Member_AUTH_KEY = "member_auth_key";

    public BaseAuthentication findById(String id);




    /**
     * 通过认证ID，获得认证信息。本方法会检查认证是否过期。
     *
     * @param authId
     *            认证ID
     * @return 返回Authentication对象。如果authId不存在或已经过期，则返回null。
     */
    public BaseAuthentication retrieve(String authId);


    public Integer retrieveUserIdFromSession(SessionProvider session,
                                             HttpServletRequest request);

    /**
     * 删除认证信息
     *
     * @param id
     * @return
     */
    public BaseAuthentication deleteById(String id);

    BaseAuthentication login(String username, String password, String ip, HttpServletRequest request, HttpServletResponse response, SessionProvider session) throws BadCredentialsException, UsernameNotFoundException;


    BaseAuthentication save(BaseAuthentication bean);

    Integer retrieveUserIdFromToken(String token, HttpServletRequest request);
}
