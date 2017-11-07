package com.dgut.main.action.main;

import com.dgut.common.page.Pagination;
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.RequestUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.main.entity.Admin;
import com.dgut.main.entity.Role;
import com.dgut.main.manager.AdminMng;
import com.dgut.main.manager.RoleMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.CmsUtils;
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
import java.util.List;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * 全站管理员action
 * Created by PUNK on 2017/1/18.
 */
@RequestMapping("/admin_global/")
@Controller
public class LeAdminAct {

    private static final Logger log = LoggerFactory.getLogger(LeAdminAct.class);

    /**
     * 遍历全部管理员信息
     * @param queryUsername  用户名
     * @param queryRoleId    角色id 
     * @param queryDisabled  是否可用
     * @param isSuper        是否是超级管理员
     * @param pageNo         页号
     * @param request        页码
     * @param model       
     * @return
     */
    @RequestMapping("/v_list.do")
    public String list(String queryUsername, Integer queryRoleId,
                       Boolean queryDisabled, Boolean isSuper,String pageNo,
                       HttpServletRequest request, ModelMap model) {

        Pagination pagination = adminMng.getPage(queryUsername,queryRoleId,
                queryDisabled, isSuper,
                cpn(pageNo), CookieUtils.getPageSize(request));
        Admin admin = CmsUtils.getAdmin(request);
        List<Role> roleList = roleMng.getList();
        model.addAttribute("pagination", pagination);
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryDisabled", queryDisabled);
        model.addAttribute("queryRoleId", queryRoleId);
        model.addAttribute("currUser",admin.getId());
        return "admin/global/list";
    }


    @RequestMapping("/v_add.do")
    public String add(HttpServletRequest request, ModelMap model) {
        Admin currAdmin = CmsUtils.getAdmin(request);
        List<Role> roleList = roleMng.getList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("currRank", currAdmin.getRank());
        return "admin/global/add";
    }

    @RequestMapping("/o_save.do")
    public String save(Admin bean, String username,
                       String password, Integer rank, Boolean gender, String realname,
                       Integer roleId, HttpServletRequest request,
                       ModelMap model) {
        WebErrors errors = validateSave(bean, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        String ip = RequestUtils.getIpAddr(request);
        bean = adminMng.save(username, password, ip, rank,gender,realname,roleId);
        log.info("save CmsAdmin id={}", bean.getId());
        adminLogMng.operating(request, "cms.admin.add", "id=" + bean.getId()
                + ";username=" + bean.getUsername());
        return "redirect:v_list.do";
    }

    /**
     * 校验保存参数
     * @param bean
     * @param request
     * @return
     */
    private WebErrors validateSave(Admin bean, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        //// TODO: 2017/1/18
        Admin admin = CmsUtils.getAdmin(request);
        if(StringUtils.isBlank(bean.getUsername())){
            errors.addErrorCode("error.required","username");
            return errors;
        }

        if(StringUtils.isBlank(bean.getRealname())){
            errors.addErrorCode("error.required","realname");
            return errors;
        }

        if(StringUtils.isBlank(bean.getPassword())){
            errors.addErrorCode("error.required","password");
            return errors;
        }

        if(bean.getRank() ==null){
            errors.addErrorCode("error.required","rank");
            return errors;
        }
        else{
            if(bean.getRank()<0 || bean.getRank()>admin.getRank()){
                errors.addErrorCode("error.invalid","rank");
                return errors;
            }
        }
        return errors;
    }

    /**
     * 检查是否重名
     * @param username
     * @param response
     */
    @RequestMapping(value = "/v_check_username.do")
    public void checkUsername(String username, HttpServletResponse response) {
        String pass;
        if (StringUtils.isBlank(username)) {
            pass = "false";
        } else {
            Admin admin = adminMng.findByUsername(username);
            pass = admin==null ? "true" : "false";
        }
        ResponseUtils.renderJson(response, pass);
    }



    /**
     * 用户信息编辑
     * @param id
     * @param queryDisabled
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/v_edit.do")
    public String edit(Integer id,  Boolean queryDisabled,
                       HttpServletRequest request, ModelMap model) {
        String queryUsername = RequestUtils.getQueryParam(request,
                "queryUsername");

        Admin currAdmin = CmsUtils.getAdmin(request);
        String rank = RequestUtils.getQueryParam(request,"rank");
        Integer userRank = StringUtils.isBlank(rank) ? 0 :Integer.parseInt(rank);
        WebErrors errors = validateEdit(id,userRank, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        Admin admin = adminMng.findById(id);

        List<Role> roleList = roleMng.getList();

        model.addAttribute("cmsAdmin", admin);
        model.addAttribute("roleList", roleList);
        model.addAttribute("currRank", currAdmin.getRank());
        model.addAttribute("currId", currAdmin.getId());

        model.addAttribute("queryUsername", queryUsername);
        model.addAttribute("queryDisabled", queryDisabled);
        return "admin/global/edit";
    }

    private WebErrors validateEdit(Integer id, Integer rank,HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }
        // 检查管理员的等级是否有权限操作
        if (vldParams(id,rank, request, errors)) {
            return errors;
        }
        return errors;
    }


    @RequestMapping("/o_update.do")
    public String update(Admin bean, Integer roleId, String queryUsername, Integer queryRoleId, Boolean queryDisabled, String pageNo, HttpServletRequest request,
                         ModelMap model) {
        WebErrors errors = validateUpdate(bean,request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        bean = adminMng.updateAdmin(bean,roleId);
        log.info("update CmsAdmin id={}.", bean.getId());

        adminLogMng.operating(request, "cms.admin.update", "id=" + bean.getId()
                + ";username=" + bean.getUsername());
        return list(queryUsername,queryRoleId,   queryDisabled,null,
                pageNo, request, model);
    }

    private WebErrors validateUpdate(Admin bean, HttpServletRequest request) {
       WebErrors errors = WebErrors.create(request);
        if(vldExist(bean.getId(), errors)){
            return errors;
        }
        //防止注入修改管理员名字
        Admin admin = adminMng.findById(bean.getId());
        if(!admin.getUsername().equals(bean.getUsername())){
            errors.addErrorCode("error.unequal","username");
        }

        vldParams(bean.getId(),bean.getRank(),request,errors);
        return errors;
    }

    private boolean vldExist(Integer id, WebErrors errors) {
        if (errors.ifNull(id, "id")) {
            return true;
        }
        Admin entity = adminMng.findById(id);
        if (errors.ifNotExist(entity, Admin.class, id)) {
            return true;
        }
        return false;
    }

    private boolean vldParams(Integer id,Integer rank, HttpServletRequest request,
                              WebErrors errors) {
        Admin admin = CmsUtils.getAdmin(request);
        Admin entity = adminMng.findById(id);
        if(!admin.isSuper()) {
            //修改的用户等级大于当前登录用户，操作没无权限
            if (entity.getRank() > admin.getRank()) {
                errors.addErrorCode("error.noPermission", Admin.class, id);
                return true;
            }
            //提升等级大于当前登录用户
            if (rank > admin.getRank()) {
                errors.addErrorCode("error.noPermissionToRaiseRank", entity.getUsername());
                return true;
            }
        }

        return false;
    }


    @Autowired
    private AdminMng adminMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    @Autowired
    private RoleMng roleMng;
}
