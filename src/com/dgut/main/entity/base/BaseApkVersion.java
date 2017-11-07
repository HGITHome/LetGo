package com.dgut.main.entity.base;

import java.io.Serializable;
import java.util.Date;

/**
 * app 版本控制
 * @author zw
 *
 */
public class BaseApkVersion implements Serializable{
	
	//	版本号
	private String version_id;
	
	//下载地址
	private String url;
	
	//更新内容
	private String note;
	
	//是否可用
	private Boolean isValid;
	
	//上传时间
	private Date publish_time;
	
	//修订类型
	private String type;
	
	

	

	public Date getPublish_time() {
		return publish_time;
	}



	public void setPublish_time(Date publish_time) {
		this.publish_time = publish_time;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public Boolean getIsValid() {
		return isValid;
	}
	
	

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}



	public void version(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getVersion_id() {
		return version_id;
	}

	public void setVersion_id(String version_id) {
		this.version_id = version_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BaseApkVersion() {
		super();
		// TODO Auto-generated constructor stub
	}



	public BaseApkVersion(String version_id, String url, String note,
						  Boolean isValid, Date publish_time, String type) {
		super();
		this.version_id = version_id;
		this.url = url;
		this.note = note;
		this.isValid = isValid;
		this.publish_time = publish_time;
		this.type = type;
	}

	
	

}
