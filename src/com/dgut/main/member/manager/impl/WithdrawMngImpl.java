package com.dgut.main.member.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dgut.common.web.RequestUtils;
import com.dgut.main.member.dao.WithdrawDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Withdraw;
import com.dgut.main.member.manager.WithdrawMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;


@Service
@Transactional
public class WithdrawMngImpl implements WithdrawMng {
	@Transactional(readOnly = true)
	public Pagination getList(String username, String status, Integer pageNo,
			Integer pageSize) {
		return dao.getList(username,status,pageNo,pageSize);
	}

	@Transactional(readOnly = true)
	public Withdraw findById(Integer id) {
		Withdraw entity = dao.findById(id);
		return entity;
	}

	

	public Withdraw save(Withdraw bean) {

		dao.save(bean);
		return bean;
	}

	public Withdraw update(Withdraw bean) {
		Updater<Withdraw> updater = new Updater<Withdraw>(bean);
		bean = dao.updateByUpdater(updater);

		return bean;
	}

	public Withdraw deleteById(Integer id) {
		Withdraw bean = dao.deleteById(id);
		return bean;
	}

	public Withdraw[] deleteByIds(Integer[] ids) {
		Withdraw[] beans = new Withdraw[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Override
	public Withdraw save(Member member, double amount, String ipAddr) {
		Withdraw withdraw = new Withdraw();
		withdraw.setMember(member);
		withdraw.setWithdrawTime(new Date());
		withdraw.setWithdrawStatus(false);
		withdraw.setWithdrawAmount(amount);
		withdraw.setWithdrawIp(ipAddr);
		save(withdraw);
		return withdraw;
	}

	private WithdrawDao dao;

	@Autowired
	public void setDao(WithdrawDao dao) {
		this.dao = dao;
	}

	@Override
	public Pagination getListByUser(Integer userid, Boolean flag,
			Integer pageNo, Integer pageSize) {
		return dao.getListByUser(userid,flag,pageNo,pageSize);
		
	}
}