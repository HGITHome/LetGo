package com.dgut.main.entity;

import com.dgut.main.entity.base.BaseRole;

import java.util.Set;

/**
 * Created by PUNK on 2017/1/14.
 */
public class Role extends BaseRole {

    public Role() {
    }

    public Role(Integer id, String roleName, Integer priority, Boolean isSuper, Set<String> permissions) {
        super(id, roleName, priority, isSuper, permissions);
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj){
            return false;
        }
        if(!(obj instanceof Role)){
            return false;
        }
        else {
            Role role = (Role) obj;
            if(null == this.getId() || null== role.getId()){
                return false;
            }
            else{
                return (this.getId().equals(role.getId()));
            }
        }

    }
}
