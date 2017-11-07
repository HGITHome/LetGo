package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by PUNK on 2017/3/31.
 */
public class BaseRedEnvolope implements Serializable {
    private Integer id;           // pk
    private Member sender;        //发送者
    private Date sendTime;        //发送时间
    private Integer num;          //红包个数
    private RedEnvolopeType type; //分配算法
    private Double total;         //总金额
    private Double surplus;       //剩余金额
    private Boolean isPublic;     //是否指定人领取
    private String content;       //留言
    private Boolean isInvalid;     //是否无效



    public enum RedEnvolopeType{
        random(0),average(1);
        private int value = 0;

        private RedEnvolopeType(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }

        public static RedEnvolopeType valueOf(int value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0:
                    return random;
                case 1:
                    return average;
                default:
                    return null;
            }
        }

        public int value() {
            return this.value;
        }
    }


    public Boolean getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(Boolean invalid) {
        isInvalid = invalid;
    }

    public BaseRedEnvolope() {
    }

    public BaseRedEnvolope(Integer id, Member sender, Date sendTime, Double total) {
        this.id = id;
        this.sender = sender;
        this.sendTime = sendTime;

        this.total = total;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public RedEnvolopeType getType() {
        return type;
    }

    public void setType(RedEnvolopeType type) {
        this.type = type;
    }

    public Double getSurplus() {
        return surplus;
    }

    public void setSurplus(Double surplus) {
        this.surplus = surplus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Member getSender() {
        return sender;
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }



    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
