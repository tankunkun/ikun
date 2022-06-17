package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Role;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RoleDao extends BaseDao<Role> {
    //模块特有的方法，声明到子接口中，共用的方法声明在父接口中
    List<Role> findAll();

    //通用的增伤改查，分页封装到了基础BaseDao
}

