package com.dgut.main.member.manager;

import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;
import com.dgut.main.member.entity.RedEnvolopeReceiver;

import java.util.List;
import java.util.Map;

/*
 * Created by PUNK on 2017/3/31.
 * */


public interface RedEnvolopeMng {

    RedEnvolope save(Member member, String type, String num, String content,Double total, Double balance, String users, String group_id);


    RedEnvolope findById(int id);

    void getRedEnvolopeMoney(RedEnvolopeReceiver receiver);

    Pagination getRedEnvolopes(String user_id, String username,String type, String isGet, String year,String isPublic,String isInvalid, Integer pageNo, Integer pageSize);

    RedEnvolope update(RedEnvolope bean);

    List<Member> getTop3RichMen(String type);

    List<Member> getLuckyMen();

    List<Object[]> getSexInfo();

    List<Object[]> getTypeInfo();

    Map<String,Object> getTimeInfo();
}
