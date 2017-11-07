package com.dgut.main.member.entity;

import com.dgut.main.member.entity.base.BaseRedEnvolope;

import java.util.Date;
import java.util.List;

/**
 * Created by PUNK on 2017/3/31.
 */
public class RedEnvolope extends BaseRedEnvolope {

    private ChatGroup group;

    private Member LuckyMan;

    private List<RedEnvolopeReceiver> receiverList;

    public Member getLuckyMan() {
        return LuckyMan;
    }

    public void setLuckyMan(Member luckyMan) {
        LuckyMan = luckyMan;
    }

    public ChatGroup getGroup() {
        return group;
    }

    public void setGroup(ChatGroup group) {
        this.group = group;
    }

    public List<RedEnvolopeReceiver> getReceiverList() {

        return receiverList;
    }

    public void setReceiverList(List<RedEnvolopeReceiver> receiverList) {
        this.receiverList = receiverList;
    }

    private int hashCode = Integer.MIN_VALUE;



    public RedEnvolope() {
    }

   

    public RedEnvolope(Integer id, Member sender, Date sendTime, Double total, List<RedEnvolopeReceiver> receiverList) {
        super(id, sender, sendTime, total);
        this.receiverList = receiverList;
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Recharge)) return false;
        else {
            RedEnvolope redBag = (RedEnvolope) obj;
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
