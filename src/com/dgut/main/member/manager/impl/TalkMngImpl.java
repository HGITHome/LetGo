package com.dgut.main.member.manager.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.dao.TalkDao;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Photo;
import com.dgut.main.member.entity.Talk;
import com.dgut.main.member.entity.TalkLike;
import com.dgut.main.member.manager.TalkMng;
import com.dgut.main.util.InfoConvertor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by PUNK on 2017/1/31.
 */
@Service
@Transactional
public class TalkMngImpl implements TalkMng {

    @Autowired
    private TalkDao dao;

    @Override
    public Talk findById(int id) {
        return dao.findById(id);

    }

    @Override
    public Talk update(Talk talk) {
        Updater<Talk> updater = new Updater<Talk>(talk);
        return dao.updateByUpdater(updater);
    }

    @Override
    public Talk save(String content, String video_url, List<Photo> photos, Member member) {
        Talk talk=new Talk();
        talk.setPublisher(member);
        talk.setPhoto_urls(photos);
        talk.setPublish_time(new Date());
        talk.setVideo_url(video_url);
        talk.setContent(content);
        talk.setDisabled(false);
        save(talk);
        return  talk;
    }

    @Override
    public Talk disabledTalk(Talk talk) {
        talk.setDisabled(true);
        return update(talk);
    }

    @Override
    public Pagination findTalksByUser(Member member, int pageNo, int pageSize) {
        return dao.findTalkByUser(member,pageNo,pageSize);
    }

    @Override
    public Pagination getTalkList(Member member, int pageNo, int pageSize) {

        return dao.getTalkList(member, pageNo,pageSize);
    }

    @Override
    public Pagination getTalkList(String username, String status, int pageNo, int pageSize) {
        return dao.getTalkList(username,status,pageNo,pageSize);
    }

    @Override
    public List<Member> getTop3ShareMen() {
        List<Object[]> resultList = dao.getTop3ShareMen();

        List<Member> memberList = new ArrayList<>(resultList.size());
        Member m =null;
        Object[] result = null;

        for(int i=0;i<resultList.size();i++ ){
            result = resultList.get(i);
            m =(Member) result[0];
            m.setCount((Long) result[1]);
            memberList.add(m);
        }



        return memberList;
    }

    @Override
    public List<Member> getTop3TapLikeMen() {
        List<Object[]> resultList = dao.getTop3TapLikeMen();

        List<Member> memberList = new ArrayList<>(resultList.size());
        Member m =null;
        Object[] result = null;

        for(int i=0;i<resultList.size();i++ ){
            result = resultList.get(i);
            m =(Member) result[0];
            m.setCount((Long) result[1]);
            memberList.add(m);
        }



        return memberList;
    }

    @Override
    public List<Object[]> getSexInfo() {
        List<Object[]> list=dao.getSexInfo();

        list = InfoConvertor.convertSexInfo(list);
        return list;
    }

    @Override
    public Map<String, Object> getTimeInfo() {
        List<Object[]> list = dao.getTimeInfo();
        List<String> timeList = new ArrayList(list.size());
        List<String > scoreList = new ArrayList(list.size());
        Map<String,Object> resultMap = new HashMap<>();
        for(Object[] objs : list){
            timeList.add(String.valueOf(objs[0]));
            scoreList.add(String.valueOf(objs[1]));
        }
        resultMap.put("timeList",timeList);
        resultMap.put("summaryList",scoreList);

        return resultMap;
    }


    private void save(Talk talk) {
        dao.save(talk);
    }
}
