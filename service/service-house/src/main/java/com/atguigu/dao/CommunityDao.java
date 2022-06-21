package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Community;

import java.util.List;

/**
*@Date 2022/6/21 9:37
*@Author by:Plisetsky
*/
public interface CommunityDao extends BaseDao<Community> {

    List<Community> findAll();
}
