package com.dgut.main.member.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.member.entity.Member;
import com.dgut.main.member.entity.RedEnvolope;

import java.util.List;

/**
 * Created by PUNK on 2017/3/31.
 */
public interface RedEnvolopeDao {
    RedEnvolope save(RedEnvolope bean);

    RedEnvolope findById(int id);

    RedEnvolope updateByUpdater(Updater<RedEnvolope> redEnvolopeUpdater);

    Member getLuckyMan(Integer id);

    Double getTodaySenderMoney(Integer user_id);

    Pagination getSendRedEnvolopes(String user_id,String username,String type,String year,String isPublic,String isInvalid,Integer pageNo,Integer pageSize);

    Pagination getReceivedEnvolopes(String user_id,String year,Integer pageNo,Integer pageSize);


    List<Object[]> getTop3RichMen(String type);

    List<Object[]> getLuckyMen();

    List<Object[]> getSexInfo();

    List<Object[]> getTypeInfo();

    List<Object[]> getTimeInfo();
}
