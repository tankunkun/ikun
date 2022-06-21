package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseService;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.CommunityDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/21 9:36
 * @Author by:Plisetsky
 */
@Service( interfaceClass = CommunityService.class)
@Transactional
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    CommunityDao communityDao;

    @Autowired
    DictDao dictDao;

    @Override
    public BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    //重写分页查询，要显示区域名称而不是id
    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

        PageHelper.startPage(pageNum, pageSize);

        Page<Community> page = communityDao.findPage(filters);
        //getNameById,将通过id获取name
        for (Community community : page) {
            community.setAreaName(dictDao.getNameById(community.getAreaId()));
            community.setPlateName(dictDao.getNameById(community.getPlateId()));
        }
        return new PageInfo<Community>(page, 5);
    }

    @Override
    public List<Community> findAll() {
        return communityDao.findAll();
    }

    //重写
    @Override
    public Community getById(Serializable id) {
        Community community = communityDao.selectById(id);
        if(community!=null){
            community.setAreaName(dictDao.getNameById(community.getAreaId()));
            community.setPlateName(dictDao.getNameById(community.getPlateId()));
        }
        return super.getById(id);
    }
}
