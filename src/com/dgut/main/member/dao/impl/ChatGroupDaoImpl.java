package com.dgut.main.member.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.ChatGroupDao;
import com.dgut.main.member.entity.ChatGroup;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/2/23.
 */
@Repository
public class ChatGroupDaoImpl extends HibernateBaseDao<ChatGroup,Integer> implements ChatGroupDao {
    @Override
    protected Class<ChatGroup> getEntityClass() {
        return ChatGroup.class;
    }

    @Override
    public ChatGroup save(ChatGroup bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public ChatGroup findById(Integer id) {
        ChatGroup group = get(id);
        return group;
    }

    @Override
    public List<ChatGroup> getUserJoinedGroup(String[] groups) {
        Finder f = Finder.create("select distinct bean from ChatGroup bean ");
        f.append(" inner join bean.memberList members ");

        f.append("where  bean.disabled = false and bean.easemob_id in (:ids)  and members.disabled=false" );
        f.setParamList("ids", groups);
        return find(f);
    }

    @Override
    public void removeBatchUserFromGroup(ChatGroup group, Integer[] userIds) {
        String hql = "update GroupMember bean set bean.disabled=true where bean.group.id = :id and     bean.member.id in (:userIds)";
        getSession().createQuery(hql).setParameter("id",group.getId()).setParameterList("userIds",userIds).executeUpdate();
    }

    @Override
    public Pagination getList(String queryUsername, String queryDisabled, int pageNo, int pageSize) {
        String hql = "select bean from ChatGroup bean  where 1=1 ";
//        String hql = "select bean from ChatGroup bean left join bean.memberList members ";
        Finder f = Finder.create(hql);
//        f.append(" where 1=1 and members.disabled=false ");

        if(StringUtils.isNotBlank(queryUsername)){
            f.append( "and bean.owner.username like :username");
            f.setParam("username","%"+queryUsername+"%");
        }
        if(StringUtils.isNotBlank(queryDisabled)){
            f.append(" and bean.disabled = :disabled");

            if(queryDisabled.equals("0")){
                f.setParam("disabled",false);
            }
            else{
                f.setParam("disabled",true);
            }
        }
        f.append(" order by bean.groupName");
        return find(f,pageNo,pageSize);
    }

    @Override
    public Pagination getMemberList(Integer group_id, String queryUsername, String queryDisabled, int pageNo, int pageSize) {
        String hql = "select bean from GroupMember bean where bean.chatGroup.id=:id";
        Finder f= Finder.create(hql);
        f.setParam("id",group_id);
        if(StringUtils.isNotBlank(queryUsername)){
            f.append(" and bean.member.username like :username");
            f.setParam("username","%"+queryUsername+"%");
        }
        if(StringUtils.isNotBlank(queryDisabled)){
            f.append(" and bean.disabled = :queryDisabled");
            if("1".equals(queryDisabled)){
                f.setParam("queryDisabled",true);
            }
            else if("0".equals(queryDisabled)){
                f.setParam("queryDisabled",false);
            }
        }

        return find(f,pageNo,pageSize);
    }
}
