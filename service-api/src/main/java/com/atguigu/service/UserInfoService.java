package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;
import com.atguigu.entity.UserInfo;

import java.util.List;



public interface UserInfoService extends BaseService<UserInfo> {

    UserInfo getByPhone(String phone);
}