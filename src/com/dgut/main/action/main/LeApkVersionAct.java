package com.dgut.main.action.main;

import com.dgut.common.page.Pagination;
import com.dgut.common.upload.FileRepository;
import com.dgut.common.web.CookieUtils;
import com.dgut.common.web.ResponseUtils;
import com.dgut.common.web.springmvc.RealPathResolver;
import com.dgut.main.entity.ApkVersion;
import com.dgut.main.entity.Role;
import com.dgut.main.manager.ApkVersionMng;
import com.dgut.main.manager.UserLogMng;
import com.dgut.main.web.WebErrors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import static com.dgut.common.page.SimplePage.cpn;

/**
 * Created by PUNK on 2017/2/11.
 */

@Controller
@RequestMapping("apk")
public class LeApkVersionAct {

    @Autowired
    private ApkVersionMng apkVersionMng;

    @Resource(name="adminLogMng")
    private UserLogMng adminLogMng;

    private static Logger log = LoggerFactory.getLogger(LeApkVersionAct.class);


    @RequestMapping("v_list.do")
    public String list( HttpServletRequest request,
                       HttpServletResponse response, ModelMap model) {
        String queryType = request.getParameter("queryEditType");
        String isValid = request.getParameter("queryStatus");
        String pageNo = request.getParameter("pageNo");

        Pagination pagination = apkVersionMng.getList(queryType,isValid,cpn(pageNo),
                CookieUtils.getPageSize(request));
        model.addAttribute("queryEditType", queryType);
        model.addAttribute("queryStatus", isValid);
        model.addAttribute("pageNo", cpn(pageNo));
        model.addAttribute("pageSize", CookieUtils.getPageSize(request));
        model.addAttribute("pagination", pagination);
        return "apk/list";

    }


    @RequestMapping("v_add.do")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        return "apk/add";
    }

    @RequestMapping("v_upload.do")
    public String upload(
            @RequestParam(value = "apk", required = true) MultipartFile apk,
          HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {

        String note = request.getParameter("note");
        String change_type = request.getParameter("change_type");
        String version = request.getParameter("version");
        String isValid = request.getParameter("isValid");

        WebErrors errors = validateSave(apk,version, change_type, note, isValid ,request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        String url = null;
        try {
            url = getApkUrl(apk, request);
        } catch (IOException e) {
            // TODO Auto-generated catch block

            log.error("apk上传出错");
            errors.addError("apk上传出错");
            return errors.showErrorPage(model);
        }

        ApkVersion bean=apkVersionMng.save(version,url, change_type,isValid, note.trim().replace("\r\n",";"));

        adminLogMng.operating(request, "cms.app.upload", "id=" + bean.getVersion_id()
                + ";link=" + bean.getUrl());

        return "redirect:v_list.do";
    }


    @RequestMapping("v_edit.do")
    public String edit(String id, HttpServletRequest request, ModelMap model) {
        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            errors.showErrorPage(model);
        }
        ApkVersion version = apkVersionMng.findById(id);
        model.addAttribute("apk", version);
        model.addAttribute("notes",version.getNote().split(";"));

        return "apk/edit";
    }

    private WebErrors validateEdit(String id, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }
        return errors;
    }

    private WebErrors validateSave(MultipartFile apk,String version, String change_type,
                                   String note, String isValid, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (apk == null || apk.isEmpty()) {
            errors.addError("apk不能为空");
        } else if (StringUtils.isBlank(change_type)) {
            errors.addError("版本修订类型不能为空");
        } else if (StringUtils.isBlank(note)) {
            errors.addError("修改内容不能为空");
        }else if(StringUtils.isBlank(isValid) ){
            errors.addError("开放设置不能为空");
        }else if (StringUtils.isBlank(version)) {
            errors.addError("版本号不能为空");
        }
        ApkVersion app = apkVersionMng.findById(version);
        if(app!=null){
            errors.addError("版本号不能重复");
        }
        return errors;
    }

    private String getApkUrl(MultipartFile apk, HttpServletRequest request)
            throws IOException {
        // 文件名
        String apk_name = apk.getOriginalFilename();
        // 后缀名
        String apk_ext = FilenameUtils.getExtension(apk_name).toLowerCase(
                Locale.ENGLISH);

        String url = fileRepository.storeByExt("/apk", apk_ext, apk);

        return url;
    }


    @RequestMapping("v_update.do")
    public String update(
            HttpServletRequest request,
            String pageNo, HttpServletResponse response, ModelMap model) {


        String isValid = request.getParameter("isValid");
        String id= request.getParameter("id");
        String change_type = request.getParameter("change_type");
        String note  = request.getParameter("note");

        WebErrors errors = validate(id, isValid, change_type, note,request);

        if(errors.hasErrors()){
            errors.showErrorPage(model);
        }


        ApkVersion version = apkVersionMng.findById(id);
        version.setIsValid(Boolean.valueOf(isValid));
        version.setPublish_time(new Date());
        version.setType(change_type);


        String []notes=note.trim().split("\r\n");
        StringBuffer sb=new StringBuffer();
        for(String s:notes){
            if(!s.endsWith(";")){
                sb.append(s+";");
            }
        }
        version.setNote(sb.toString());

        ApkVersion bean=apkVersionMng.updateVersion(version);


        adminLogMng.operating(request, "cms.app.update", "id=" + bean.getVersion_id()
                + ";url=" + bean.getUrl());




        return list(request, response, model);

    }

    private WebErrors validate(String id, String isValid, String type,
                                     String note,HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if (vldExist(id, errors)) {
            return errors;
        }

        else if(StringUtils.isBlank(isValid)){
            errors.addError("开放设置不能为空");
            return errors;
        }

        else if(StringUtils.isBlank(type)){
            errors.addError("修改类型不能为空");
            return errors;
        }

        else if(StringUtils.isBlank(note)){
            errors.addError("更新信息不能为空");
        }
        return errors;
    }

    private boolean vldExist(String id, WebErrors errors) {
        // TODO Auto-generated method stub
        if (errors.ifNull(id, "id")) {
            return true;
        }
        ApkVersion entity = apkVersionMng.findById(id);
        if (errors.ifNotExist(entity, ApkVersion.class, id)) {
            return true;
        }
        return false;
    }

    @RequestMapping("o_delete.do")
    public String delete(String id, String pageNo, HttpServletRequest request,
                         HttpServletResponse response, ModelMap model) {

        WebErrors errors = validateEdit(id, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }

        ApkVersion bean=apkVersionMng.deleteById(id);
        adminLogMng.operating(request, "cms.app.delete",
                "链接:" + bean.getUrl());

        model.addAttribute("message","global.success");
        return list( request, response, model);
    }



    /**
     * 检查是否重名
     * @param version
     * @param response
     */
    @RequestMapping(value = "v_check_version.do")
    public void checkRoleName(String version, HttpServletResponse response) {
        String pass;
        if (StringUtils.isBlank(version)) {
            pass = "false";
        } else {
            ApkVersion apk = apkVersionMng.findById(version);
            pass = apk==null ? "true" : "false";
        }
        ResponseUtils.renderJson(response, pass);
    }

    @Autowired
    private RealPathResolver realPathResolver;

    @Autowired
    private FileRepository fileRepository;
}
