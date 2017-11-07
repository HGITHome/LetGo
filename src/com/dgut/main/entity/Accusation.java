package com.dgut.main.entity;


import com.dgut.main.member.entity.Member;

import java.util.Date;

/**
 * Created by PUNK on 2017/3/21.
 */
public class Accusation {

    private Integer id;
    private AccusationType type;
    private Date publishTime;
    private Member reporter;
    private Member respondent;
    private String content;
    private Boolean handleFlag;
    private String reply;
    private Date replyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AccusationType getType() {
        return type;
    }

    public void setType(AccusationType type) {
        this.type = type;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Member getRespondent() {
        return respondent;
    }

    public void setRespondent(Member respondent) {
        this.respondent = respondent;
    }

    public Member getReporter() {
        return reporter;
    }

    public void setReporter(Member reporter) {
        this.reporter = reporter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(Boolean handleFlag) {
        this.handleFlag = handleFlag;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Accusation)) return false;

        Accusation that = (Accusation) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (publishTime != null ? !publishTime.equals(that.publishTime) : that.publishTime != null)
            return false;
        if (reporter != null ? !reporter.equals(that.reporter) : that.reporter != null) return false;
        if (respondent != null ? !respondent.equals(that.respondent) : that.respondent != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (handleFlag != null ? !handleFlag.equals(that.handleFlag) : that.handleFlag != null) return false;
        if (reply != null ? !reply.equals(that.reply) : that.reply != null) return false;
        return replyTime != null ? replyTime.equals(that.replyTime) : that.replyTime == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (publishTime != null ? publishTime.hashCode() : 0);
        result = 31 * result + (reporter != null ? reporter.hashCode() : 0);
        result = 31 * result + (respondent != null ? respondent.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (handleFlag != null ? handleFlag.hashCode() : 0);
        result = 31 * result + (reply != null ? reply.hashCode() : 0);
        result = 31 * result + (replyTime != null ? replyTime.hashCode() : 0);
        return result;
    }
}
