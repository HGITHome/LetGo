package com.dgut.main.member.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.RechargeDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Recharge;
import com.dgut.main.member.manager.RechargeMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class RechargeMngImpl implements RechargeMng {
	@Transactional(readOnly = true)
	public Pagination getList(String username, String status, Integer pageNo,
							  Integer pageSize) {
		return dao.getList(username,status,pageNo,pageSize);
	}

	@Transactional(readOnly = true)
	public Recharge findById(String id) {
		Recharge entity = dao.findById(id);
		return entity;
	}

	/*@Transactional(readOnly = true)
	public Recharge findByChargeId(String recharge_id) {
		Recharge entity = dao.findByChargeId(recharge_id);
		return entity;
	}*/

	public Recharge save(Recharge bean) {

		dao.save(bean);
		return bean;
	}

	public Recharge update(Recharge bean) {
		Updater<Recharge> updater = new Updater<Recharge>(bean);
		bean = dao.updateByUpdater(updater);

		return bean;
	}

	public Recharge deleteById(String id) {
		Recharge bean = dao.deleteById(id);
		return bean;
	}

	public Recharge[] deleteByIds(String[] ids) {
		Recharge[] beans = new Recharge[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}



	@Override
	public Recharge save(Member member, String id, String chargeId, Double amount, String ip) {
		Recharge bean = new Recharge();
		bean.setRechargeStatus(false);
		bean.setMember(member);
		bean.setId(id);
		bean.setChargeId(chargeId);
		bean.setId(ip);
		bean.setRechargeTime(new Date());
		bean.setRechargeAmount(amount);
		save(bean);
		return bean;
	}

	private RechargeDao dao;

	@Autowired
	public void setDao(RechargeDao dao) {
		this.dao = dao;
	}
}