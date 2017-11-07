package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.hibernate4.Updater;
import com.dgut.main.member.dao.TalkLikeDao;
import com.dgut.main.member.entity.TalkLike;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
public class TalkLikeDaoImpl extends HibernateBaseDao<TalkLike,Integer> implements TalkLikeDao {
    @Override
    protected Class<TalkLike> getEntityClass() {
        return TalkLike.class;
    }

    @Override
    public TalkLike findById(Integer id) {
        return get(id);
    }

    @Override
    public List<Object[]> getSexInfo() {
        String hql = "select bean.publisher.gender,count(*) from TalkLike bean group by bean.publisher.gender";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }


}
