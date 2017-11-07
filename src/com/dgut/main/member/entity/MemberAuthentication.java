package com.dgut.main.member.entity;

import com.dgut.main.entity.base.BaseAuthentication;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/22.
 */
public class MemberAuthentication extends BaseAuthentication {
    public MemberAuthentication(String id, Integer uid, Date loginTime, String loginIP, Date updateTime) {
        super(id, uid, loginTime, loginIP, updateTime);
    }

    public MemberAuthentication() {
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj){
            return false;
        }
        if(!(obj instanceof MemberAuthentication)){
            return false;
        }
        else{
            MemberAuthentication authentication = (MemberAuthentication) obj;
            if(null == this.getId() ||null == authentication.getId()){
                return false;
            }
            else{
                return this.getId().equals(authentication.getId());
            }
        }
    }
}
