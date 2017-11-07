package com.dgut.common.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Session提供者
 * Created by PUNK on 2017/1/14.
 */
public interface SessionProvider {
    public Serializable getAttribute(HttpServletRequest request, String name);

    public void setAttribute(HttpServletRequest request,
                             HttpServletResponse response, String name, Serializable value);

    public String getSessionId(HttpServletRequest request,
                               HttpServletResponse response);

    public void logout(HttpServletRequest request, HttpServletResponse response);

}
