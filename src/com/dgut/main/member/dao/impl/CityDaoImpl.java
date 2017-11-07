package com.dgut.main.member.dao.impl;

import java.util.List;

import com.dgut.main.member.dao.CityDao;
import com.dgut.main.member.entity.City;
import org.springframework.stereotype.Repository;

import com.dgut.common.hibernate4.HibernateBaseDao;



@Repository
public class CityDaoImpl extends HibernateBaseDao<City, Integer>
		implements CityDao {

	@Override
	public City findById(Integer id) {
		City entity = get(id);
		return entity;
	}



	@Override
	protected Class<City> getEntityClass() {
		return City.class;
	}
}