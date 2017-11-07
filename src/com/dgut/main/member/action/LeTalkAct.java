package com.dgut.main.member.action;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.member.manager.TalkMng;
import com.dgut.main.web.WebErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * Created by PUNK on 2017/2/10.
 */
@Controller
@RequestMapping("talk")
public class LeTalkAct {

    @Autowired
    private TalkMng talkMng;

    private static Logger logger= LoggerFactory.getLogger(LeTalkAct.class);


    @RequestMapping("v_list.do")
    public String getTalkList(HttpServletRequest request ,ModelMap model){
        String pageNo = request.getParameter("pageNo");
        String queryUsername = request.getParameter("queryUsername");
        String queryStatus = request.getParameter("queryStatus");
        Pagination pagination = talkMng.getTalkList(queryUsername,queryStatus,cpn(pageNo), CookieUtils.getPageSize(request));


        model.addAttribute("pageNo",pageNo);
        model.addAttribute("pagination",pagination);
        model.addAttribute("queryUsername",queryUsername);
        model.addAttribute("queryStatus",queryStatus);
        return "member/talk/list";
    }

    @RequestMapping("o_info.do")
    public String getTalkInfo(HttpServletRequest request,ModelMap model){

        String id = request.getParameter("id");
        WebErrors errors = validateInfo(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        Talk talk = talkMng.findById(Integer.parseInt(id));




        model.addAttribute("talk",talk);
        System.out.println(talk);

        return "member/talk/talkInfo";
    }

    private WebErrors validateInfo(String id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }

        return errors;
    }

    private boolean vldExist(String id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        Talk entity = talkMng.findById(Integer.parseInt(id));
        if (errors.ifNotExist(entity, Talk.class, id)) {
            return true;
        }
        return false;
    }
}
