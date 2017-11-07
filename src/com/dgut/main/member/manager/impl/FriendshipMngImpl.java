package com.dgut.main.member.manager.impl;

import com.dgut.common.chinese.ChineseToPinYin;
import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.FriendshipDao;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseApplyFriend;
import com.dgut.main.member.entity.base.BaseFriendship;
import com.dgut.main.member.manager.FriendshipMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by PUNK on 2017/1/29.
 */
@Service
@Transactional
public class FriendshipMngImpl implements FriendshipMng {

    @Autowired
    private FriendshipDao dao;

    @Override
    public Friendship save(Member member, Member friend, String alias) {
        Friendship friendship = new Friendship();
        friendship.setAlias(alias);
        friendship.setFriend(friend);
        friendship.setFriendship_status(Friendship.Friendship_status.IN_NORMAL);
        friendship.setOwner(member);
        friendship.setChinese(ChineseToPinYin.getFirstSpell(alias));
        friendship.setRegisterTime(new Date());

        return dao.save(friendship);
    }

    @Override
    public Friendship findById(int id) {
        return dao.findById(id);
    }

    @Override
    public void putIntoBlacklist(Friendship friendship) {
        friendship.setFriendship_status(BaseFriendship.Friendship_status.IN_BLACKLIST);
        update(friendship);
    }

    @Override
    public List<Friendship> getFriendshipsByUser(Member member,List friendNames) {
        // TODO Auto-generated method stub
        return dao.getFriendshipsByUser(member,friendNames);
    }

    @Override
    public void removeFromBlacklist(Friendship friendship) {
        friendship.setFriendship_status(Friendship.Friendship_status.IN_NORMAL);
        update(friendship);
    }

    @Override
    public void removerFriendship(Friendship friendship) {
        dao.removeFriendship(friendship);
    }

    @Override
    public Pagination getList(String username, String status, int pageNo, int pageSize) {
        return dao.getList(username,status,pageNo,pageSize);
    }

    @Override
    public void updateFriendship(Friendship bean, String type) {
        if(type.equals("1")){
            bean.setFriendship_status(BaseFriendship.Friendship_status.DELETE);
        }
        else if(type.equals("2")){
            bean.setFriendship_status(BaseFriendship.Friendship_status.IN_BLACKLIST);
        }
        else if(type.equals("3")){
            bean.setFriendship_status(BaseFriendship.Friendship_status.IN_NORMAL);
        }
        update(bean);
    }


    private void update(Friendship friendship) {
        Updater<Friendship> updater = new Updater<Friendship>(friendship);
        dao.updateByUpdater(updater);

    }
}
