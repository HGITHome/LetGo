package com.dgut.main.member.entity;

import java.util.Date;

import com.dgut.main.member.entity.base.BaseTalkMessage;

/**
 * 朋友圈评论
 * @author zw
 *
 */
public class TalkComment extends BaseTalkMessage{
	
	
	//评论回复的对象
	private Member receiver;
	//内容
	private String content;
	
	public Member getReceiver() {
		return receiver;
	}
	public void setReceiver(Member receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public TalkComment(Integer id, Member member, Date tap_time, Talk talk,
			Member receiver, String content) {
		super(id, member, tap_time, talk);
		this.receiver = receiver;
		this.content = content;
	}
	public TalkComment() {
		super();
		// TODO Auto-generated constructor stub
	}


}
