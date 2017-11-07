package com.dgut.main.manager.impl;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.RequestUtils;
import com.dgut.common.web.springmvc.MessageResolver;
import com.dgut.main.dao.AdminDao;
import com.dgut.main.dao.UserLogDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AdminLog;
import com.dgut.main.entity.base.BaseUserLog;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.CmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by PUNK on 2017/1/18.
 */
@Service
@Transactional
public class AdminLogMngImpl implements UserLogMng {


    @Resource(name="adminLogDao")
    private UserLogDao adminLogDao;

    @Autowired
    private AdminDao adminDao;


    /**
     * 保存日志
     * @param category
     * @param admin
     * @param url
     * @param ip
     * @param date
     * @param title
     * @param content
     * @return
     */
    public AdminLog save(Integer category, Admin admin,
                         String url, String ip, Date date, String title, String content) {

        AdminLog log = new AdminLog();
        log.setAdmin(admin);
        log.setCategory(category);
        log.setIp(ip);
        log.setTime(date);
        log.setUrl(url);
        log.setTitle(title);
        log.setContent(content);
        log.setDisabled(false);
        save(log);
        return log;
    }

    /**
     * 日志保存
     * @param bean
     * @return
     */
    @Override
    public BaseUserLog save(BaseUserLog bean) {
        return adminLogDao.save(bean);
    }


    /**
     * 操作日志
     * @param request
     * @param title
     * @param content
     * @return
     */
    public BaseUserLog operating(HttpServletRequest request, String title, String content) {
        Admin admin = CmsUtils.getAdmin(request);
        String ip = RequestUtils.getIpAddr(request);
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        Date date = new Date();
        AdminLog log = save(AdminLog.OPERATING, admin, uri, ip, date,
                MessageResolver.getMessage(request, title), content);
        return log;
    }


    /**
     * 登录成功日志
     * @param request
     * @param  username
     * @param title
     * @return
     */
    @Override
    public BaseUserLog loginSuccess(HttpServletRequest request,String username ,String title){
        //Admin admin = CmsUtils.getAdmin(request);
        Admin admin = adminDao.findByUsername(username);
        String ip = RequestUtils.getIpAddr(request);
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        Date date = new Date();
        AdminLog log = save(AdminLog.LOGIN_SUCCESS,admin, uri, ip, date,
                MessageResolver.getMessage(request,title), null);
        return log;
    }

    @Override
    public Pagination getPage(int operating, String queryUsername, String queryTitle, String queryIp, int pageNo, int pageSize) {
        return adminLogDao.getPage(operating,queryUsername,queryTitle,queryIp,pageNo,pageSize);
    }

    /**
     * 登录失败日志保存
     * @param request
     * @param title
     * @param content
     * @return
     */
    @Override
    public BaseUserLog loginFailure(HttpServletRequest request, String username,String title, String content) {

        Admin admin = adminDao.findByUsername(username);

        String ip = RequestUtils.getIpAddr(request);
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        Date date = new Date();
        AdminLog log = save(AdminLog.LOGIN_FAILURE,  admin, uri, ip, date,
                MessageResolver.getMessage(request,title), content);
        return log;
    }


}
