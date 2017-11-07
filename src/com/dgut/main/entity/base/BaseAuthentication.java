package com.dgut.main.entity.base;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 认证信息
 * Created by PUNK on 2017/1/15.
 */
public abstract class BaseAuthentication implements Serializable{

    private int hashCode = Integer.MIN_VALUE;

    //primary key
    private String id;

    //field
    private Integer uid;
//    private String username;
    private Date loginTime;
    private String loginIP;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

   /* public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }*/

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BaseAuthentication(String id, Integer uid, Date loginTime, String loginIP, Date updateTime) {
        this.id = id;
        this.uid = uid;
//        this.username = username;
        this.loginTime = loginTime;
        this.loginIP = loginIP;
        this.updateTime = updateTime;
    }

    public BaseAuthentication() {
    }

    public int hashCode(){
        if(Integer.MIN_VALUE == this.hashCode){
            if(null == this.getId()){
                return super.hashCode();
            }
            else{
                String hashStr = this.getClass().getName()+":"+this.getId();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public void init() {
        Date now = new Timestamp(System.currentTimeMillis());
        setLoginTime(now);
        setUpdateTime(now);
    }
}
