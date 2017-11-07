package com.dgut.main.member.manager;

import java.util.List;
import java.util.Set;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Withdraw;


public interface WithdrawMng {
	public Pagination getList(String username, String status, Integer pageNo,
							  Integer pageSize);

	public Withdraw findById(Integer id);

	/**
	 * 查看提现记录
	 * @param userid
	 * @param flag
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getListByUser(Integer userid, Boolean flag, Integer pageNo, Integer pageSize);

	public Withdraw save(Withdraw bean);

	public Withdraw update(Withdraw bean);

	public Withdraw deleteById(Integer id);

	public Withdraw[] deleteByIds(Integer[] ids);

	Withdraw save(Member member, double amount, String ipAddr);
}