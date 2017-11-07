package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.GroupMember;
import com.dgut.main.member.entity.Member;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by PUNK on 2017/2/22.
 */
public class BaseChatGroup implements Serializable {

    public static final Long CAPACITY = 200L;

    private int hashCode = Integer.MIN_VALUE;

    //primary key
    //主键
    private Integer id;

    //fields
    //群组名
    private String groupName;
    //群组介绍
    private String description;
    // 容量
    private Long capacity;
    // 创建时间
    private Date createTime;
    // 创建者
    private Member owner;
    // 是否是公开群
    private Boolean isPublic;
    // 是否需要群主验证
    private Boolean approval;

    // 是否禁用
    private Boolean disabled;

    //总人数
    private Integer total ;

    // 会员列表
    private List<GroupMember> memberList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BaseChatGroup(String groupName, String description, Long capacity, Member owner, Boolean isPublic, Boolean approval) {
        this.groupName = groupName;
        this.description = description;
        this.capacity = capacity;
        this.owner = owner;
        this.isPublic = isPublic;
        this.approval = approval;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public BaseChatGroup() {
    }

    public BaseChatGroup(Integer id, String groupName, String desc, Long capacity, Date createTime, Member owner, Boolean isPublic, Boolean approval, List<GroupMember> memberList) {
        this.id = id;
        this.groupName = groupName;
        this.description = desc;
        this.capacity = capacity;
        this.createTime = createTime;
        this.owner = owner;
        this.isPublic = isPublic;
        this.approval = approval;
        this.memberList = memberList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public List<GroupMember> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<GroupMember> memberList) {
        this.memberList = memberList;
    }


    @Override
    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode){
            if(null == this.getId()){
                return super.hashCode();
            }
            else{
                String hashStr = this.getClass().getName()+":"+this.getId();
                this.hashCode = hashStr.hashCode();
            }
        }
        return  this.hashCode;
    }
}
