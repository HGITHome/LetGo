package com.dgut.main.action.main;

import com.dgut.common.web.ResponseUtils;
import com.dgut.main.entity.Admin;
import com.dgut.main.manager.AdminMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.CmsUtils;
import com.dgut.main.web.FrontUtils;
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

/**
 * 个人资料controller
 * Created by PUNK on 2017/1/17.
 */

@Controller
@RequestMapping("/personal/")
public class LePersonAct {

    private static final Logger log = LoggerFactory
            .getLogger(LePersonAct.class);

    @RequestMapping("/v_profile.do")
    public String profileEdit(HttpServletRequest request, ModelMap model) {
        FrontUtils.adminData(request, model);

        return "personal/profile";
    }

    @RequestMapping("/o_profile.do")
    public String profileUpdate(String origPwd, String newPwd, String realname,
                                HttpServletRequest request, ModelMap model) {
        Admin admin = CmsUtils.getAdmin(request);
        WebErrors errors = validatePasswordSubmit(admin.getId(), origPwd,
                newPwd,  realname, request);
        if (errors.hasErrors()) {
            model.addAttribute("errors", errors.getErrors());
            return profileEdit(request, model);
        }
        adminMng.updatePwdRealName(admin.getId(), newPwd,realname);
        model.addAttribute("message", "global.success");

        adminLogMng.operating(request, "cms.admin.update", "修改密码");

        return profileEdit(request, model);
    }

    /**
     * 验证密码是否正确
     *
     * @param origPwd
     *            原密码
     * @param request
     * @param response
     */
    @RequestMapping("/v_checkPwd.do")
    public void checkPwd(String origPwd, HttpServletRequest request,
                         HttpServletResponse response) {
        Admin admin = CmsUtils.getAdmin(request);
        boolean pass = adminMng.isPasswordValid(admin.getId(), origPwd);

        ResponseUtils.renderJson(response, pass ? "true" : "false");
    }

    private WebErrors validatePasswordSubmit(Integer id, String origPwd,
                                             String newPwd, String realname,
                                             HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (errors.ifBlank(origPwd, "origPwd", 32)) {
            return errors;
        }
        if (errors.ifMaxLength(newPwd, "newPwd", 32)) {
            return errors;
        }
        if (errors.ifMaxLength(realname, "realname", 100)) {
            return errors;
        }
        if (!adminMng.isPasswordValid(id, origPwd)) {
            errors.addErrorString("密码错误");
            return errors;
        }
        return errors;
    }

    @Autowired
    private AdminMng adminMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

}
