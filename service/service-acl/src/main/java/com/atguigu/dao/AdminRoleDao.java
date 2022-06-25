package com.atguigu.dao;

import com.atguigu.base.BaseDao;

import java.util.List;

/**
 * @Date 2022/6/25 16:29
 * @Author by:Plisetsky
 */
public interface AdminRoleDao extends BaseDao<AdminRoleDao> {
    List<Long> findRoleIdListByAdminId(Long adminId);

    void deleteAdminRoleRelationship(Long adminId);



}
