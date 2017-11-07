package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseApplyFriend;

/**
 * Created by PUNK on 2017/1/25.
 */
public class ApplyFriend extends BaseApplyFriend {


    /*public ApplyFriend(Integer id, Member publisher, Member receiver, Integer handle_flag, String apply_reason, Boolean isRead) {
        super(id, publisher, receiver, handle_flag, apply_reason, isRead);
    }*/

    public ApplyFriend(Integer id, Member publisher, Member receiver, ApplyFlag handle_flag, String apply_reason, Boolean isRead) {
        super(id, publisher, receiver, handle_flag, apply_reason, isRead);
    }

    public ApplyFriend() {
    }
}
