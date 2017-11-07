package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by PUNK on 2017/1/25.
 */
public class BaseApplyFriend implements Serializable{

    //primary key
    private Integer id; //编号



    //fields
    //对象
    private Member publisher; //申请人
    private Member receiver;  //接受者

//    private Integer handle_flag; //处理状态(0为待处理，1为通过，2为失败,4为被刷新)

    private ApplyFlag handle_flag;

    private String apply_reason;  //申请理由
    private Boolean isRead; //是否阅读

    private Date applyTime;//申请时间
    private Date replyTime;//处理时间




    public enum ApplyFlag {
        UNHANDLED,ACCEPT,FAILURE
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public ApplyFlag getHandle_flag() {
        return handle_flag;
    }

    public void setHandle_flag(ApplyFlag handle_flag) {
        this.handle_flag = handle_flag;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
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

    public Member getReceiver() {
        return receiver;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    /*public Integer getHandle_flag() {
        return handle_flag;
    }

    public void setHandle_flag(Integer handle_flag) {
        this.handle_flag = handle_flag;
    }*/



    public String getApply_reason() {
        return apply_reason;
    }

    public void setApply_reason(String apply_reason) {
        this.apply_reason = apply_reason;
    }


   /* public BaseApplyFriend(Integer id, Member publisher, Member receiver, Integer handle_flag, String apply_reason, Boolean isRead) {
        this.id = id;
        this.publisher = publisher;
        this.receiver = receiver;
        this.handle_flag = handle_flag;
        this.apply_reason = apply_reason;
        this.isRead = isRead;
    }*/

    public BaseApplyFriend(Integer id, Member publisher, Member receiver, ApplyFlag handle_flag, String apply_reason, Boolean isRead) {
        this.id = id;
        this.publisher = publisher;
        this.receiver = receiver;
        this.handle_flag = handle_flag;
        this.apply_reason = apply_reason;
        this.isRead = isRead;
    }

    public BaseApplyFriend() {
    }
}
