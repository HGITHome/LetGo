package com.dgut.main.member.action;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.entity.AdminLog;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.MemberLog;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * 会员日志action
 * Created by PUNK on 2017/1/19.
 */

@Controller
@RequestMapping("member/log")
public class LeMemberLogAct {

    private static final Logger log = LoggerFactory.logger(LeMemberLogAct.class);

    @RequestMapping("v_list_operating.do")
    public String listOperating(String queryUsername, String queryTitle,
                                String queryIp, String pageNo, HttpServletRequest request,
                                ModelMap model) {
        Pagination pagination = memberLogMng.getPage(MemberLog.OPERATING, queryUsername, queryTitle, queryIp, cpn(pageNo), CookieUtils
                .getPageSize(request));
        model.addAttribute("pagination", pagination);
        model.addAttribute("pageNo", pagination.getPageNo());
        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryTitle", queryTitle);
        model.addAttribute("queryIp", queryIp);
        return "member/log/list_operating";
    }

    @RequestMapping("v_list_login_success.do")
    public String listLoginSuccess(String queryUsername, String queryTitle,
                                   String queryIp, String pageNo, HttpServletRequest request,
                                   ModelMap model) {
        Pagination pagination = memberLogMng.getPage(MemberLog.LOGIN_SUCCESS,
                queryUsername, queryTitle, queryIp, cpn(pageNo), CookieUtils
                        .getPageSize(request));
        model.addAttribute("pagination", pagination);
        model.addAttribute("pageNo", pagination.getPageNo());
        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryTitle", queryTitle);
        model.addAttribute("queryIp", queryIp);
        return "member/log/list_login_success";
    }

    @RequestMapping("v_list_login_failure.do")
    public String listLoginFailure(String queryTitle, String queryIp,
                                   String pageNo, HttpServletRequest request, ModelMap model) {
        Pagination pagination = memberLogMng.getPage(MemberLog.LOGIN_FAILURE,
                null, queryTitle, queryIp, cpn(pageNo), CookieUtils
                        .getPageSize(request));
        model.addAttribute("pagination", pagination);
        model.addAttribute("pageNo", pagination.getPageNo());
        model.addAttribute("queryTitle", queryTitle);
        model.addAttribute("queryIp", queryIp);
        return "member/log/list_login_failure";
    }

    @Resource(name="memberLogMng")
    private UserLogMng memberLogMng;
}
