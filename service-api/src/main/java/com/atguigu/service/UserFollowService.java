package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;

public interface UserFollowService extends BaseService<UserFollow> {
    /**
     * 关注房源
     * @param userId
     * @param houseId
     */
    Boolean follow(Long userId, Long houseId);

    boolean isFollow(Long userInfoId, Long houseId);
}
