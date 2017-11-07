package com.dgut.main.action.main;


import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.entity.Accusation;
import com.dgut.main.entity.AccusationCategory;
import com.dgut.main.entity.Admin;
import com.dgut.main.manager.AccusationMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.CmsUtils;
import com.dgut.main.web.WebErrors;
import org.apache.commons.lang.StringEscapeUtils;
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
 * Created by PUNK on 2017/3/21.
 */
@Controller
@RequestMapping("accusation/category")
public class LeAccusationCategoryAct {

    @Autowired
    private AccusationMng accusationMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    private Logger logger = LoggerFactory.getLogger(LeAccusationCategoryAct.class);

    @RequestMapping("v_list.do")
    public String getCatrgoryList(String queryName,String pageNo,HttpServletRequest request, HttpServletResponse response,ModelMap model){
        Pagination pagination = accusationMng.getCategory(queryName,cpn(pageNo), CookieUtils.getPageSize(request));
        model.put("pagination",pagination);
        model.put("queryName",queryName);
        model.put("pageNo",pageNo);
        return "accusation/category/list";
    }

    @RequestMapping("o_add.do")
    public String addCategory(HttpServletRequest request,HttpServletResponse response,ModelMap model){
        return "accusation/category/add";
    }

    @RequestMapping("/v_checkCategory.do")
    public void checkCategoryName(String name, HttpServletRequest request,
                         HttpServletResponse response) {

        AccusationCategory category = accusationMng.findCategoryByName(name);
        boolean pass = (category==null);
        ResponseUtils.renderJson(response, pass ? "true" : "false");
    }

    @RequestMapping("o_save.do")
    public String save( AccusationCategory bean,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        WebErrors errors = validateSave(bean,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        bean=accusationMng.addCategory(bean);
        adminLogMng.operating(request,"cms.accusation.category.add",bean.getName());
        return getCatrgoryList(null,"1",request,response,model);
    }

    private WebErrors validateSave(AccusationCategory bean, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(bean.getName())){
            errors.addErrorCode("error.required","大类名称");
            return errors;
        }
        if(bean.getPriority()==null){
            errors.addErrorCode("error.required","优先级");
        }
        return errors;
    }


    @RequestMapping("o_edit.do")
    public String edit(String id,HttpServletRequest request,ModelMap model){
        WebErrors errors = validateEdit(id,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        AccusationCategory category = accusationMng.findCategoryById(Integer.parseInt(id));
        model.put("category",category);
        return "accusation/category/edit";
    }

    @RequestMapping("o_update.do")
    public String update(AccusationCategory bean,HttpServletRequest request,ModelMap model){
        WebErrors errors = validateUpdate(bean,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        AccusationCategory category =  accusationMng.updateCategory(bean);
        adminLogMng.operating(request,"cms.accusation.category.edit","类名为"+bean.getName()+",优先级为"+bean.getPriority());
        return "redirect:v_list.do";
    }

    private WebErrors validateUpdate(AccusationCategory bean, HttpServletRequest request) {
        WebErrors errors =WebErrors.create(request);
        if(bean.getId()==null){
            errors.addErrorCode("error.required","id");
            return errors;
        }
        else if(StringUtils.isBlank(bean.getName())){
            errors.addErrorCode("error.required","类名");
            return errors;
        }
        else if(bean.getPriority()==null){
            errors.addErrorCode("error.required","优先级");
            return errors;
        }
        return errors;
    }

    private WebErrors validateEdit(String id,HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(id)){
            errors.addErrorCode("error.required","大类编号");
            return errors;
        }
        AccusationCategory category =  accusationMng.findCategoryById(Integer.parseInt(id));
        if(category==null){
            errors.addErrorCode("error.notExist","大类");
        }
        return errors;
    }

    @RequestMapping("o_delete.do")
    public String deleteCategory(String[] ids,HttpServletRequest request,HttpServletResponse response,ModelMap model){
        WebErrors errors = validateDelete(ids,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        for(String s: ids){
            AccusationCategory bean= accusationMng.deleteCategory(Integer.parseInt(s));
            adminLogMng.operating(request,"cms.accusation.category.delete","类名称为"+bean.getName());
        }

        return "redirect:v_list.do";

    }

    private WebErrors validateDelete(String[] id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(id==null){
            errors.addErrorCode("error.required","欲删除的举报类别");
            return errors;
        }
        return  errors;
    }


}
