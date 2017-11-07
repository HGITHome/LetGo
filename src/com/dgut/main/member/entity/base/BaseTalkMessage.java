package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Talk;

import java.io.Serializable;
import java.util.Date;


public class BaseTalkMessage implements Serializable{
	
	//primary key
	private Integer id; // 编号
	
	//field
	//发布者
	private Member publisher;
	//发布的时间
	private Date message_time;
	//对应的朋友圈
	private Talk talk;
	
	//是否可用
	private Boolean disabled;
	
	
	
	
	
	public Boolean getDisabled() {
		return disabled;
	}
	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Member getPublisher() {
		return publisher;
	}
	public void setPublisher(Member publisher) {
		this.publisher = publisher;
	}
	public Date getMessage_time() {
		return message_time;
	}
	public void setMessage_time(Date message_time) {
		this.message_time = message_time;
	}
	public Talk getTalk() {
		return talk;
	}
	public void setTalk(Talk talk) {
		this.talk = talk;
	}
	public BaseTalkMessage(Integer id, Member member, Date tap_time, Talk talk) {
		super();
		this.id = id;
		this.publisher = member;
		this.message_time = tap_time;
		this.talk = talk;
	}
	public BaseTalkMessage() {
		super();
		// TODO Auto-generated constructor stub
	}


}
