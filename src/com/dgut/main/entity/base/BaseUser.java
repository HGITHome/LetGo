package com.dgut.main.entity.base;

import java.io.Serializable;
import java.util.Date;

/** admin base类
 * Created by PUNK on 2017/1/14.
 */
public abstract class BaseUser implements Serializable {

    private int hashCode = Integer.MIN_VALUE;

    //primary key
    private Integer id; //编号

    //field
    //基本信息
    private String username;    //用户名
    private String password;    //密码

    private Boolean gender ;    //性别
    private Boolean disabled; //账户是否可用

    //系统记录
    private Date registerTime;  //注册时间
    private String registerIP;  //注册IP
    private Date lastLoginTime; //上一次登陆时间
    private String lastLoginIP; //上一次登陆的IP
    private Integer loginCount; //登陆次数

    private Integer errorCount; //登陆失败次数
    private Date errorTime;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterIP() {
        return registerIP;
    }

    public void setRegisterIP(String registerIP) {
        this.registerIP = registerIP;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }



    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    public BaseUser() {
    }

    public BaseUser(Integer id, String username, String password, Boolean gender, Boolean disabled, Date registerTime, String registerIP, Date lastLoginTime, String lastLoginIP, Integer loginCount, Integer errorCount, Date errorTime) {
        this.id = id;
        this.username = username;
        this.password = password;

        this.gender = gender;
        this.disabled = disabled;
        this.registerTime = registerTime;
        this.registerIP = registerIP;
        this.lastLoginTime = lastLoginTime;
        this.lastLoginIP = lastLoginIP;
        this.loginCount = loginCount;

        this.errorCount = errorCount;
        this.errorTime = errorTime;
    }

    @Override
    public int hashCode() {
       if (Integer.MIN_VALUE == this.hashCode){
           if(null == this.getId()){
               return super.hashCode();
           }
           else{
               String hashStr = this.getClass().getName()+":"+this.getId();
               this.hashCode = hashStr.hashCode();
           }
       }
        return  this.hashCode;
    }
}
