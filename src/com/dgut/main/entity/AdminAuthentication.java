package com.dgut.main.entity;

import com.dgut.main.entity.base.BaseAuthentication;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by PUNK on 2017/1/15.
 */
public class AdminAuthentication extends BaseAuthentication {
    public AdminAuthentication(String id, Integer uid, Date loginTime, String loginIP, Date updateTime) {
        super(id, uid,loginTime, loginIP, updateTime);
    }

    public AdminAuthentication() {
    }



    @Override
    public boolean equals(Object obj) {
        if(null == obj){
            return false;
        }
        if(!(obj instanceof AdminAuthentication)){
            return false;
        }
        else{
            AdminAuthentication authentication = (AdminAuthentication) obj;
            if(null == this.getId() ||null == authentication.getId()){
                return false;
            }
            else{
                return this.getId().equals(authentication.getId());
            }
        }
    }
}
