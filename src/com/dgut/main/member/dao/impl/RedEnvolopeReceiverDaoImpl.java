package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.main.member.dao.RedEnvolopeReceiverDao;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.entity.RedEnvolopeReceiver;
import org.springframework.stereotype.Repository;

/**
 * Created by PUNK on 2017/4/4.
 */
@Repository
public class RedEnvolopeReceiverDaoImpl extends HibernateBaseDao<RedEnvolopeReceiver,Integer> implements RedEnvolopeReceiverDao {
    @Override
    protected Class<RedEnvolopeReceiver> getEntityClass() {
        return RedEnvolopeReceiver.class;
    }
}
