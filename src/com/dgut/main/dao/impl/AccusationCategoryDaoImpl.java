package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.AccusationCategoryDao;
import com.dgut.main.dao.AccusationDao;
import com.dgut.main.dao.AccusationTypeDao;
import com.dgut.main.entity.AccusationCategory;
import com.dgut.main.entity.AccusationType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/3/21.
 */
@Repository
public class AccusationCategoryDaoImpl extends HibernateBaseDao<AccusationCategory,Integer> implements AccusationCategoryDao {

    @Autowired
    private AccusationTypeDao typeDao;

    @Override
    public Pagination getCategory(String queryName, int pageNo, int pageSize) {
       String hql ="select bean from AccusationCategory bean where 1=1";
        Finder f = Finder.create(hql);
        if(StringUtils.isNotBlank(queryName)){
            f.append(" and bean.name like :name");
            f.setParam("name","%"+queryName+"%");
        }
        f.append(" order by bean.priority");
        return find(f,pageNo,pageSize);
    }

    @Override
    public AccusationCategory save(AccusationCategory bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public AccusationCategory fingById(int id) {
        return get(id);
    }

    @Override
    public AccusationCategory findByName(String categoryName) {
        return findUniqueByProperty("name",categoryName);
    }

    @Override
    public AccusationCategory deleteCategory(Integer id) {
        AccusationCategory bean = get(id);

        List<AccusationType> types  = bean.getTypes();
        for(AccusationType type:types){
           typeDao.deleteType(type.getId());
        }


        getSession().delete(bean);
        return bean;
    }

    @Override
    protected Class<AccusationCategory> getEntityClass() {
        return AccusationCategory.class;
    }


}
