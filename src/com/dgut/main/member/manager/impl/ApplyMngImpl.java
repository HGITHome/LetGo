package com.dgut.main.member.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.ApplyDao;
import com.dgut.main.member.entity.ApplyFriend;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.base.BaseApplyFriend;
import com.dgut.main.member.manager.ApplyMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by PUNK on 2017/1/28.
 */
@Service
@Transactional
public class ApplyMngImpl implements ApplyMng{
    @Autowired
    private ApplyDao dao;

    @Override
    public void saveOrUpdate(Member publisher, Member receiver, String apply_reason) {
        ApplyFriend af=dao.getPreviousUnhandledApply(publisher,receiver);
        if(af!=null){
            af.setApply_reason(apply_reason);
            af.setApplyTime(new Date());
            af=update(af);
        }
        else{
            af=save(publisher,receiver,apply_reason);
        }

    }

    @Override
    public ApplyFriend findById(int id) {
        return dao.findById(id);
    }

    private ApplyFriend save(Member publisher, Member receiver, String apply_reason) {
        ApplyFriend bean = new ApplyFriend();
        bean.setPublisher(publisher);
        bean.setReceiver(receiver);
        bean.setApply_reason(apply_reason);
        bean.setId(ApplyFriend.ApplyFlag.UNHANDLED.ordinal());
        bean.setIsRead(false);
        bean.setHandle_flag(BaseApplyFriend.ApplyFlag.UNHANDLED);
        bean.setApplyTime(new Date());
        return dao.save(bean);
    }

    @Override
    public ApplyFriend update(ApplyFriend bean) {
        Updater<ApplyFriend> updater =new Updater<ApplyFriend>(bean);
        ApplyFriend apply = dao.updateByUpdater(updater);

        return apply;
    }

    @Override
    public Integer countUnhandleApplication(Member member) {
        return dao.countUnhandleApplication(member);
    }

    @Override
    public Pagination findByUser(Integer id, int pageNo, int pageSize) {
        return dao.findByUser(id,pageNo,pageSize);
    }

    @Override
    public Pagination getApplyList(String username, String status, Integer pageNo, Integer pageSize) {
        return dao.getApplyList(username,status,pageNo,pageSize);
    }
}
