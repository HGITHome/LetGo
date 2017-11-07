package com.dgut.main.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Accusation;

/**
 * Created by PUNK on 2017/3/21.
 */
public interface AccusationDao {

    Accusation save(Accusation accusation);

    Pagination getList(String reporter, String respondent, Integer type, String status,int pageNo, int pageSize);

    Accusation findById(int id);

    Accusation updateByUpdater(Updater<Accusation> updater);
}
