package com.dgut.main.manager.assist;

import java.util.Map;

/**
 * Created by PUNK on 2017/5/6.
 */
public interface DataSummaryMng {

    public Map<String,Object> getRedEnvolopeData();

    public Map<String,Object> getRedEnvolopeTimeInfo();

    Map<String,Object> getTalkData();

    Map<String,Object> getTalkTimeInfo();
}
