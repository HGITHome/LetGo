package com.dgut.main.member.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.TalkCommentDao;
import com.dgut.main.member.entity.TalkComment;
import com.dgut.main.member.manager.TalkCommentMng;
import com.dgut.main.util.InfoConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PUNK on 2017/2/2.
 */
@Service
public class TalkCommentMngImpl implements TalkCommentMng {

    @Autowired
    private TalkCommentDao dao;

    @Override
    public TalkComment findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public void disabledComment(TalkComment comment) {
        comment.setDisabled(true);
        update(comment);
    }

    @Override
    public void setCommentValid(TalkComment comment) {
        comment.setDisabled(false);
        update(comment);
    }

    @Override
    public Pagination getComments(String username, String status, int pageNo, int pageSize) {
        return dao.getList(username,status,pageNo,pageSize);
    }

    @Override
    public List<Object[]> getSexInfo() {
        List<Object[]> list=dao.getSexInfo();

        list = InfoConvertor.convertSexInfo(list);
        return list;
    }

    private void update(TalkComment comment) {
        Updater<TalkComment> updater = new Updater<TalkComment>(comment);
        dao.updateByUpdater(updater);
    }
}
