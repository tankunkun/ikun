package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.DictService;
import com.atguigu.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @Date 2022/6/21 14:24
 * @Author by:Plisetsky
 */

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {


    @Autowired
    HouseDao houseDao;

    //房源服务接口和我们的数据字典接口在一个服务中，直接注入即可，无需远程调用
    @Autowired
    DictDao dictDao;
    //@Reference
    //DictService dictService;




    @Override
    public BaseDao<House> getEntityDao() {
        return houseDao;
    }

    //发布
    @Override
    public void publish(Long id, Integer status) {
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseDao.update(house);
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.selectById(id);
        if(house!=null){

            //户型：
            String houseTypeName = dictDao.getNameById(house.getHouseTypeId());
            //楼层
            String floorName = dictDao.getNameById(house.getFloorId());
            //建筑结构：
            String buildStructureName = dictDao.getNameById(house.getBuildStructureId());
            //朝向：
            String directionName = dictDao.getNameById(house.getDirectionId());
            //装修情况：
            String decorationName = dictDao.getNameById(house.getDecorationId());
            //房屋用途：
            String houseUseName = dictDao.getNameById(house.getHouseUseId());
            house.setHouseTypeName(houseTypeName);
            house.setFloorName(floorName);
            house.setBuildStructureName(buildStructureName);
            house.setDirectionName(directionName);
            house.setDecorationName(decorationName);
            house.setHouseUseName(houseUseName);
        }
        return house;
    }
}
