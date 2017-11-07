package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseRedEnvolopeReceiver;

import java.util.Date;

/**
 * Created by PUNK on 2017/3/31.
 */
public class RedEnvolopeReceiver extends BaseRedEnvolopeReceiver {

    private RedEnvolope redEnvolope;

    public RedEnvolope getRedEnvolope() {
        return redEnvolope;
    }

    public void setRedEnvolope(RedEnvolope redEnvolope) {
        this.redEnvolope = redEnvolope;
    }

    private int hashCode = Integer.MIN_VALUE;



    public RedEnvolopeReceiver() {
    }



    public RedEnvolopeReceiver(Integer id, Date receiveTime, Double amount, Member receiver, Boolean isReceived) {
        super(id, receiveTime, amount, receiver,isReceived);

    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Recharge)) return false;
        else {
            RedEnvolopeReceiver redBag = (RedEnvolopeReceiver) obj;
            if (null == this.getId() || null == redBag.getId()) return false;
            else return (this.getId().equals(redBag.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }
}
