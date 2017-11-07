package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.Photo;
import com.dgut.main.member.entity.Talk;

import java.util.List;
import java.util.Map;

/**
 * Created by PUNK on 2017/1/31.
 */
public interface TalkMng {
    Talk findById(int id);

    Talk update(Talk talk);

    Talk save(String content, String video_url, List<Photo> talk_photos, Member member);

    Talk disabledTalk(Talk talk);

    Pagination findTalksByUser(Member member, int pageNo, int pageSize);

    Pagination getTalkList(Member member, int pageNo, int pageSize);

    Pagination getTalkList(String username,String status,int pageNo,int pageSize);


    List<Member> getTop3ShareMen();

    List<Member> getTop3TapLikeMen();

    List<Object[]> getSexInfo();

    Map<String,Object> getTimeInfo();
}
