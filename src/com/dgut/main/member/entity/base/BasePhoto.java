package com.dgut.main.member.entity.base;

import java.io.Serializable;

/**
 * 图片类
 * @author zw
 *
 */
public class BasePhoto implements Serializable{
	
	//primary key
	private Integer id;
	
	//fields
	private String photo;
	private String mini_photo;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getMini_photo() {
		return mini_photo;
	}
	public void setMini_photo(String mini_photo) {
		this.mini_photo = mini_photo;
	}
	public BasePhoto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BasePhoto(Integer id, String photo, String mini_photo) {
		super();
		this.id = id;
		this.photo = photo;
		this.mini_photo = mini_photo;
	}
	
	
	
	
	
	
	

}
