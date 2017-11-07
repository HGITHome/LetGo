package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;

import java.util.List;

/**
 * Created by PUNK on 2017/1/29.
 */
public interface FriendshipDao {
    Friendship save(Friendship friendship);

    Friendship findById(int id);

    Friendship updateByUpdater(Updater<Friendship> updater);

    List<Friendship> getFriendshipsByUser(Member member, List friendNames);

    List<Friendship> getFriendshipAmongUsers(Member owner,Member friend);

    void removeFriendship(Friendship friendship);

    Pagination getList(String username, String status, int pageNo, int pageSize);
}
