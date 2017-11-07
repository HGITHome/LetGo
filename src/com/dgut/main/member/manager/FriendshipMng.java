package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Friendship;
import com.dgut.main.member.entity.Member;

import java.util.List;

/**
 * Created by PUNK on 2017/1/29.
 */
public interface FriendshipMng {

    Friendship save(Member member, Member friend, String alias);

    Friendship findById(int i);

    void putIntoBlacklist(Friendship friendship);

    List<Friendship> getFriendshipsByUser(Member member, List list);

    void removeFromBlacklist(Friendship friendship);

    void removerFriendship(Friendship friendship);

    Pagination getList(String username, String status, int pageNo, int pageSize);

    void updateFriendship(Friendship bean, String type);
}
