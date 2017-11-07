package com.dgut.main.member.entity;


import com.dgut.main.member.entity.base.BaseRecharge;

public class Recharge extends BaseRecharge {



	private int hashCode = Integer.MIN_VALUE;
	

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Recharge () {
		super();
	}
	

	/**
	 * Constructor for primary key
	 */
	public Recharge (String id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Recharge (
			String id,
			Double amount,
			String charge_id,
			Boolean status,
			java.util.Date date,
			String ip
					) {

			

		super (
			id,
			amount,
			charge_id,
			status,
			date,
			ip);
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof Recharge)) return false;
		else {
			Recharge cmsLog = (Recharge) obj;
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
}
