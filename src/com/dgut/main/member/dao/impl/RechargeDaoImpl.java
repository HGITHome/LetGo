package com.dgut.main.member.dao.impl;

import java.util.List;

import com.dgut.main.member.dao.RechargeDao;
import com.dgut.main.member.entity.Recharge;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;


import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;




@Repository
public class RechargeDaoImpl extends HibernateBaseDao<Recharge, String>
		implements RechargeDao {
	@SuppressWarnings("unchecked")
	public Pagination getList(String username,String status,Integer pageNo,Integer pageSize) {
		String hql = "from Recharge bean WHERE 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(username)){
			f.append(" and bean.member.username like :username");
			f.setParam("username", "%"+username+"%");
		}
		if(StringUtils.isNotBlank(status)){
			f.append(" and bean.rechargeStatus=:status");
			f.setParam("status", Boolean.parseBoolean(status));
		}
		f.append(" order by bean.rechargeTime desc");
		return find(f, pageNo, pageSize);
	}

	public Recharge findById(String id) {
		Recharge entity = get(id);
		return entity;
	}
	
	/*public Recharge findByChargeId(String recharge_id){
		Finder f = Finder.create("select bean from Recharge bean  where bean.charge_id=:recharge_id");
		f.setParam("recharge_id", recharge_id);
		List<Recharge> list=find(f);
		return list.size()==0? null:list.get(0);
	}*/

	public Recharge save(Recharge bean) {
		getSession().save(bean);
		return bean;
	}

	public Recharge deleteById(String id) {
		Recharge entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<Recharge> getEntityClass() {
		return Recharge.class;
	}
}