package com.atguigu.service.impl;

import com.atguigu.dao.RoleDao;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/15 14:33
 * @Author by:Plisetsky
 */

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired  //Spring框架提供的依赖注入  先进性byType再进行byName
    //@Resource   //jdk提供的依赖注入注解   先进行byName在进行byType
    RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Integer insert(Role role) {
        return roleDao.insert(role);
    }

    @Override
    public Role getById(Long id) {
        return roleDao.selectById(id);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public void delete(Long id) {
        roleDao.delete(id);
    }

    //分页
    @Override
    public PageInfo<Role> findPage(Map<String, Object> filters) {
        //使用工具类进行类型装换，设置默认值，解决空指针(访问首页时没有pageNum参数)
        int pageNum = CastUtil.castInt(filters.get("pageNum"),1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"),1);

        //开启分页功能 (注意检查是否进行依赖、mybatis.xml是否配置)
        //将这两个参数，与当前线程(ThreadLocal)进行绑定，传递给dao层
        PageHelper.startPage(pageNum,pageSize);
        // select 语句，会自动追加limit ?,?  (limit startIndex,pageSize)
        // 公式: startIndex = (pageNum-1)*pageSize

        Page<Role> page = roleDao.findPage(filters);  //传递查询条件参数
        return new PageInfo(page,5);   //5: 显示的导航页数
    }
}
