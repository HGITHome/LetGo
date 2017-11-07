package com.dgut.main.member.dao;

import java.util.List;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Withdraw;


public interface WithdrawDao {
	public Pagination getList(String username, String queryStatus,
							  Integer pageNo, Integer pageSize);

	public Withdraw findById(Integer id);
	
	/**
	 * 提现列表
	 * @param userid
	 * @param flag
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getListByUser(Integer userid, Boolean flag, Integer pageNo, Integer pageSize);

	
	public Withdraw save(Withdraw bean);

	public Withdraw updateByUpdater(Updater<Withdraw> updater);

	public Withdraw deleteById(Integer id);
}