package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.RedEnvolopeDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.entity.base.BaseRedEnvolope;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by PUNK on 2017/3/31.
 */
@Repository
public class RedEnvolopeDaoImpl extends HibernateBaseDao<RedEnvolope,Integer> implements RedEnvolopeDao{
    @Override
    protected Class<RedEnvolope> getEntityClass() {
        return RedEnvolope.class;
    }

    @Override
    public RedEnvolope save(RedEnvolope bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public RedEnvolope findById(int id) {
        RedEnvolope bean = get(id);
        return bean;
    }

    @Override
    public Member getLuckyMan(Integer id) {
        String hql = "select bean.receiver from RedEnvolopeReceiver  bean where bean.redEnvolope.id=:redEnvolope_id and bean.amount=(select max(bean1.amount) from RedEnvolopeReceiver bean1 where bean1.redEnvolope.id=:redEnvolope_id)";
        Finder f = Finder.create(hql);
        f.setParam("redEnvolope_id",id);
        List<Member> list = find(f);
        return list.size()==0? null:list.get(0);
    }

    @Override
    public Double getTodaySenderMoney(Integer user_id) {
        String hql = "select sum(bean.total) from RedEnvolope bean where date(bean.sendTime)=date(:curDate) and bean.sender.id=:user_id";
       Double total = (Double) getSession().createQuery(hql).setParameter("curDate", new Date()).setParameter("user_id",user_id).uniqueResult();
        return total;
    }

    @Override
    public Pagination getSendRedEnvolopes(String user_id, String username,String type, String year, String isPublic,String isInvalid,Integer pageNo, Integer pageSize) {
        String hql = "select bean from RedEnvolope bean where 1=1";
        Finder f = Finder.create(hql);

        if(StringUtils.isNotBlank(user_id)){
            f.append(" and bean.sender.id=:user_id");
            f.setParam("user_id",Integer.parseInt(user_id));
        }

        if(StringUtils.isNotBlank(username)){
            f.append(" and bean.sender.username=:username");
            f.setParam("username",username);
        }
        if(StringUtils.isNotBlank(type)){
            f.append(" and bean.type=:type");
            f.setParam("type", BaseRedEnvolope.RedEnvolopeType.valueOf(Integer.parseInt(type)));
        }

        if(StringUtils.isNotBlank(year)){
            f.append(" and year(bean.sendTime)=:sendTime");
            f.setParam("sendTime",Integer.parseInt(year));
        }
       /* else{
            f.setParam("sendTime",Calendar.getInstance().get(Calendar.YEAR));
        }*/

        if(StringUtils.isNotBlank(isPublic)){
            f.append(" and bean.isPublic=:isPublic");
            if(isPublic.equals("1")){
                f.setParam("isPublic",true);
            }
            else{
                f.setParam("isPublic",false);
            }

        }

        if(StringUtils.isNotBlank(isInvalid)){
            f.append(" and bean.isInvalid=:isInvalid");
            if("1".equals(isInvalid)){
                f.setParam("isInvalid",true);
            }
            else{
                f.setParam("isInvalid",false);
            }
        }

        f.append(" order by bean.sendTime desc");
        return find(f,pageNo,pageSize);
    }

    @Override
    public Pagination getReceivedEnvolopes(String user_id, String year, Integer pageNo, Integer pageSize) {
        String hql = "select bean from RedEnvolope bean left join bean.receiverList as redEnvolopeReceiver where redEnvolopeReceiver.receiver.id =:user_id";
        Finder f=  Finder.create(hql);
        f.setParam("user_id",Integer.parseInt(user_id));


        if(StringUtils.isNotBlank(year)){
            f.append(" and year(redEnvolopeReceiver.receiveTime)=:receiveTime");
            f.setParam("receiveTime",Integer.parseInt(year));
        }
       /* else{
            f.setParam("receiveTime",Calendar.getInstance().get(Calendar.YEAR));
        }*/



        f.append(" order by redEnvolopeReceiver.receiveTime desc");
        return find(f,pageNo,pageSize);
    }

    @Override
    public List<Object[]> getTop3RichMen(String type) {
        StringBuffer hql = new StringBuffer("select bean.sender,count(*) as count1,sum(total*1.0) as total1 " +
                     "from RedEnvolope bean  " +
                     "group by bean.sender.id  "
                    ) ;
        //按次数排名
        if(type.equals("0")){
            hql.append(" order by count1 desc");
        }
        //按金额排名
        else if(type.equals("1")){
            hql.append(" order by total1 desc");
        }
//        hql.append(" limit 0, 3");

        List<Object[]> list = getSession().createQuery(hql.toString()).setMaxResults(3).list();

        return list;
    }

    @Override
    public List<Object[]> getLuckyMen() {
        StringBuffer hql = new StringBuffer(
                "select bean.luckyMan,count(*) as count1 " +
                "from RedEnvolope bean  where bean.type="+ BaseRedEnvolope.RedEnvolopeType.random.ordinal()+" and bean.luckyMan!=null "+
                "group by bean.sender.id  "
        ) ;
        hql.append(" order by count1 desc");

        List<Object[]> list = getSession().createQuery(hql.toString()).setMaxResults(3).list();

        return list;
    }

    @Override
    public List<Object[]> getSexInfo() {
        String hql = "select bean.sender.gender,count(*) from RedEnvolope bean group by bean.sender.gender";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }

    @Override
    public List<Object[]> getTypeInfo() {
        String hql = "select bean.type,count(*) from RedEnvolope bean group by bean.type";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }

    @Override
    public List<Object[]> getTimeInfo() {
        String hql = "select DATE_FORMAT(bean.sendTime,'%Y-%m-%d'),count(*) from RedEnvolope bean group by DATE_FORMAT(bean.sendTime,'%Y-%m-%d')";
        List<Object[]> list = getSession().createQuery(hql.toString()).list();

        return list;
    }


}
