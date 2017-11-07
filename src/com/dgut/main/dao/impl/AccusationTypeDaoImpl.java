package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.AccusationTypeDao;
import com.dgut.main.entity.AccusationType;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/3/23.
 */
@Repository
public class AccusationTypeDaoImpl extends HibernateBaseDao<AccusationType,Integer> implements AccusationTypeDao {
    @Override
    protected Class<AccusationType> getEntityClass() {
        return AccusationType.class;
    }

    @Override
    public Pagination getList(String categoryId,String typeName, int pageNo, int pageSize) {
        String hql ="select bean from AccusationType bean where 1=1";
        Finder f = Finder.create(hql);
        if(StringUtils.isNotBlank(typeName)){
            f.append(" and bean.name like :type");
            f.setParam("type","%"+typeName+"%");
        }
        f.append(" and bean.category.id=:categoryId");
        f.setParam("categoryId",Integer.parseInt(categoryId));
        f.append(" order by bean.name ");
        return find(f,pageNo,pageSize);
    }

    @Override
    public List findByTypeByName(String categoryId, String name) {
        String hql ="select bean from AccusationType bean where 1=1";
        Finder f = Finder.create(hql);
        f.append(" and bean.category.id=:categoryId and bean.name=:typeName");
        f.setParam("categoryId",Integer.parseInt(categoryId));
        f.setParam("typeName",name);

        return find(f);
    }

    @Override
    public AccusationType save(AccusationType type) {
        getSession().save(type);
        return type;
    }

    @Override
    public AccusationType findById(Integer id) {
        AccusationType type = get(id);
        return type;
    }

    @Override
    public AccusationType deleteType(int i) {
        AccusationType type  = findById(i);
        getSession().delete(type);
        return type;
    }
}
