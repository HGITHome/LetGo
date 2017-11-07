package com.dgut.main.entity.base;

import java.io.Serializable;
import java.util.Set;

/**
 * role base类
 * Created by PUNK on 2017/1/14.
 */
public abstract class BaseRole implements Serializable {

    private int hashCode = Integer.MIN_VALUE;

    //primary key
    private Integer id; //编号

    //field
    private String roleName; //角色名
    private Integer priority; //优先级
    private Boolean isSuper; //是否是超级管理员

    //collections
    private Set<String> permissions; //权限集合

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(Boolean aSuper) {
        isSuper = aSuper;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public BaseRole() {
    }

    public BaseRole(Integer id, String roleName, Integer priority, Boolean isSuper, Set<String> permissions) {
        this.id = id;
        this.roleName = roleName;
        this.priority = priority;
        this.isSuper = isSuper;
        this.permissions = permissions;
    }

    public int hashCode(){
        if(Integer.MIN_VALUE == this.hashCode){
            if(null == this.getId()){
                return super.hashCode();
            }
            else{
                String hashStr = this.getClass().getName()+":"+this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }




}
