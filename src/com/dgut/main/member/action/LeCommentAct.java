package com.dgut.main.member.action;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.TalkComment;
import com.dgut.main.member.manager.TalkCommentMng;
import com.dgut.main.web.WebErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * Created by PUNK on 2017/2/11.
 */
@Controller
@RequestMapping("comment")
public class LeCommentAct {

    @Autowired
    private TalkCommentMng commentMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    private final static Logger logger = LoggerFactory.getLogger(LeCommentAct.class);

    @RequestMapping("v_list.do")
    public String getComments(ModelMap model, HttpServletRequest request){
        String queryUsername = request.getParameter("queryUsername");
        String queryStatus = request.getParameter("queryStatus");
        String pageNo = request.getParameter("pageNo");
        Pagination pagination = commentMng.getComments(queryUsername,queryStatus,cpn(pageNo), CookieUtils.getPageSize(request));

        model.addAttribute("pagination",pagination);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("queryUsername",queryUsername);
        model.addAttribute("queryStatus",queryStatus);

        return "member/comment/list";
    }

    @RequestMapping("v_disabled.do")
    public String disabledComment(ModelMap model,HttpServletRequest request){
        String id = request.getParameter("id");
        WebErrors errors = validateDisabled(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        TalkComment bean = commentMng.findById(Integer.parseInt(id));
        commentMng.disabledComment(bean);

        model.addAttribute("message", "global.success");
        adminLogMng.operating(request,"cms.comment.disabled","id="+bean.getId());



        return getComments(model,request);
    }

    @RequestMapping("commentOpe.do")
    public String commentOperation(ModelMap model,HttpServletRequest request){
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        WebErrors errors = validateDisabled(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        TalkComment bean = commentMng.findById(Integer.parseInt(id));
        if (type.equals("0")) {

            commentMng.setCommentValid(bean);
        }
        else{
            commentMng.disabledComment(bean);
        }



        adminLogMng.operating(request,"cms.comment.disabled","id="+bean.getId());



        return "redirect:../talk/o_info.do?id="+bean.getTalk().getId()+"";

    }

    private WebErrors validateDisabled(String id, HttpServletRequest request) {
        WebErrors errors= WebErrors.create(request);
        if(vldExist(id,errors)){
            return errors;
        }
        return errors;
    }

    private boolean vldExist(String id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        TalkComment entity = commentMng.findById(Integer.parseInt(id));
        if (errors.ifNotExist(entity, TalkComment.class, id)) {
            return true;
        }
        return false;
    }
}
