package com.atguigu.service.impl;

import com.atguigu.dao.RoleDao;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @Date 2022/6/15 14:33
 * @Author by:Plisetsky
 */

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired  //Spring框架提供的依赖注入  先进性byType再进行byName
    //@Resource   //jdk提供的依赖注入注解   先进行byName在进行byType
    RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Integer insert(Role role) {
        return roleDao.insert(role);
    }
}
