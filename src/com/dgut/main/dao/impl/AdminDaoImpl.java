package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.Finder;
import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.common.page.Pagination;
import com.dgut.main.dao.AdminDao;
import com.dgut.main.entity.Admin;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by PUNK on 2017/1/15.
 */
@Repository
public class AdminDaoImpl extends HibernateBaseDao<Admin,Integer> implements AdminDao {
    @Override
    protected Class<Admin> getEntityClass() {
        return Admin.class;
    }



    @Override
    public Pagination getPage(String username, Integer roleId, Boolean disabled, Boolean isSuper, int pageNo, int pageSize) {
        Finder f = Finder.create("select bean from Admin bean  where 1=1");
        if (!StringUtils.isBlank(username)) {
            f.append(" and bean.username like :username or bean.realname like :realname");
            f.setParam("username", "%" + username + "%");
            f.setParam("realname", "%" + username + "%");
        }

        if (disabled != null) {
            f.append(" and bean.disabled=:disabled");
            f.setParam("disabled", disabled);
        }
        if (isSuper != null) {
            f.append(" and bean.isSuper=:admin");
            f.setParam("admin", isSuper);
        }

        if (roleId != null) {
            f.append(" and bean.role.id =:role");
            f.setParam("role", roleId);
        }
        f.append(" order by bean.id desc");
        return find(f, pageNo, pageSize);
    }


    @Override
    public Admin findByUsername(String username) {
        return findUniqueByProperty("username", username);
    }

    @Override
    public Admin findById(Integer uid) {
        Admin admin = get(uid);
        return admin;
    }

    @Override
    public Admin save(Admin bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public void deleteRole(Integer id) {
        getSession().createQuery("update com.dgut.main" +
                ".entity.Admin as bean set bean.role.id=null where bean.role.id=:roleId").setInteger("roleId",id).executeUpdate();

    }
}
