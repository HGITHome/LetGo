package com.dgut.main.member.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by PUNK on 2017/3/16.
 */
public class GroupMember implements Serializable{

    private Integer id;
    private Member member;
//    private ChatGroup group;
    private ChatGroup chatGroup;
    private Date enterTime;
    private Boolean disabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

//    public ChatGroup getGroup() {
//        return group;
//    }

//    public void setGroup(ChatGroup group) {
//        this.group = group;
//    }


    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMember that = (GroupMember) o;

        if (!id.equals(that.id)) return false;
        if (member != null ? !member.equals(that.member) : that.member != null) return false;
        if (chatGroup != null ? !chatGroup.equals(that.chatGroup) : that.chatGroup != null) return false;
        return enterTime != null ? enterTime.equals(that.enterTime) : that.enterTime == null;

    }

    /*@Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (member != null ? member.hashCode() : 0);
        result = 31 * result + (member.group != null ? member.group.hashCode() : 0);
        result = 31 * result + (enterTime != null ? enterTime.hashCode() : 0);
        return result;
    }*/
}
