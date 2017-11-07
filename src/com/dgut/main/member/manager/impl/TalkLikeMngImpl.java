package com.dgut.main.member.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.main.member.dao.TalkLikeDao;
import com.dgut.main.member.entity.TalkLike;
import com.dgut.main.member.manager.TalkLikeMng;
import com.dgut.main.util.InfoConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
@Service
public class TalkLikeMngImpl implements TalkLikeMng {

    @Autowired
    private TalkLikeDao dao;

    @Override
    public TalkLike findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public TalkLike disabledTapLike(TalkLike talkLike) {
        talkLike.setDisabled(true);
        return update(talkLike);
    }

    @Override
    public TalkLike update(TalkLike talkLike) {
        Updater updater = new Updater<TalkLike>(talkLike);
        dao.updateByUpdater(updater);
        return talkLike;
    }

    @Override
    public List<Object[]> getSexInfo() {
        List<Object[]> list=dao.getSexInfo();

        list = InfoConvertor.convertSexInfo(list);
        return list;
    }
}
