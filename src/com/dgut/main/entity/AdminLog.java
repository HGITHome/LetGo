package com.dgut.main.entity;

import com.dgut.main.entity.base.BaseUserLog;

import java.util.Date;

/**
 * admin 日志类
 * Created by PUNK on 2017/1/18.
 */
public class AdminLog extends BaseUserLog {

    //relation
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public AdminLog(Integer id, Integer category, Date time, String ip, String url, String title, String content, Boolean disabled, Admin admin) {
        super(id, category, time, ip, url, title, content, disabled);
        this.admin = admin;
    }

    public AdminLog(Admin admin) {
        this.admin = admin;
    }

    public AdminLog(){

    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return  false;
        }
        if(!(obj instanceof  AdminLog)){
            return false;
        }
        else{
            AdminLog adminLog = (AdminLog) obj;
            if(null == this.getId() || null ==adminLog.getId()){
                return false;
            }
            else{
                return  this.getId().equals(adminLog.getId());
            }
        }
    }
}
