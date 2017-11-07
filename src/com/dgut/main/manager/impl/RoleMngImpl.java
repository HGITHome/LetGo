package com.dgut.main.manager.impl;

import com.dgut.common.hibernate4.Updater;
import com.dgut.main.dao.AdminDao;
import com.dgut.main.dao.RoleDao;
import com.dgut.main.entity.Role;
import com.dgut.main.manager.RoleMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by PUNK on 2017/1/18.
 */
@Service
@Transactional
public class RoleMngImpl implements RoleMng {



    @Override
    public List<Role> getList() {
        return roleDao.getAllRole();
    }

    @Override
    public Role save(Role bean, Set<String> perms) {
        bean.setPermissions(perms);
        roleDao.save(bean);
        return bean;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleDao.findByRoleName(roleName);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public Role deleteById(Integer id) {
        adminDao.deleteRole(id);
        return roleDao.deleteById(id);
    }

    @Override
    public Role[] deleteByIds(Integer[] ids) {
        Role[] beans = new Role[ids.length];
        for (int i = 0, len = ids.length; i < len; i++) {

            beans[i] = deleteById(ids[i]);
        }
        return beans;
    }

    @Override
    public Role update(Role bean, Set<String> permissions) {
        Updater<Role> updater = new Updater<Role>(bean);
        bean = roleDao.updateByUpdater(updater);
        bean.setPermissions(permissions);
        return bean;
    }

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AdminDao adminDao;
}
