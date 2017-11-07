package com.dgut.main.member.entity;

import com.dgut.main.entity.base.BaseUserLog;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/24.
 */
public class MemberLog extends BaseUserLog {

    Member member;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


    public MemberLog(){}

    public MemberLog(Integer id, Integer category, Date time, String ip, String url, String title, String content, Boolean disabled, Member member) {
        super(id, category, time, ip, url, title, content, disabled);
        this.member = member;
    }

    public MemberLog(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return  false;
        }
        if(!(obj instanceof  MemberLog)){
            return false;
        }
        else{
            MemberLog memberLog = (MemberLog) obj;
            if(null == this.getId() || null ==memberLog.getId()){
                return false;
            }
            else{
                return  this.getId().equals(memberLog.getId());
            }
        }
    }
}
