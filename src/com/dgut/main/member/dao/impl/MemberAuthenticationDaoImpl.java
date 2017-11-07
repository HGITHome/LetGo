package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.main.dao.AuthenticationDao;
import com.dgut.main.entity.AdminAuthentication;
import com.dgut.main.entity.base.BaseAuthentication;
import com.dgut.main.member.entity.MemberAuthentication;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/22.
 */
@Repository
public class MemberAuthenticationDaoImpl extends HibernateBaseDao<MemberAuthentication,String> implements AuthenticationDao {
    @Override
    public BaseAuthentication findById(String id) {
        MemberAuthentication authentication =get(id);
        return authentication;
    }

    @Override
    public int deleteExpire(Date date) {
        String hql = "delete from MemberAuthentication bean where bean.updateTime <= :d";
        return getSession().createQuery(hql).setTimestamp("d", date)
                .executeUpdate();
    }

    @Override
    public BaseAuthentication deleteById(String id) {
        MemberAuthentication entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    public BaseAuthentication save(BaseAuthentication bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    protected Class<MemberAuthentication> getEntityClass() {
        return MemberAuthentication.class;
    }
}
