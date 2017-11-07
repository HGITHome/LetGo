package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.TalkCommentDao;
import com.dgut.main.member.entity.TalkComment;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
@Repository
public class TalkCommentDaoImpl extends HibernateBaseDao<TalkComment,Integer> implements TalkCommentDao {
    @Override
    protected Class<TalkComment> getEntityClass() {
        return TalkComment.class;
    }

    @Override
    public TalkComment findById(Integer id) {
        return get(id);
    }

    @Override
    public Pagination getList(String username, String status, int pageNo,int pageSize) {
        String hql ="select bean from TalkComment bean where 1=1";
        Finder f = Finder.create(hql);
        if(StringUtils.isNotBlank(username)){
            f.append(" and bean.publisher.username=:username");
            f.setParam("username",username);
        }
        if(StringUtils.isNotBlank(status)){
            f.append(" and bean.disabled=:disabled");
            if(status.equals("1")){
                f.setParam("disabled",true);
            }
            else{
                f.setParam("disabled",false);
            }
        }
        return find(f,pageNo,pageSize);
    }

    @Override
    public List<Object[]> getSexInfo() {
        String hql = "select bean.publisher.gender,count(*) from TalkComment bean group by bean.publisher.gender";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }
}
