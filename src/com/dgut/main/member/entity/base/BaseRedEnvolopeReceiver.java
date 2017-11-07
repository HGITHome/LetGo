package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by PUNK on 2017/3/31.
 */
public abstract class BaseRedEnvolopeReceiver implements Serializable {

    private Integer id;
    private Date receiveTime;
    private Double amount;
    private Member receiver;
    private Boolean isReceived;



    public BaseRedEnvolopeReceiver() {
    }



    public BaseRedEnvolopeReceiver(Integer id, Date receiveTime, Double amount, Member receiver, Boolean isReceived) {
        this.id = id;
        this.receiveTime = receiveTime;
        this.amount = amount;
        this.receiver = receiver;
        this.isReceived = isReceived;
    }

    public Boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Boolean received) {
        isReceived = received;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Member getReceiver() {
        return receiver;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }


}
