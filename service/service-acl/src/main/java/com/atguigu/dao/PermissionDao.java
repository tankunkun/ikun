package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionDao extends BaseDao<Permission> {

    List<Permission> findAll();

    List<Permission> findPermissionListByAdminId(Long adminId);

    List<String> findCodeListByAdminId(Long id);

    List<String> findAllCode();

}
