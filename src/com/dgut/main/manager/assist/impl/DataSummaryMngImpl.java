package com.dgut.main.manager.assist.impl;

import com.dgut.main.manager.assist.DataSummaryMng;
import com.dgut.main.member.manager.RedEnvolopeMng;
import com.dgut.main.member.manager.TalkCommentMng;
import com.dgut.main.member.manager.TalkLikeMng;
import com.dgut.main.member.manager.TalkMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PUNK on 2017/5/6.
 */
@Service
public class DataSummaryMngImpl implements DataSummaryMng{
    
    @Autowired
    private RedEnvolopeMng redEnvolopeMng;
    
    @Autowired
    private TalkMng talkMng;

    @Autowired
    private TalkCommentMng commentMng;

    @Autowired
    private TalkLikeMng talkLikeMng;
    
    @Override
    public Map<String, Object> getRedEnvolopeData() {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("sexDistribution",redEnvolopeMng.getSexInfo());
        resultMap.put("typeDistribution",redEnvolopeMng.getTypeInfo());
//        resultMap.put("timeDistribution",redEnvolopeMng.getTimeInfo());
        return resultMap;
    }

    @Override
    public Map<String, Object> getRedEnvolopeTimeInfo() {

        return redEnvolopeMng.getTimeInfo();
    }

    @Override
    public Map<String, Object> getTalkData() {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("talkDistribution",talkMng.getSexInfo());
        resultMap.put("commentDistribution",commentMng.getSexInfo());
        resultMap.put("tapLikeDistribution",talkLikeMng.getSexInfo());
        return resultMap;
    }

    @Override
    public Map<String, Object> getTalkTimeInfo() {
        return talkMng.getTimeInfo();
    }


}
