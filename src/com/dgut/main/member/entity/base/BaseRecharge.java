package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;

import java.io.Serializable;
import java.util.Date;


public class BaseRecharge  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	// constructors
	public BaseRecharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseRecharge (String id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseRecharge (
		String id,
		Double amount,
		String charge_id,
		Boolean status,
		java.util.Date date,
		String ip
				) {

		this.setId(id);
		this.setChargeId(charge_id);
		this.setRechargeStatus(status);
		this.setRechargeTime(date);
		this.setRechargeAmount(amount);
		this.setRechargeIp(ip);
		initialize();
	}

	protected void initialize () {}





	// primary key
	private String id;

	// fields
	private java.util.Date rechargeTime;
	private Double rechargeAmount;
	private String chargeId;
	private Boolean rechargeStatus;
	private String rechargeIp;
	private String transactionNo;
	
	//relation field
	private Member member;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public Boolean getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(Boolean rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}

	public String getRechargeIp() {
		return rechargeIp;
	}

	public void setRechargeIp(String rechargeIp) {
		this.rechargeIp = rechargeIp;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String toString () {
		return super.toString();
	}

}
