package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Photo;
import com.dgut.main.member.entity.TalkComment;
import com.dgut.main.member.entity.TalkLike;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public abstract  class BaseTalk implements Serializable {

	
	//prinary key
	private Integer id;  //编号
	//fields
	private Date publish_time;  // 发布时间
	private String content;		// 文字内容
	private String video_url;	// 视频链接
	
	private Boolean disabled;   // 是否展示
	
	//relations
	private Member publisher;   // 发布者
	
	//collections
	private List<Photo> photo_urls;  // 图片集合
//	private Set<String> photo_urls;
	

	private List<TalkLike> talkLikes; //点赞列表

	private List<TalkComment> talkComments; //评论列表
	
	
	
	
	
	
	
	
	
	
	

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Member getPublisher() {
		return publisher;
	}

	public void setPublisher(Member publisher) {
		this.publisher = publisher;
	}

	public List<TalkComment> getTalkComments() {
		return talkComments;
	}

	public void setTalkComments(List<TalkComment> talkComments) {
		this.talkComments = talkComments;
	}

	public List<TalkLike> getTalkLikes() {
		return talkLikes;
	}

	public void setTalkLikes(List<TalkLike> talkLikes) {
		this.talkLikes = talkLikes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(Date publish_time) {
		this.publish_time = publish_time;
	}

	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public List<Photo> getPhoto_urls() {
		return photo_urls;
	}

	public void setPhoto_urls(List<Photo> photo_urls) {
		this.photo_urls = photo_urls;
	}
	
	
	
	
	

	

	
//
//	public Set<String> getPhoto_urls() {
//		return photo_urls;
//	}
//
//	public void setPhoto_urls(Set<String> photo_urls) {
//		this.photo_urls = photo_urls;
//	}

	

	public BaseTalk() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BaseTalk (Integer id) {
		this.setId(id);
		
	}
	
	

	public BaseTalk(Integer talk_id, Date publish_time, String content,
			String video_url) {
		super();
		this.id = talk_id;
		this.publish_time = publish_time;
		this.content = content;
		this.video_url = video_url;
		
	}

	@Override
	public String toString() {
		return "BaseTalk{" +
				"id=" + id +
				", publish_time=" + publish_time +
				", content='" + content + '\'' +
				", video_url='" + video_url + '\'' +
				", disabled=" + disabled +
				", publisher=" + publisher +
				", photo_urls=" + photo_urls +
				", talkLikes=" + talkLikes +
				", talkComments=" + talkComments +
				'}';
	}
}
