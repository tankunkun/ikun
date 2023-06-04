package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.RolePermission;
import java.util.List;


public interface RolePermissionDao extends BaseDao<RolePermission> {
    //根据角色id查询许可权限id集合
    List<Long> findPermissionIdListByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
}
