package com.dgut.main.member.manager.impl;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.RequestUtils;
import com.dgut.common.web.springmvc.MessageResolver;
import com.dgut.main.dao.UserLogDao;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.AdminLog;
import com.dgut.main.entity.base.BaseUserLog;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.dao.MemberDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.MemberLog;
import com.dgut.main.web.CmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by PUNK on 2017/1/24.
 */

@Service
@Transactional
public class MemberLogMngImpl implements UserLogMng {

    @Resource(name="memberLogDao")
    private UserLogDao logDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public BaseUserLog save(BaseUserLog bean) {
        logDao.save(bean);
        return bean;
    }

    @Override
    public BaseUserLog operating(HttpServletRequest request, String title, String content) {
        Member member = CmsUtils.getMember(request);
        String ip = RequestUtils.getIpAddr(request);
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        Date date = new Date();
        MemberLog log = save(BaseUserLog.OPERATING, member, uri, ip, date,
                MessageResolver.getMessage(request, title), content);
        return log;
    }

    @Override
    public BaseUserLog loginFailure(HttpServletRequest request, String username, String title, String content) {
        Member member = memberDao.findByMobile(username);
        String ip = RequestUtils.getIpAddr(request);
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        Date date = new Date();
        MemberLog log = save(BaseUserLog.LOGIN_FAILURE,member, uri, ip, date,
                MessageResolver.getMessage(request,title),content);
        return log;
    }

    @Override
    public BaseUserLog loginSuccess(HttpServletRequest request, String username, String title) {
        Member member = memberDao.findByMobile(username);
        String ip = RequestUtils.getIpAddr(request);
        UrlPathHelper helper = new UrlPathHelper();
        String uri = helper.getOriginatingRequestUri(request);
        Date date = new Date();
        MemberLog log = save(BaseUserLog.LOGIN_SUCCESS,member, uri, ip, date,
               title,null);
        return log;
    }

    private MemberLog save(int category, Member member, String uri, String ip, Date date, String title, String content) {
        MemberLog log = new MemberLog();
        log.setMember(member);
        log.setCategory(category);
        log.setUrl(uri);
        log.setIp(ip);
        log.setTime(new Date());
        log.setTitle(title);
        log.setContent(content);
        log.setDisabled(false);
        save(log);
        return log;

    }


    @Override
    public Pagination getPage(int operating, String queryUsername, String queryTitle, String queryIp, int pageNo, int pageSize) {
        return logDao.getPage(operating,queryUsername,queryTitle, queryIp,pageNo,pageSize);
    }
}
