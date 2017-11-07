package com.dgut.main.member.entity.base;

import com.dgut.main.member.entity.Member;

import java.io.Serializable;
import java.util.Date;


public class BaseWithdraw  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	// constructors
	public BaseWithdraw () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseWithdraw (Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseWithdraw (
		Integer id,
		Double amount,
		
		Boolean status,
		java.util.Date apply_date,
		String ip
				) {

		this.setId(id);
		
		this.setWithdrawStatus(status);
		this.setWithdrawTime(apply_date);
		this.setWithdrawAmount(amount);
		this.setWithdrawIp(ip);
		initialize();
	}

	protected void initialize () {}





	// primary key
	private Integer id;

	// fields
	private java.util.Date withdrawTime;
	private java.util.Date finishTime;
	private Double withdrawAmount;

	private Boolean withdrawStatus;
	private String withdrawIp;
	
	
	//relation field
	private Member member;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getWithdrawTime() {
		return withdrawTime;
	}

	public void setWithdrawTime(Date withdrawTime) {
		this.withdrawTime = withdrawTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Double getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(Double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public Boolean getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(Boolean withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public String getWithdrawIp() {
		return withdrawIp;
	}

	public void setWithdrawIp(String withdrawIp) {
		this.withdrawIp = withdrawIp;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
