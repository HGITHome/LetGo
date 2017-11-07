package com.dgut.main.member.entity;

import com.dgut.common.pck.Encrypt;
import com.dgut.main.Constants;
import com.dgut.main.entity.base.BaseUser;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by PUNK on 2017/1/22.
 */
public class Member extends BaseUser {

    private String icon;  //头像

    private String mobile; //手机

    private City city ; //所属城市

    private String realname;//真实姓名

    private String balance; //账户余额

    private String easemob_name;//环信名

    private Boolean easemob_flag; //是否在网（环信）

    private Set<Friendship> friendships;//友谊list

    private String payAccount; //支付宝账户

    private Double money;//发、领取红包的金额

    private Long count;//次数

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Set<Friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(Set<Friendship> friendships) {
        this.friendships = friendships;
    }

    //private List<Friendship> friendships;

   /* public Set<Friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(Set<Friendship> friendships) {
        this.friendships = friendships;
    }*/

   /* public List<Friendship> getFriendships() {
        return friendships;
    }

    public void setFriendships(java.util.List<Friendship> friendships) {
        this.friendships = friendships;
    }*/

    public Boolean getEasemob_flag() {
        return easemob_flag;
    }

    public void setEasemob_flag(Boolean easemob_flag) {
        this.easemob_flag = easemob_flag;
    }

    public String getEasemob_name() {
        return easemob_name;
    }

    public void setEasemob_name(String easemob_name) {
        this.easemob_name = easemob_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Member(Integer id, String username, String password, Boolean gender, Boolean disabled, Date registerTime, String registerIP, Date lastLoginTime, String lastLoginIP, Integer loginCount, Integer errorCount, Date errorTime, String icon, String mobile, City city) {
        super(id, username, password, gender, disabled, registerTime, registerIP, lastLoginTime, lastLoginIP, loginCount, errorCount, errorTime);
        this.icon = icon;
        this.mobile = mobile;
        this.city = city;
    }

    public Member() {

    }

    public void initialize() {
        this.setLoginCount(0);
        this.setLastLoginTime(null);
        this.setLastLoginIP(null);
        this.setCity(null);
        this.setIcon(null);
        this.setGender(false);
        this.setErrorTime(null);
        this.setErrorCount(0);
        this.setDisabled(false);
        this.setEasemob_flag(false);
        this.setBalance(Encrypt.encrypt3DES("0.0", Constants.ENCRYPTION_KEY));
    }

    public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof Member)) return false;
        else {
            Member member = (Member) obj;
            if (null == this.getId() || null == member.getId()) return false;
            else {
                return (this.getId().equals(member.getId()));
            }
        }
    }
}
