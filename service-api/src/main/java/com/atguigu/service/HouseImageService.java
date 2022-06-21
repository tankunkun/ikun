package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;
import java.util.List;

public interface HouseImageService extends BaseService<HouseImage> {
    //type =1 房源图片 type=2 房产图片
    List<HouseImage> findList(Long houseId, Integer type);
}