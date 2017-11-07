package com.dgut.main.entity.base;



import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志基础类
 * Created by PUNK on 2017/1/18.
 */
public class BaseUserLog implements Serializable{

    private Integer hashCode =Integer.MIN_VALUE;

    public static final int LOGIN_SUCCESS = 1; //登录成功
    public static final int LOGIN_FAILURE = 2; //登录失败
    public static final int OPERATING = 3; //实际操作

    //primary key
    private Integer id;

    //field
    private Integer category;   //日志类型
    private Date time;          //操作时间
    private String ip;          //操作ip
    private String url;         //操作url
    private String title;       //操作主题
    private String content;     //操作内容
    private Boolean disabled;   //是否隐藏



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public BaseUserLog(Integer id, Integer category, Date time, String ip, String url, String title, String content,Boolean disabled) {
        this.id = id;
        this.category = category;
        this.time = time;
        this.ip = ip;
        this.url = url;
        this.title = title;
        this.content = content;
        this.disabled = disabled;

    }

    public BaseUserLog() {
    }

    public int hashCode(){
        if(Integer.MIN_VALUE == this.hashCode){
            if(null == this.getId()){
                return super.hashCode();
            }
            else{
                String hashStr = this.getClass().getName()+":"+this.getId();
                this.hashCode = hashStr.hashCode();
            }
        }
        return hashCode();
    }


}
