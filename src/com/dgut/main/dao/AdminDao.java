package com.dgut.main.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.common.page.Pagination;
import com.dgut.main.entity.Admin;

/**
 * Created by PUNK on 2017/1/14.
 */

public interface AdminDao {




    Pagination getPage(String username, Integer roleId, Boolean disabled, Boolean isSuper, int pageNo, int pageSize);

    Admin findByUsername(String username);

    Admin findById(Integer uid);

    Admin save(Admin bean);

    Admin updateByUpdater(Updater<Admin> updater);

    void deleteRole(Integer id);
}
