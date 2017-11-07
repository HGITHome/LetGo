package com.dgut.main.action.main;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.main.entity.Accusation;
import com.dgut.main.manager.AccusationMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.WebErrors;
import org.apache.commons.lang.StringUtils;
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

/**
 * Created by PUNK on 2017/3/25.
 */
@Controller
@RequestMapping("accusation")
public class LeAccusationAct {

    private Logger logger = LoggerFactory.getLogger(LeAccusationAct.class);

    @Autowired
    private AccusationMng accusationMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;




    @RequestMapping("v_list.do")
    public String getAccusationList(String queryReporter, String queryRespondent, String queryStatus,Integer queryType,String pageNo, HttpServletRequest request, HttpServletResponse response, ModelMap model){
        Pagination pagination  = accusationMng.getAccusationList(queryReporter,queryRespondent,queryType,queryStatus,cpn(pageNo), CookieUtils.getPageSize(request));

        Pagination category=accusationMng.getCategory(null,1,Integer.MAX_VALUE);

        model.put("category",category.getList());
        model.put("pagination",pagination);
        model.put("pageNo",pageNo);
        model.put("queryReporter",queryReporter);
        model.put("queryRespondent",queryRespondent);
        model.put("queryType",queryType);
        model.put("queryStatus",queryStatus);
        return "accusation/list";
    }

    @RequestMapping("o_info.do")
    public String getAccusationInfo(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        WebErrors errors =validateInfo(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        Accusation accusation = accusationMng.findAccusationById(Integer.parseInt(id));
        model.put("accusation",accusation);
        return "accusation/info";
    }

    private WebErrors validateInfo(String id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(id)){
            errors.addErrorCode("error.required","id");
            return errors;
        }
        Accusation bean = accusationMng.findAccusationById(Integer.parseInt(id));
        if(bean==null){
            errors.addErrorCode("error.notExist");
            return  errors;
        }
        return errors;
    }


    @RequestMapping("o_update.do")
    public String update(String id,String reply,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        WebErrors errors = validateUpdate(id, reply,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        Accusation bean = accusationMng.update(id,reply);
        adminLogMng.operating(request,"cms.accusation.reply","id="+bean.getId());
        return "redirect:v_list.do";


    }

    private WebErrors validateUpdate(String id, String reply, HttpServletRequest request) {
        WebErrors errors =validateInfo(id,request);
        if(errors.hasErrors()){
            return  errors;
        }
        if(StringUtils.isBlank(reply)){
            errors.addErrorCode("error.required","处理结果");
            return  errors;
        }
        return errors;
    }

}
