package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友关系
 * Created by PUNK on 2017/1/25.
 */
public class BaseFriendship implements Serializable{

    //primary key
    private Integer id; //编号

    //fields
    private Member owner; //拥有者
    private Member friend; //好友
    protected Friendship_status  friendship_status; //好友状态
    private Date registerTime; //成为好友的时间



    private String alias;//好友备注
    private String chinese;//备注拼音

    public enum Friendship_status{
        //正常
        IN_NORMAL,
        //黑名单
        IN_BLACKLIST,
        //删除好友
        DELETE
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Member getFriend() {
        return friend;
    }

    public void setFriend(Member friend) {
        this.friend = friend;
    }

    public Friendship_status getFriendship_status() {
        return friendship_status;
    }

    public void setFriendship_status(Friendship_status friendship_status) {
        this.friendship_status = friendship_status;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date register_time) {
        this.registerTime = register_time;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public BaseFriendship() {
    }

    public BaseFriendship(Integer id, Member owner, Member friend, Friendship_status friendship_status, Date register_time, String alias, String chinese) {
        this.id = id;
        this.owner = owner;
        this.friend = friend;
        this.friendship_status = friendship_status;
        this.registerTime = register_time;
        this.alias = alias;
        this.chinese = chinese;
    }
}
