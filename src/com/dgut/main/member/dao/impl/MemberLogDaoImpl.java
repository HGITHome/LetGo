package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.UserLogDao;
import com.dgut.main.entity.base.BaseUserLog;
import com.dgut.main.member.entity.MemberLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 会员日志dao
 * Created by PUNK on 2017/1/24.
 */

@Repository
public class MemberLogDaoImpl extends HibernateBaseDao<MemberLog,Integer> implements UserLogDao {
    @Override
    public BaseUserLog save(BaseUserLog bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public Pagination getPage(int operating, String queryUsername, String queryTitle, String queryIp, int pageNo, int pageSize) {
        Finder f = Finder.create("select bean from MemberLog bean where 1=1");
        f.append(" and bean.category=:category");
        f.setParam("category",operating);
        if(StringUtils.isNotBlank(queryUsername)){
            f.append(" and bean.member.username like :username");
            f.setParam("username","%"+queryUsername+"%");
        }
        if(StringUtils.isNotBlank(queryTitle)){
            f.append(" and bean.title like :title");
            f.setParam("title","%"+queryTitle+"%");
        }
        if(StringUtils.isNotBlank(queryIp)){
            f.append(" and bean.ip=:ip ");
            f.setParam("ip",queryIp);
        }
        f.append(" order by bean.time desc");
        return find(f,pageNo,pageSize);
    }

    @Override
    protected Class<MemberLog> getEntityClass() {
        return MemberLog.class;
    }
}
