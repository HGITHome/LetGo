package com.dgut.main.entity;

import com.dgut.main.entity.base.BaseUser;

import java.util.Date;

/**
 * admin类
 * Created by PUNK on 2017/1/14.
 */
public class Admin extends BaseUser {

    //addtional field

    private Role role;

    private String realname;    //真实姓名

    private Integer rank;       //优先级

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isSuper(){
        Role role = getRole();
        if(null == role){
            return false;
        }
        if(role.getIsSuper()){
            return true;
        }
        else{
            return false;
        }


    }

    public Admin() {
    }

    public Admin(Integer id, String username, String passowrd, String realname, Boolean gender, Boolean isDisabled, Date registerTime, String registerIP, Date lastLoginTime, String lastLoginIP, Integer loginCount, Integer rank, Integer errorCount, Role role, Date errorTime) {
        super(id, username, passowrd, gender, isDisabled, registerTime, registerIP, lastLoginTime, lastLoginIP, loginCount, errorCount,errorTime);
        this.role = role;
        this.realname =  realname;
        this.rank = rank;
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj){
            return false;
        }
        if(!(obj instanceof Admin)){
            return false;
        }
        else{
            Admin admin = (Admin) obj;
            if(null == this.getId() || null==admin.getId()){
                return false;
            }
            else{
                return this.getId().equals(admin.getId());
            }
        }
    }
}
