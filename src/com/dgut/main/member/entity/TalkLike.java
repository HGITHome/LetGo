package com.dgut.main.member.entity;

import java.util.Date;

import com.dgut.main.member.entity.base.BaseTalkMessage;

/**
 * 点赞类
 */
public class TalkLike extends BaseTalkMessage{

	public TalkLike() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TalkLike(Integer id, Member member, Date tap_time, Talk talk) {
		super(id, member, tap_time, talk);
		// TODO Auto-generated constructor stub
	}


	
	

}
