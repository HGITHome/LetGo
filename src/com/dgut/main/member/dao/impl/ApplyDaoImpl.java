package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.ApplyDao;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseApplyFriend;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/1/28.
 */

@Repository
public class ApplyDaoImpl extends HibernateBaseDao<ApplyFriend,Integer> implements ApplyDao {
    @Override
    protected Class<ApplyFriend> getEntityClass() {
        return ApplyFriend.class;
    }

    @Override
    public ApplyFriend getPreviousUnhandledApply(Member publisher, Member receiver) {
        Finder f = Finder.create("select bean from ApplyFriend bean where bean.publisher=:publisher and bean.receiver = :receiver and bean.handle_flag = :flag and bean.isRead=false");
        f.setParam("publisher",publisher);
        f.setParam("receiver",receiver);
        f.setParam("flag",ApplyFriend.ApplyFlag.UNHANDLED);
        f.append(" order by bean.id desc");
        List<ApplyFriend> list = find(f);
        return list.size()==0? null:list.get(0);
    }

    @Override
    public ApplyFriend save(ApplyFriend bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public ApplyFriend findById(int id) {
        ApplyFriend bean =get(id);
        return bean;
    }

    @Override
    public Integer countUnhandleApplication(Member member) {
        String hql="SELECT COUNT(*) FROM ApplyFriend bean WHERE bean.handle_flag=:handle_flag AND bean.receiver.id=:userid AND bean.isRead=false";
        Query query =getSession().createQuery(hql);
        query.setParameter("handle_flag", ApplyFriend.ApplyFlag.UNHANDLED);
        query.setParameter("userid", member.getId());
        return ((Number)query.iterate().next()).intValue();
    }

    @Override
    public Pagination findByUser(Integer id, int pageNo, int pageSize) {
        Finder f = Finder.create("SELECT bean FROM ApplyFriend bean WHERE bean.receiver.id=:userid");
        f.setParam("userid", id);
        f.append(" ORDER BY bean.id DESC");
        return find(f, pageNo, pageSize);
    }

    @Override
    public Pagination getApplyList(String username, String status, Integer pageNo, Integer pageSize) {
       String hql ="select bean from ApplyFriend bean where 1=1";
        Finder f =Finder.create(hql);
        if(StringUtils.isNotBlank(username)){
            f.append(" and bean.publisher.username=:username");
            f.setParam("username",username);
        }
        if(StringUtils.isNotBlank(status)){
            f.append(" and bean.handle_flag=:status");
            f.setParam("status", ApplyFriend.ApplyFlag.valueOf(status));
        }
        f.append(" order by bean.applyTime desc");
        return find(f,pageNo,pageSize);
    }


}
