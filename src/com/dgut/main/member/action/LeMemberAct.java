package com.dgut.main.member.action;

import com.dgut.common.page.Pagination;

import com.dgut.common.web.CookieUtils;
import com.dgut.main.entity.Admin;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.manager.MemberMng;
import com.dgut.main.web.CmsUtils;
import com.dgut.main.web.WebErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dgut.common.page.SimplePage.cpn;
import static com.dgut.common.page.SimplePage.cps;

/**
 * Created by PUNK on 2017/2/10.
 */
@Controller
@RequestMapping("/member")
public class LeMemberAct {

    private static Logger log = LoggerFactory.getLogger(LeMemberAct.class);

    @Autowired
    private MemberMng memberMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    @RequestMapping("v_list.do")
    public String getMemberList(HttpServletRequest request, HttpServletResponse response,ModelMap model ){
       String username = request.getParameter("queryUsername");
        String disabled = request.getParameter("disabled");
        String pageNo = request.getParameter("pageNo");

        Pagination pagination =memberMng.getList(username,disabled,cpn(pageNo), CookieUtils
                .getPageSize(request));
        model.addAttribute("queryUsername",username);
        model.addAttribute("disabled",disabled);
        model.addAttribute("pageNo",pagination.getPageNo());
        model.addAttribute("pageSize","10");
        model.addAttribute("pagination",pagination);
        return "member/global/list";
    }

    @RequestMapping("v_edit.do")
    public String edit(Integer id, HttpServletRequest request, ModelMap model) {

        Admin currUser = CmsUtils.getAdmin(request);
        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        Member member = memberMng.findById(id);

        model.addAttribute("cmsMember", member);

        return "member/global/edit";
    }

    @RequestMapping("o_update.do")
    public String update(Member bean, String newPassword, Boolean queryDisabled, HttpServletResponse response,HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateUpdate(bean.getId(), request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        bean = memberMng.updateMemberPwd(bean, newPassword);
        log.info("update Member id={}.", bean.getId());
        adminLogMng.operating(request, "cms.member.update", "id=" + bean.getId()
                + ";username=" + bean.getUsername());
        return getMemberList(request, response ,model);
    }

    private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }

        return errors;
    }

    /**
     * 编辑校验
     * @param id
     * @param request
     * @return
     */
    private WebErrors validateEdit(Integer id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }
        return errors;
    }

    private boolean vldExist(Integer id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        Member entity = memberMng.findById(id);
        if (errors.ifNotExist(entity, Member.class, id)) {
            return true;
        }
        return false;
    }
}
