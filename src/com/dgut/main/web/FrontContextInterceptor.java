package com.dgut.main.web;

import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.session.SessionProvider;
import com.dgut.main.manager.AuthenticationMng;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.member.manager.impl.MemberAuthenticationMngImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * app 拦截器
 * Created by PUNK on 2017/1/23.
 */

public class FrontContextInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws ServletException {


        Member user = null;
//		Integer userId = memberAuthMng.retrieveUserIdFromSession(session, request);
        //根据token去数据库查找用户 时间是2天
        String token = null;
        token = getTokenCookie(request, response, token);

        Integer userId = memberAuthMng.retrieveUserIdFromToken(token, request);
        System.out.println("请求的用户id:"+userId);
        if (userId != null) {
            user = memberMng.findById(userId);
        }
        if (user != null) {
            CmsUtils.setMember(request, user);
        }
        return true;
    }

    private String getTokenCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        Cookie[]cookies=request.getCookies();
        Date now = new Date();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    token=cookie.getValue();
                    System.out.println("获取cookie值:"+token);
                    String ctx = request.getContextPath();
//                    cookie.setMaxAge( AuthenticationMng.expire/1000);
					/*CookieUtils.addCookie(request, response, "token",
							token, MemberAuthenticationMngImpl.expire/1000, null);*/
                    break;
                }
            }
        }
        return token;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String token = null;
        token = getTokenCookie(request, response, token);
        if(StringUtils.isNotBlank(token)){
            CookieUtils.addCookie(request, response, "token",
                    token, MemberAuthenticationMngImpl.expire/1000, null);
        }
    }

    @Autowired
    private SessionProvider session;

    @Autowired
    private MemberMng memberMng;

    @Resource(name="memberAuthenticationMng")
    private AuthenticationMng memberAuthMng;


}
