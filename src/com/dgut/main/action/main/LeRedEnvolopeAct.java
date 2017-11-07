package com.dgut.main.action.main;


import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;

import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.manager.RedEnvolopeMng;
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
 * Created by PUNK on 2017/4/9.
 */
@Controller
@RequestMapping("redEnvolope")
public class LeRedEnvolopeAct {
    private static final Logger log = LoggerFactory.getLogger(LeRedEnvolopeAct.class);

    @Autowired
    private RedEnvolopeMng redEnvolopeMng;

    @RequestMapping("v_list.do")
    public String getRevolopes(String queryUsername,String queryType,String queryIsPublic, String year,String pageNo, HttpServletRequest request, HttpServletResponse response,ModelMap model){

        Pagination pagination = redEnvolopeMng.getRedEnvolopes(null,queryUsername,queryType,null,year,null,queryIsPublic,cpn(pageNo), CookieUtils.getPageSize(request));

        model.put("queryUsername",queryUsername);
        model.put("pagination",pagination);
        model.put("pageNo",cpn(pageNo));
        model.put("queryType",queryType);
        model.put("queryIsPublic",queryIsPublic);
        return "member/redEnvolope/list";
    }

    @RequestMapping("o_info.do")
    public String getRedEnvolopeInfo(String id,HttpServletRequest request,ModelMap model){
        WebErrors errors = validateId(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        RedEnvolope bean = redEnvolopeMng.findById(Integer.parseInt(id));
        model.put("redEnvolope",bean);
        return "member/redEnvolope/info";
    }

    private WebErrors validateId(String id,HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(id)){
            errors.addErrorCode("error.required","id");
            return errors;
        }
        RedEnvolope bean = redEnvolopeMng.findById(Integer.parseInt(id));
        if(bean==null){
            errors.addErrorCode("error.notExist","该红包实体");
        }
        return errors;
    }
}
