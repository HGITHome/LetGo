package com.dgut.main.member.action;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.manager.ApplyMng;
import com.dgut.main.web.WebErrors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * Created by PUNK on 2017/2/10.
 */
@Controller
@RequestMapping("apply")
public class LeApplyAct {
    private static Logger log = LoggerFactory.getLogger(LeApplyAct.class);

    @Autowired
    private ApplyMng applyMng;

    @RequestMapping("v_list.do")
    private String getApply(HttpServletRequest request, HttpServletResponse response,ModelMap model){
        String queryStatus = request.getParameter("queryStatus");
        String queryUsername = request.getParameter("queryUsername");
        String pageNo = request.getParameter("pageNo");
        Pagination pagination = applyMng.getApplyList(queryUsername,queryStatus,cpn(pageNo), CookieUtils.getPageSize(request));
        model.addAttribute("pagination",pagination);
        model.addAttribute("queryUsername",queryUsername);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("pageNo",pageNo);
        return "member/apply/list";
    }

    @RequestMapping("info.do")
    public String getApplyInfo(ModelMap model,HttpServletRequest request){
        String id=request.getParameter("id");
        WebErrors errors = validate(Integer.parseInt(id),request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        ApplyFriend entity = applyMng.findById(Integer.parseInt(id));
        model.addAttribute("apply",entity);
        return "member/apply/info";
    }

    private WebErrors validate(Integer id,HttpServletRequest request) {
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
        ApplyFriend entity = applyMng.findById(id);
        if (errors.ifNotExist(entity, ApplyFriend.class, id)) {
            return true;
        }
        return false;
    }

}
