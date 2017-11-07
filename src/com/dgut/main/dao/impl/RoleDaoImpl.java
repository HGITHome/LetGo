package com.dgut.main.dao.impl;

import com.dgut.common.hibernate4.HibernateBaseDao;
import com.dgut.main.dao.RoleDao;
import com.dgut.main.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by PUNK on 2017/1/14.
 */
@Repository
public class RoleDaoImpl  extends HibernateBaseDao<Role,Integer> implements RoleDao{
    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    /**
     * 得到全部的角色信息
     * @return
     */
    @Override
    public List<Role> getAllRole() {
        String hql="FROM Role bean WHERE 1=1";
        hql+="ORDER BY bean.priority desc";
        return find(hql);
    }

    /**
     * 得到角色信息
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
        Role role =get(id);
        return role;
    }

    @Override
    public Role save(Role bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return findUniqueByProperty("roleName", roleName);
    }

    @Override
    public Role deleteById(Integer id) {

        Role role = get(id);
        if(role!=null){
            role.setPermissions(null);
            getSession().delete(role);
        }
        return role;

    }


}
