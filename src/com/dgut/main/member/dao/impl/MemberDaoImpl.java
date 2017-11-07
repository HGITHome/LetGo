package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.MemberDao;
import com.dgut.main.member.entity.Member;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


/**
 * Created by PUNK on 2017/1/22.
 */
@Repository
public class MemberDaoImpl extends HibernateBaseDao<Member, Integer> implements MemberDao {
    @Override
    public int countByPhone(String mobile) {
        String hql = "select count(*) from Member bean where bean.mobile=:phone";
        Query query = getSession().createQuery(hql);
        query.setParameter("phone", mobile);
        return ((Number) query.iterate().next()).intValue();
    }

    @Override
    public int countByUsername(String username) {
        String hql = "select count(*) from Member bean where bean.username=:username";
        Query query = getSession().createQuery(hql);
        query.setParameter("username", username);
        return ((Number) query.iterate().next()).intValue();
    }

    @Override
    public Member findByMobile(String mobile) {
        return findUniqueByProperty("mobile", mobile);
    }

    @Override
    public Member findByUsername(String username) {
        return findUniqueByProperty("username", username);
    }

    @Override
    public Member save(Member member) {
        getSession().save(member);
        return member;
    }

    @Override
    public Member findById(Integer id) {
        Member member =get(id);
        return member;
    }

    @Override
    public Pagination getList(String username,String disabled,Integer pageNo,Integer pageSize) {
        String hql ="select bean from Member bean where 1=1 ";
        Finder f = Finder.create(hql);
        if(StringUtils.isNotBlank(username)){
            f.append(" and bean.username=:username");
            f.setParam("username",username);
        }
        if(!StringUtils.isBlank(disabled)){
            f.append(" and bean.disabled=:disabled");
            if(disabled.equals("0")){
                f.setParam("disabled",false);
            }
            else{
                f.setParam("disabled",true);
            }

        }
        f.append(" order by bean.id ");
        return find(f,pageNo,pageSize);
    }


    @Override
    protected Class<Member> getEntityClass() {
        return Member.class;
    }
}
