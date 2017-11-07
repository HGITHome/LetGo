package com.dgut.main.action.main.assist;

import com.dgut.common.file.FileWrap;
import com.dgut.common.util.DateUtils;
import com.dgut.common.util.StrUtils;
import com.dgut.common.util.Zipper;
import com.dgut.common.web.ResponseUtils;
import com.dgut.common.web.springmvc.RealPathResolver;
import com.dgut.main.Constants;
import com.dgut.main.entity.assist.DBField;
import com.dgut.main.entity.assist.DBFile;
import com.dgut.main.manager.assist.DBFileMng;
import com.dgut.main.manager.assist.DBResourceMng;
import com.dgut.main.manager.assist.DataBackMng;
import com.dgut.main.web.WebErrors;
import org.apache.commons.lang.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import static javax.swing.text.html.HTML.Tag.BR;

/**
 * 数据备份的action
 * Created by PUNK on 2017/1/20.
 */

@Controller
@RequestMapping("/data/")
public class LeDBDataAct {

    private static String ABLEFOREIGN = "SET FOREIGN_KEY_CHECKS = 1;\r\n";
    private static String BRANCH = ";";
    private static String COMMA = ",";
    private static String DISABLEFOREIGN = "SET FOREIGN_KEY_CHECKS = 0;\r\n";
    private static String INSERT_INTO = " INSERT INTO ";
    private static final String INVALID_PARAM = "template.invalidParams";
    private static String LEFTBRACE = "(";
    private static String QUOTES = "'";
    private static String RIGHTBRACE = ")";
    private static String SLASH="/";
    public static String  DIMENSION = "\\";
    private static String SPACE = " ";
    private static String SPLIT = "`";
    private static String SUFFIX = "sql";
    private static String VALUES = "VALUES";

    private static String backup_table;
    private static Integer per;


    /**
     * 显示数据库的表名
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("v_list.do")
    public String list(ModelMap model, HttpServletRequest request,
                       HttpServletResponse response) {
        List<String> tables = null;
        try {
            tables = dataBackMng.listTables(dataBackMng.getDefaultCatalog());
        }
        catch (SQLException e) {
            model.addAttribute("msg", e.toString());
            return "common/error_message";
        }
        model.addAttribute("tables", tables);
        return "data/list";
    }


    /**
     * 显示表字段
     * @param tablename
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("v_listfields.do")
    public String listFields(String tablename, ModelMap model,
                            HttpServletRequest request, HttpServletResponse response) {
        WebErrors errors = validateListField(tablename,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        List<DBField> list = dataBackMng.listFields(tablename);
        model.addAttribute("list", list);
        return "data/fields";
    }

    /**
     * 数据库备份
     * @param tableNames
     * @param model
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping("o_backup.do")
    public String backup(String tableNames, ModelMap model,
                         HttpServletRequest request, HttpServletResponse response)
            throws IOException, InterruptedException {
        WebErrors errors =validateBackup(tableNames,request);
        if(errors.hasErrors()){
            return errors.showErrorPage(model);
        }
        String backpath = realPathResolver.get(Constants.BACKUP_PATH);
        File backDirectory = new File(backpath);
        if (!backDirectory.exists()) {
            backDirectory.mkdir();
        }
        DateUtils dateUtils = DateUtils.getDateInstance();
        String backFilePath = backpath + DIMENSION+ dateUtils.getNowString() + "."
                + SUFFIX;
        File file=new File(backFilePath);
        Thread thread =new DateBackupTableThread(file,tableNames.split(","));

        String fileName = backFilePath.substring(backFilePath.lastIndexOf("\\")+1);
        String filePath = backFilePath.substring(backFilePath.indexOf("W"));
        dbFileMng.saveFileByPath(filePath,fileName, true);
        thread.start();


//        adminLogMng.operating(request, "cms.db.backup", "文件名为"+file.getName());
        return "data/backupProgress";
    }

    private WebErrors validateBackup(String tableNames, HttpServletRequest request) {
        WebErrors errors = WebErrors.create(request);
        if(StringUtils.isBlank(tableNames)){
            errors.addErrorCode("error.required","tableName");
        }
        return errors;
    }

    /**
     * 进度条
     * @param request
     * @param response
     * @throws JSONException
     */
    @RequestMapping("o_backup_progress.do")
    public void getBackupProgress(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject json=new JSONObject();
        json.put("tablename", backup_table);
        json.put("per", per);
        ResponseUtils.renderJson(response, json.toString());
    }

    private class DateBackupTableThread extends Thread {
        private File file;
        private String[] tablenames;

        public DateBackupTableThread(File file, String[] tablenames) {
            super();
            this.file = file;
            this.tablenames = tablenames;
        }

        public void run() {
            FileOutputStream out;
            OutputStreamWriter writer = null;
            try {
                out = new FileOutputStream(file);
                writer = new OutputStreamWriter(out, "utf8");
                writer.write(Constants.ONESQL_PREFIX + DISABLEFOREIGN);
                for (int i = 0; i < tablenames.length; i++) {
                    backup_table = tablenames[i];
                    per = 100 / tablenames.length * (i + 1);
//					System.out.println("正在被备份表："+backup_table);
//					System.out.println("进度条:"+per);
                    backupTable(writer, tablenames[i]);
                }
                writer.write(Constants.ONESQL_PREFIX + ABLEFOREIGN);
                per = 100;
                backup_table = "";
                writer.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String backupTable(OutputStreamWriter writer, String tablename) throws IOException {
            writer.write(createOneTableSql(tablename));
            writer.flush();
            return tablename;
        }

        private String createOneTableSql(String tablename) {
            StringBuffer buffer = new StringBuffer();
            Object[] oneResult;
            buffer.append(Constants.ONESQL_PREFIX + "DROP TABLE IF EXISTS "
                    + tablename + BRANCH + BR);
            buffer.append(Constants.ONESQL_PREFIX
                    + dataBackMng.createTableDDL(tablename) + BRANCH + BR
                    + Constants.ONESQL_PREFIX);
            List<Object[]> results = dataBackMng.createTableData(tablename);
            for (int i = 0; i < results.size(); i++) {
                // one insert sql
                oneResult = results.get(i);
                buffer.append(createOneInsertSql(oneResult, tablename));
            }
            return buffer.toString();
        }

        private String createOneInsertSql(Object[] oneResult, String tablename) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(Constants.ONESQL_PREFIX + INSERT_INTO + SPLIT + tablename
                    + SPLIT + SPACE + VALUES + LEFTBRACE);
            for (int j = 0; j < oneResult.length; j++) {
                if (oneResult[j] != null) {
                    if (oneResult[j] instanceof Date) {
                        buffer.append(QUOTES + oneResult[j] + QUOTES);
                    } else if (oneResult[j] instanceof String) {
                        buffer.append(QUOTES
                                + StrUtils.replaceKeyString((String) oneResult[j])
                                + QUOTES);
                    } else if (oneResult[j] instanceof Boolean) {
                        if ((Boolean) oneResult[j]) {
                            buffer.append(1);
                        } else {
                            buffer.append(0);
                        }
                    } else {
                        buffer.append(oneResult[j]);
                    }
                } else {
                    buffer.append(oneResult[j]);
                }
                buffer.append(COMMA);
            }
            buffer = buffer.deleteCharAt(buffer.lastIndexOf(COMMA));
            buffer.append(RIGHTBRACE + BRANCH + BR);
            return buffer.toString();
        }
    }



    private WebErrors validateListField(String tableName,HttpServletRequest request) {
        WebErrors errors= WebErrors.create(request);
        if(StringUtils.isBlank(tableName)){
            errors.addErrorCode("error.required","tableName");
        }
        return errors;
    }

    @RequestMapping("v_listfiles.do")
    public String listBackUpFiles(ModelMap model, HttpServletRequest request,
                                  HttpServletResponse response) {
//		model.addAttribute("list",resourceMng.listFile(Constants.BACKUP_PATH, false));

        List<DBFile> list=dbFileMng.getList(true);
//		List<FileWrap> list = new ArrayList<FileWrap>(files.length);
        List<FileWrap> fileWrapList= new ArrayList<>(list.size());
//        ServletContext context=request.getSession().getServletContext();
        //解决数据库保存工作空间不存在的文件
        List<String> fileNames =new ArrayList<>();
        for (DBFile f : list) {
            File file=new File(realPathResolver.get(f.getFilePath()));
            if(file.length()==0){
                fileNames.add(file.getName());
            }
            else{
                fileWrapList.add(new FileWrap(file, realPathResolver.get("")));
            }
        }

        if(fileNames.size()!=0){
            dbFileMng.updateInvalidFiles(fileNames);
        }
        model.addAttribute("list",fileWrapList);
        return "data/files";
    }

    @RequestMapping("o_delete.do")
    public String delete(String root, String names,
                         HttpServletRequest request, ModelMap model,HttpServletResponse response) {
        WebErrors errors = validateDBFile(names, request);
        if (errors.hasErrors()) {
            return errors.showErrorPage(model);
        }
        resourceMng.delete(names.split(","));
        model.addAttribute("root", root);
        model.addAttribute("message", "global.success");
        return listBackUpFiles( model,request,response);
    }

    private WebErrors validateDBFile(String names, HttpServletRequest request) {

        WebErrors errors = WebErrors.create(request);
        if(names==null || names.split(",").length==0){

            errors.addErrorCode("error.required", "sql文件");

            return errors;
        }
//        errors.ifEmpty(names, "names");
        String[] files = names.split(",");
        for (String id : files) {

            //导出阻止非法获取其他目录文件
            if (!id.contains("sql")) {
                errors.addErrorCode(INVALID_PARAM);
                break;
            }
            if(!vldExist(id, errors)){
                break;
            }
        }


        return errors;
    }
    private boolean vldExist(String name, WebErrors errors) {
        if (errors.ifNull(name, "name")) {
            return false;
        }
       DBFile file = dbFileMng.findByFileName(name);
        if(file ==null){
            errors.addErrorCode("error.notExist",name);
            return false;
        }
        else{
            return true;
        }

    }

    @RequestMapping(value = "o_export.do")
    public String exportSubmit(String names,ModelMap model,HttpServletRequest request,HttpServletResponse response)
            throws UnsupportedEncodingException {

        WebErrors errors = validateDBFile(names,request);
        if(errors.hasErrors()){


            return errors.showErrorPage(model);
        }
        String[] files = names.split(",");
        String backName="back";
        if(files[0]!=null){
            backName=files[0].substring(files[0].indexOf(Constants.BACKUP_PATH)+Constants.BACKUP_PATH.length()+1);
//            backName = files[0];
        }
        List<Zipper.FileEntry> fileEntrys = new ArrayList<Zipper.FileEntry>();
        response.setContentType("application/x-download;charset=UTF-8");
        response.addHeader("Content-disposition", "filename="
                + backName+".zip");
        for(String filename:files){
            File file=new File(realPathResolver.get(dbFileMng.findByFileName(filename).getFilePath()));
            fileEntrys.add(new Zipper.FileEntry("", "", file));
        }
        try {
            // 模板一般都在windows下编辑，所以默认编码为GBK
            Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
            model.addAttribute("message", "global.success");
        } catch (IOException e) {
        }
        return listBackUpFiles(model,request,response);
    }

    /*private WebErrors validate(String names,HttpServletRequest request) {
        WebErrors errors =WebErrors.create(request);
        if(names==null || names.split(",").length==0){

                errors.addErrorCode("error.required", "tableName");

            return errors;
        }
        String[] files = names.split(",");
        for(String s:files) {
            if(!vldExist(names, errors)){
                break;
            }
        }
        *//*else {

            for(String name:files){
                //导出阻止非法获取其他目录文件
                if (!name.contains("/WEB-INF/backup/")) {
                    errors.addErrorCode(INVALID_PARAM);
                    break;
                }
            }
        }*//*
        return errors;
    }*/


    @Autowired
    private DataBackMng dataBackMng;

    @Autowired
    private RealPathResolver realPathResolver;

    @Autowired
    private DBFileMng dbFileMng;

    @Autowired
    private DBResourceMng resourceMng;
}
