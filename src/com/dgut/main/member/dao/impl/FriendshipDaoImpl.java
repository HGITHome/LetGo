package com.dgut.main.member.dao.impl;

import com.dgut.common.easemob.comm.wrapper.QueryWrapper;
import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.FriendshipDao;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseFriendship;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Created by PUNK on 2017/1/29.
 */
@Repository
public class FriendshipDaoImpl extends HibernateBaseDao<Friendship,Integer> implements FriendshipDao {
    @Override
    protected Class<Friendship> getEntityClass() {
        return Friendship.class;
    }

    @Override
    public Friendship save(Friendship friendship) {
        getSession().save(friendship);
        return friendship;
    }

    @Override
    public Friendship findById(int id) {
        return get(id);
    }

    @Override
    public List<Friendship> getFriendshipsByUser(Member member, List friendNames) {
        Finder f = Finder
                .create("SELECT bean FROM Friendship bean WHERE bean.owner.id=:user_id AND bean.friendship_status!= :status");
        f.setParam("user_id", member.getId());
        f.setParam("status", BaseFriendship.Friendship_status. DELETE);
        // 和环信的一致
        f.append(" and bean.friend.easemob_name in (:friendNames)");
        f.setParamList("friendNames", friendNames);

        // 账户可用
        f.append(" and bean.friend.disabled=false");
        // 按字母排序
        f.append(" order by bean.chinese asc,alias desc");

        return find(f);
    }

    @Override
    public List<Friendship> getFriendshipAmongUsers(Member owner, Member friend) {
        String hql = "select bean from Friendship bean where  bean.friendship_status!= :status and  (bean.owner.id=:owner and bean.friend.id=:friend) or (bean.owner.id=:friend and bean.friend.id=:owner) and bean.friend.disabled=false and bean.owner.disabled=false";

        Finder f = Finder.create(hql);

        f.setParam("status", BaseFriendship.Friendship_status. DELETE);
        f.setParam("owner",owner.getId());
        f.setParam("friend",friend.getId());
        return find(f);
    }

    @Override
    public void removeFriendship(Friendship friendship) {
        String hql ="update Friendship bean set bean.friendship_status= :status where (bean.owner.id=:userid and bean.friend.id=:friendId) or (bean.owner.id=:friendId and bean.friend.id=:userid)";
        getSession().createQuery(hql).setParameter("status",Friendship.Friendship_status.DELETE).setParameter("userid",friendship.getOwner().getId()).setParameter("friendId",friendship.getFriend().getId()).executeUpdate();

    }

    @Override
    public Pagination getList(String username, String status, int pageNo, int pageSize) {
        String sql ="select bean from Friendship bean where 1=1";
        Finder f = Finder.create(sql);
        if(StringUtils.isNotBlank(username)){
            f.append(" and bean.owner.username=:username");
            f.setParam("username",username);
        }
        if(StringUtils.isNotBlank(status)){
            f.append(" and bean.friendship_status =:status");
            f.setParam("status", BaseFriendship.Friendship_status.valueOf(status));
        }
        f.append(" order by bean.registerTime desc");
        return find(f,pageNo,pageSize);
    }
}
