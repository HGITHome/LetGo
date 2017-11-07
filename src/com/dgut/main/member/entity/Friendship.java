package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseFriendship;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/27.
 */
public class Friendship extends BaseFriendship {



    public Friendship() {
    }

    public Friendship(Integer id, Member owner, Member friend, Friendship_status friendship_status, Date register_time, String alias, String chinese) {
        super(id, owner, friend, friendship_status, register_time, alias, chinese);
    }

}
