package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.UserFollow;
import org.apache.ibatis.annotations.Param;

/**
 * @Date 2022/6/25 10:32
 * @Author by:Plisetsky
 */
public interface UserFollowDao extends BaseDao<UserFollow> {
    //注意Param绑定参数
    Integer countByUserIdAndHouserId(@Param("userId")Long userId, @Param("houseId")Long houseId);
}
