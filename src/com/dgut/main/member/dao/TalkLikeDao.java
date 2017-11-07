package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.member.entity.TalkLike;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
public interface TalkLikeDao {
    TalkLike findById(Integer id);

    TalkLike updateByUpdater(Updater<TalkLike> updater);

    List<Object[]> getSexInfo();
}
