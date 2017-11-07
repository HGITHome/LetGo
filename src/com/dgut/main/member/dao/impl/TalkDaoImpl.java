package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.TalkDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.member.entity.base.BaseRedEnvolope;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/1/31.
 */
@Repository
public class TalkDaoImpl extends HibernateBaseDao<Talk,Integer> implements TalkDao {
    @Override
    protected Class<Talk> getEntityClass() {
        return Talk.class;
    }

    @Override
    public Talk findById(int id) {
//        String hql ="select bean from Talk bean left join bean.talkLikes tl ";
//        Finder f = Finder.create(hql);
//        f.append(" where tl.disabled=false and bean.id=:id");
//        f.setParam("id",id);
//        return (Talk) find(f).get(0);
        return  get(id);
    }

    @Override
    public Talk save(Talk talk) {
        getSession().save(talk);
        return talk;
    }

    @Override
    public Pagination findTalkByUser(Member member, int pageNo, int pageSize) {
        String hql = "select bean from Talk bean where bean.disabled=false and bean.publisher.id=:userid order by bean.publish_time desc";
        Finder f = Finder.create(hql);
        f.setParam("userid",member.getId());
        return find(f,pageNo,pageSize);
    }

    @Override
    public Pagination getTalkList(Member member, int pageNo, int pageSize) {
        //查找自己的
        Finder f = Finder
                .create("SELECT bean FROM Talk bean WHERE bean.publisher.id=:user_id and bean.disabled=false");
        //查找非黑名单的好友
       /* f.append(" OR bean.publisher.id IN ( SELECT friend.id FROM Friendship friendship WHERE friendship.owner.id=:user_id and friendship.friendship_status=0)");*/

        //排除自己的黑名单列表以及自己被别人拉黑的用户
        f.append(" OR bean.publisher.id IN (select f.friend.id from Friendship f where f.owner.id=:user_id and f.friendship_status=0 and f.friend.id not in (select f1.owner.id from Friendship    f1 where f1.friend.id=:user_id and f1.friendship_status=1))");

        //按时间排序
        f.append(" ORDER BY bean.publish_time desc");
        f.setParam("user_id", member.getId());
        return find(f,pageNo,pageSize);
    }

    @Override
    public Pagination getTalkList(String username, String status, int pageNo, int pageSize) {
//        String hql ="select bean from Talk bean where 1=1";
        String hql ="select distinct bean from Talk bean left join bean.talkLikes tl where 1=1";
        Finder f= Finder.create(hql);
        if(StringUtils.isNotBlank(username)){
            f.append(" and bean.publisher.username=:username");
            f.setParam("username",username);
        }
        if(StringUtils.isNotBlank(status)){
            f.append(" and bean.disabled=:disabled");
            if(status.equals("0")){
                f.setParam("disabled",false);
            }
            else{
                f.setParam("disabled",true);
            }

        }
//        f.append(" and tl.disabled=false ");
        f.append(" order by bean.publish_time desc");
        return find(f,pageNo,pageSize);
    }

    @Override
    public List<Object[]> getTop3ShareMen() {
        StringBuffer hql = new StringBuffer(
                "select bean.publisher,count(*) as count1 " +
                        "from Talk bean  where bean.disabled=false "+
                        "group by bean.publisher.id  "
        ) ;
        hql.append(" order by count1 desc");

        List<Object[]> list = getSession().createQuery(hql.toString()).setMaxResults(3).list();

        return list;
    }

    @Override
    public List<Object[]> getTop3TapLikeMen() {
        StringBuffer hql = new StringBuffer(
                "select bean.publisher,count(*) as count1 " +
                        "from TalkComment bean  where bean.disabled=false "+
                        "group by bean.publisher.id  "
        ) ;
        hql.append(" order by count1 desc");

        List<Object[]> list = getSession().createQuery(hql.toString()).setMaxResults(3).list();

        return list;
    }

    @Override
    public List<Object[]> getSexInfo() {
        String hql = "select bean.publisher.gender,count(*) from Talk bean group by bean.publisher.gender";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }

    @Override
    public List<Object[]> getTimeInfo() {
        String hql = "select DATE_FORMAT(bean.publish_time,'%Y-%m-%d'),count(*) from Talk bean group by DATE_FORMAT(bean.publish_time,'%Y-%m-%d')";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }
}
