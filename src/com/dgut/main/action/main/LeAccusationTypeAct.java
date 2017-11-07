package com.dgut.main.action.main;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.entity.AccusationCategory;
import com.dgut.main.entity.AccusationType;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * Created by PUNK on 2017/3/23.
 */
@Controller
@RequestMapping("accusation/category/type")
public class LeAccusationTypeAct {

    @Autowired
    private AccusationMng accusationMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    private Logger log = LoggerFactory.getLogger(LeAccusationTypeAct.class);


    @RequestMapping("v_list.do")
    public String getTypeList(String categoryId,String pageNo,String typeName,HttpServletRequest request, HttpServletResponse response,ModelMap model){

        Pagination pagination = accusationMng.getTypeList(categoryId,typeName,cpn(pageNo), CookieUtils.getPageSize(request));
        AccusationCategory category = accusationMng.findCategoryById(Integer.parseInt(categoryId));
        model.put("category",category);
        model.put("typeName",typeName);
        model.put("pagination",pagination);
        model.put("pageNo",pageNo);
        return "accusation/category/type/list";
    }

    @RequestMapping("o_add.do")
    public String add(String categoryId,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        WebErrors errors = validateAdd(categoryId,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        model.put("categoryId",categoryId);
        return "accusation/category/type/add";
    }

    private WebErrors validateAdd(String categoryId, HttpServletRequest request) {
        WebErrors errors =WebErrors.create(request);
        if(StringUtils.isBlank(categoryId)){
            errors.addErrorCode("error.required",categoryId);
        }
        return errors;
    }

    @RequestMapping("/v_checkType.do")
    public void checkTypeName(String categoryId,String name, HttpServletRequest request,
                                  HttpServletResponse response) {

        AccusationType type = accusationMng.findTypeByName(categoryId,name);
        boolean pass = (type==null);
        ResponseUtils.renderJson(response, pass ? "true" : "false");
    }

    @RequestMapping("o_save.do")
    public String save(String categoryId,AccusationType type,HttpServletRequest request,HttpServletResponse response,ModelMap model){

        WebErrors erorrs =validateSave(categoryId,type,request);
        if(erorrs.hasErrors()){
            return erorrs.showErrorPage(model);
        }

        accusationMng.saveType(categoryId,type);
        adminLogMng.operating(request,"cms.accusation.type.add","子类名为"+type.getName());
        return "redirect:v_list.do?categoryId="+categoryId;
    }

    private WebErrors validateSave(String categoryId, AccusationType type, HttpServletRequest request) {
        WebErrors errors =WebErrors.create(request);
        if(StringUtils.isBlank(categoryId)){
            errors.addErrorCode("error.required","大类id");
            return errors;
        }
        if(StringUtils.isBlank(type.getName())){
            errors.addErrorCode("error.required","子类名称");
            return  errors;
        }
        else if(type.getPriority()==null){
            errors.addErrorCode("error.required","子类优先级");
        }
        return errors;
    }

    @RequestMapping("v_edit.do")
    public String edit(String id,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        WebErrors errors = validateEdit(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        AccusationType type = accusationMng.findTypeById(Integer.parseInt(id));
        model.put("type",type);
        return "accusation/category/type/edit";
    }

    private WebErrors validateEdit(String id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(id)){
            errors.addErrorCode("error.required","子类id");
            return errors;
        }
        AccusationType type = accusationMng.findTypeById(Integer.parseInt(id));
        if(type == null){
            errors.addErrorCode("error.notExist","子类");
        }
        return errors;
    }

    @RequestMapping("o_update.do")
    public String update(String categoryId,AccusationType type,HttpServletRequest request,ModelMap model){
        WebErrors errors = validateUpdate(type,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        type = accusationMng.updateType(type);

        adminLogMng.operating(request,"cms.accusation.type.edit","子类名为"+type.getName()+",优先级为"+type.getPriority());
        return "redirect:v_list.do?categoryId="+categoryId;
    }

    private WebErrors validateUpdate(AccusationType type, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(type.getName())){
            errors.addErrorCode("error.required","子类名称");
            return errors;
        }
        if(type.getPriority()==null){
            errors.addErrorCode("error.notExist","子类优先级");

        }
        return errors;
    }

    @RequestMapping("o_delete.do")
    public String delete(String []ids,String categoryId,HttpServletRequest request,ModelMap model){
        WebErrors errors = validateDelete(ids,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        for(String s:ids){
            AccusationType type =  accusationMng.deleteType(s);
            adminLogMng.operating(request,"cms.accusation.type.delete","子类名为"+type.getName());
        }
        return "redirect:v_list.do?categoryId="+categoryId;
    }

    private WebErrors validateDelete(String[] ids, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(ids == null){
            errors.addErrorCode("error.required","id");
        }
        return errors;
    }
}
