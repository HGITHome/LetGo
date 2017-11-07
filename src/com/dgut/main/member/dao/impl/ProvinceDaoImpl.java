package com.dgut.main.member.dao.impl;

import java.util.List;

import com.dgut.main.member.dao.ProvinceDao;
import com.dgut.main.member.entity.Province;
import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate4.HibernateBaseDao;



@Repository
public class ProvinceDaoImpl extends HibernateBaseDao<Province, Integer>
		implements ProvinceDao {
	@SuppressWarnings("unchecked")
	public List<Province> getList() {
		String hql = "from Province bean order by bean.priority asc";
		return find(hql);
	}




	@Override
	protected Class<Province> getEntityClass() {
		return Province.class;
	}
}