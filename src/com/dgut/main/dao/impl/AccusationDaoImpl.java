package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.AccusationDao;
import com.dgut.main.entity.Accusation;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by PUNK on 2017/3/21.
 */
@Repository
public class AccusationDaoImpl extends HibernateBaseDao<Accusation,Integer> implements AccusationDao {




    @Override
    protected Class<Accusation> getEntityClass() {
        return Accusation.class;
    }

    @Override
    public Accusation save(Accusation accusation) {
        getSession().save(accusation);
        return accusation;
    }

    @Override
    public Pagination getList(String reporter, String respondent, Integer type, String status,int pageNo, int pageSize) {
        String hql ="select bean from Accusation bean where 1=1";
        Finder f = Finder.create(hql);
        if(StringUtils.isNotBlank(reporter)){
            f.append(" and bean.reporter.username like :reporter");
            f.setParam("reporter","%"+reporter+"%");
        }
        if(StringUtils.isNotBlank(respondent)){
            f.append(" and bean.respondent.username like :username");
            f.setParam("username","%"+respondent+"%");
        }

        if(type!=null){
            f.append(" and bean.type.category.id=:category_id");
            f.setParam("category_id",type);
        }
        if(StringUtils.isNotBlank(status)){
            f.append(" and bean.handleFlag = :flag");
            if(status.equals("1")){
                f.setParam("flag",true);
            }
            else{
                f.setParam("flag",false);
            }

        }
        f.append(" order by bean.publishTime desc");
        return find(f,pageNo,pageSize);
    }

    @Override
    public Accusation findById(int id) {
        Accusation bean = get(id);
        return bean;
    }
}
