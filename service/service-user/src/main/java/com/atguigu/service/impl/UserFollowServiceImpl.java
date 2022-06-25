package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Date 2022/6/25 10:29
 * @Author by:Plisetsky
 */
@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    UserFollowDao userFollowDao;

    @Override
    public BaseDao<UserFollow> getEntityDao() {
        return userFollowDao;
    }

    //关注房源
    @Override
    public Boolean follow(Long userId, Long houseId) {
        Integer count = userFollowDao.countByUserIdAndHouserId(userId, houseId);
        //如果未关注
        if(count.intValue() == 0) {
            UserFollow userFollow = new UserFollow();
            userFollow.setUserId(userId);
            userFollow.setHouseId(houseId);
            userFollowDao.insert(userFollow);
        }
        return true;
    }
    //房源是否关注
    @Override
    public boolean isFollow(Long userInfoId, Long houseId) {
        Integer count = userFollowDao.countByUserIdAndHouserId(userInfoId, houseId);
        if(count.intValue()>0){
            return true;
        }
        return false;
    }


}
