package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseChatGroup;

/**
 * Created by PUNK on 2017/2/22.
 */
public class ChatGroup extends BaseChatGroup {

    private String easemob_id ;

    public String getEasemob_id() {
        return easemob_id;
    }

    public void setEasemob_id(String easemob_id) {
        this.easemob_id = easemob_id;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof ChatGroup)) return false;
        else {
            ChatGroup group = (ChatGroup) obj;
            if (null == this.getId() || null == group.getId()) return false;
            else {
                return (this.getId().equals(group.getId()));
            }
        }
    }
}
