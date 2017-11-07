package com.dgut.main.dao;

import com.dgut.common.page.Pagination;
import com.dgut.main.entity.base.BaseUserLog;

/**
 * Created by PUNK on 2017/1/18.
 */
public interface UserLogDao {

    public BaseUserLog save(BaseUserLog bean);

    Pagination getPage(int operating, String queryUsername, String queryTitle, String queryIp, int pageNo, int pageSize);
}
