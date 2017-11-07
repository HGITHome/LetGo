package com.dgut.main.member.manager;

import com.dgut.main.member.entity.TalkLike;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
public interface TalkLikeMng {
    
    TalkLike findById(Integer id);

    TalkLike disabledTapLike(TalkLike talkLike);

    TalkLike update(TalkLike talkLike);

    List<Object[]> getSexInfo();
}
