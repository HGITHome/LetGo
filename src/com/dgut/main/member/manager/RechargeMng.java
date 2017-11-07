package com.dgut.main.member.manager;

import java.util.List;
import java.util.Set;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Recharge;


public interface RechargeMng {
	public Pagination getList(String username, String status, Integer pageNo,
							  Integer pageSize);

	public Recharge findById(String id);

//	public Recharge findByChargeId(String recharge_id);

	public Recharge save(Recharge bean);

	public Recharge update(Recharge bean);

	public Recharge deleteById(String id);

	public Recharge[] deleteByIds(String[] ids);

	Recharge save(Member member, String id, String chargeId, Double amount, String ip);
}