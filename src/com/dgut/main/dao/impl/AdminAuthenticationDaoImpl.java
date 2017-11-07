package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.main.dao.AuthenticationDao;
import com.dgut.main.entity.AdminAuthentication;
import com.dgut.main.entity.base.BaseAuthentication;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/15.
 */
@Repository
public class AdminAuthenticationDaoImpl extends HibernateBaseDao<AdminAuthentication,String> implements AuthenticationDao {

    @Override
    protected Class<AdminAuthentication> getEntityClass() {
        return AdminAuthentication.class;
    }

    /**
     * 根据id查找认证信息
     * @param id
     * @return
     */
    @Override
    public BaseAuthentication findById(String id) {
        AdminAuthentication entity = get(id);
        return entity;
    }

    /**
     * 清理认证信息
     * @param date
     * @return
     */
    @Override
    public int deleteExpire(Date date) {
        String hql = "delete from AdminAuthentication bean where bean.updateTime <= :d";
        return getSession().createQuery(hql).setTimestamp("d", date)
                .executeUpdate();
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     */
    @Override
    public BaseAuthentication deleteById(String id) {
        AdminAuthentication entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    /**
     * 保存认证信息
     * @param bean
     * @return
     */
    @Override
    public BaseAuthentication save(BaseAuthentication bean) {
        getSession().save(bean);
        return bean;
    }
}
