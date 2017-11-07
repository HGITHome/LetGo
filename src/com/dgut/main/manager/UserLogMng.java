package com.dgut.main.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.entity.base.BaseUserLog;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by PUNK on 2017/1/18.
 */
public interface UserLogMng  {


    

    public BaseUserLog save(BaseUserLog bean);

    public BaseUserLog operating(HttpServletRequest request, String title,
                                 String content);

    public BaseUserLog loginFailure(HttpServletRequest request,  String username,String title, String content) ;

    public BaseUserLog loginSuccess(HttpServletRequest request,String username,
                                 String title);


    Pagination getPage(int operating, String queryUsername, String queryTitle, String queryIp, int pageNo, int pageSize);
}
