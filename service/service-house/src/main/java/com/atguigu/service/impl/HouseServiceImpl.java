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
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

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


    @Override
    public PageInfo<HouseVo> findListPage(int pageNum, int pageSize, HouseQueryVo houseQueryVo) {
        PageHelper.startPage(pageNum, pageSize);
        Page<HouseVo> page = houseDao.findListPage(houseQueryVo);
        if(page!=null && page.size()>0){
            for(HouseVo houseVo : page) {
                //户型：
                houseVo.setHouseTypeName(dictDao.getNameById(houseVo.getHouseTypeId()));
                //楼层
                houseVo.setFloorName(dictDao.getNameById(houseVo.getFloorId()));
                //朝向：
                houseVo.setDirectionName(dictDao.getNameById(houseVo.getDirectionId()));
            }
        }
        return new PageInfo<HouseVo>(page, 10);
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
