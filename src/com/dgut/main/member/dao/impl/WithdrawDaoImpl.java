package com.dgut.main.member.dao.impl;



import com.dgut.main.member.dao.WithdrawDao;
import com.dgut.main.member.entity.Withdraw;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;



import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;



@Repository
public class WithdrawDaoImpl extends HibernateBaseDao<Withdraw, Integer>
		implements WithdrawDao {
	@SuppressWarnings("unchecked")
	public Pagination getList(String username,String status,Integer pageNo,Integer pageSize) {
		String hql = "from Withdraw bean WHERE 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.member.username like :username");
			f.setParam("username", "%"+username+"%");
		}
		if(StringUtils.isNotBlank(status)){
			f.append(" and bean.withdrawStatus=:status");
			f.setParam("status", Boolean.parseBoolean(status));
		}
		f.append(" order by bean.withdrawTime desc");
		return find(f, pageNo, pageSize);
	}

	public Withdraw findById(Integer id) {
		Withdraw entity = get(id);
		return entity;
	}
	
	

	public Withdraw save(Withdraw bean) {
		getSession().save(bean);
		return bean;
	}

	public Withdraw deleteById(Integer id) {
		Withdraw entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Withdraw> getEntityClass() {
		return Withdraw.class;
	}

	@Override
	public Pagination getListByUser(Integer userid, Boolean flag,
			Integer pageNo, Integer pageSize) {
		Finder f=Finder.create("SELECT bean FROM Withdraw bean WHERE 1=1");
		if(userid!=null){
			f.append(" AND bean.member.id=:userid");
			f.setParam("userid", userid);
		}
		if(flag!=null){
			f.append(" AND bean.withdraw_status=:flag");
			f.setParam("flag", flag);
		}
		f.append(" ORDER BY bean.withdraw_time DESC");
		return find(f,pageNo,pageSize);
	}
}