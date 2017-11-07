package com.dgut.main.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.AccusationCategory;

/**
 * Created by PUNK on 2017/3/21.
 */
public interface AccusationCategoryDao {
    Pagination getCategory(String queryName, int pageNo, int pageSize);

    AccusationCategory save(AccusationCategory bean);

    AccusationCategory fingById(int id);

    AccusationCategory updateByUpdater(Updater<AccusationCategory> updater);

    AccusationCategory findByName(String categoryName);

    AccusationCategory deleteCategory(Integer id);
}
