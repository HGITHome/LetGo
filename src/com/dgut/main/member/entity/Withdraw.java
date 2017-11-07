package com.dgut.main.member.entity;


import com.dgut.main.member.entity.base.BaseWithdraw;

public class Withdraw extends BaseWithdraw {

	private int hashCode = Integer.MIN_VALUE;
	

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Withdraw () {
		super();
	}
	

	/**
	 * Constructor for primary key
	 */
	public Withdraw (Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Withdraw (
			Integer id,
			Double amount,
			
			Boolean status,
			java.util.Date date,
			String ip
					) {

			

		super (
			id,
			amount,
			status,
			date,
			ip);
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Withdraw)) return false;
		else {
			Withdraw cmsLog = (Withdraw) obj;
			if (null == this.getId() || null == cmsLog.getId()) return false;
			else return (this.getId().equals(cmsLog.getId()));
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


	public String toString () {
		return super.toString();
	}
}
