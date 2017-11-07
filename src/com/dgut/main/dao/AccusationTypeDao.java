package com.dgut.main.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.AccusationType;

import java.util.List;

/**
 * Created by PUNK on 2017/3/23.
 */
public interface AccusationTypeDao {
    Pagination getList(String categoryId,String typeName, int pageNo, int pageSize);

    List findByTypeByName(String categoryId, String name);

    AccusationType save(AccusationType type);

    AccusationType findById(Integer id);

    AccusationType updateByUpdater(Updater<AccusationType> updater);

    AccusationType deleteType(int i);
}
