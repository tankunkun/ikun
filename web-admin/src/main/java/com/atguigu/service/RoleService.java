package com.atguigu.service;

import com.atguigu.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface RoleService {//Ctrl + Alt + B 快捷跳转到实现类
    List<Role> findAll();

    Integer insert(Role role);

    Role getById(Long id);

    void update(Role role);

    void delete(Long id);

    PageInfo<Role> findPage(Map<String, Object> filters);
}
