package com.dgut.main.member.dao;

import java.util.List;


import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Recharge;


public interface RechargeDao {
	public Pagination getList(String username, String queryStatus,
							  Integer pageNo, Integer pageSize);

	public Recharge findById(String id);

//	public Recharge findByChargeId(String recharge_id);

	public Recharge save(Recharge bean);

	public Recharge updateByUpdater(Updater<Recharge> updater);

	public Recharge deleteById(String id);
}