package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.ApkVersionDao;
import com.dgut.main.entity.ApkVersion;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by PUNK on 2017/2/12.
 */
@Repository
public class ApkVersionDaoImpl extends HibernateBaseDao<ApkVersion,String> implements ApkVersionDao {
    @Override
    protected Class<ApkVersion> getEntityClass() {
        return ApkVersion.class;
    }

    @Override
    public Pagination getList(String queryType, String isValid, int pageNo, int pageSize) {
        Finder f = Finder.create("SELECT bean FROM ApkVersion bean WHERE 1=1");

        if(StringUtils.isNotBlank(queryType)){
            f.append(" AND bean.type=:queryType");
            f.setParam("queryType", queryType);
        }

        if(StringUtils.isNotBlank(isValid)){
            f.append(" AND bean.isValid=:isValid");
            if(isValid.equals("0")){
                f.setParam("isValid", false);
            }
            else{
                f.setParam("isValid", true);
            }

        }

        f.append(" ORDER BY bean.publish_time desc ");
        return find(f,pageNo,pageSize);
    }

    @Override
    public ApkVersion save(ApkVersion bean) {
        // TODO Auto-generated method stub
        getSession().save(bean);
        return bean;
    }

    @Override
    public ApkVersion getLatestVersionNo() {

        Finder f = Finder.create("SELECT bean FROM ApkVersion bean WHERE 1=1");

        f.append(" ORDER BY bean.version_id desc ");
        f.setFirstResult(0);
        f.setMaxResults(1);
        return find(f).size()==0? null: (ApkVersion)find(f).get(0);
    }

    @Override
    public ApkVersion getLatestValidVersion() {
        Finder f = Finder.create("SELECT bean FROM ApkVersion bean WHERE 1=1");
        f.append(" AND bean.isValid=true");
        f.append(" ORDER BY bean.version_id desc ");
        f.setFirstResult(0);
        f.setMaxResults(1);
        return find(f).size()==0? null: (ApkVersion)find(f).get(0);
    }

    @Override
    public ApkVersion findById(String id) {
        ApkVersion version = get(id);
        return version;
    }

    @Override
    public ApkVersion deleteById(String id) {
        ApkVersion entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }
}
