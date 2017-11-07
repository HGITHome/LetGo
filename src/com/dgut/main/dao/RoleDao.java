package com.dgut.main.dao;

import com.dgut.common.hibernate4.Updater;
import com.dgut.main.entity.Role;

import java.util.List;

/**
 * Created by PUNK on 2017/1/14.
 */
public interface RoleDao {


    public List<Role> getAllRole();

    Role findById(Integer id);

    Role save(Role bean);

    Role findByRoleName(String roleName);

    Role deleteById(Integer id);

    public Role updateByUpdater(Updater<Role> updater);


}
