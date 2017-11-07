package com.dgut.main.manager;

import com.dgut.main.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * 角色service层
 * Created by PUNK on 2017/1/18.
 */
public interface RoleMng {

    public List<Role> getList();


    Role save(Role bean, Set<String> strings);

    Role findByRoleName(String roleName);

    Role findById(Integer id);

    Role deleteById(Integer id);

    Role[] deleteByIds(Integer[] ids);

    Role update(Role bean, Set<String> permissions);
}
