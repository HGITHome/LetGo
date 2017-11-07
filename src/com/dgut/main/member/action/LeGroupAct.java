package com.dgut.main.member.action;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.member.entity.ChatGroup;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.manager.ChatGroupMng;
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
 * Created by PUNK on 2017/3/20.
 */
@Controller
@RequestMapping("group")
public class LeGroupAct {
    private Logger logger = LoggerFactory.getLogger(LeGroupAct.class);

    @Autowired
    private ChatGroupMng chatGroupMng;

    @RequestMapping("v_list.do")
    public String list(String pageNo,String queryUsername,String queryStatus,HttpServletRequest request, HttpServletResponse response,ModelMap model){
        Pagination pagination  = chatGroupMng.getList(queryUsername,queryStatus,cpn(pageNo), CookieUtils.getPageSize(request));

        model.put("pagination",pagination);
        model.put("queryUsername",queryUsername);
        model.put("queryStatus",queryStatus);
        model.put("pageNo",pageNo);

        return "member/group/list";
    }

    @RequestMapping("o_info.do")
    public String getGroupInfo(String id,String pageNo,String queryUsername,String queryDisabled,HttpServletRequest request,ModelMap model){

        WebErrors errors = validateInfo(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        ChatGroup group = chatGroupMng.findById(Integer.parseInt(id));
        Pagination pagination = chatGroupMng.getMemberListByGroupId(group.getId(),queryUsername,queryDisabled,cpn(pageNo),CookieUtils.getPageSize(request));
        model.put("chatGroup",group);
        model.put("pagination",pagination);
        model.put("pageNo",pageNo);
        model.put("queryUsername",queryUsername);
        model.put("queryDisabled",queryDisabled);
        return "member/group/groupInfo";
    }

    private WebErrors validateInfo(String group_id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(vldExist(group_id,errors)){
            return errors;
        }
        return errors;
    }


    private boolean vldExist(String id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        ChatGroup entity = chatGroupMng.findById(Integer.parseInt(id));
        if (errors.ifNotExist(entity, ChatGroup.class, id)) {
            return true;
        }
        return false;
    }
}
