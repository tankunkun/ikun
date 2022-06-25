package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.AdminDao;
import com.atguigu.dao.AdminRoleDao;
import com.atguigu.dao.RoleDao;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Date 2022/6/15 14:33
 * @Author by:Plisetsky
 */

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired  //Spring框架提供的依赖注入  先进性byType再进行byName
    //@Resource   //jdk提供的依赖注入注解   先进行byName在进行byType
    RoleDao roleDao;

    @Autowired
    AdminRoleDao adminRoleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    @Override
    public BaseDao<Role> getEntityDao() {
        return roleDao;
    }
    //查询角色分配表
    @Override
    public Map getSelectMapByAdminId(Long id) {
        Map map = new HashMap<>();
        //存放未分配角色
        List noAssignRoleList = new ArrayList<>();
        //存放已分配角色
        List assignRoleList = new ArrayList<>();
        //1.查询所有的角色
        List<Role> allList = roleDao.findAll();

        //2.查询该用户所拥有的角色的id集合
        List<Long> roleIdList = adminRoleDao.findRoleIdListByAdminId(id);


        //3.将所有的角色划分到两个集合中返回
        for (Role role : allList) {
            Long roleId = role.getId();
            //迭代角色的id如果在已拥有角色集合中存在，说明该角色已分配
            if(roleIdList.contains(roleId)){
                assignRoleList.add(role);
            }else {
                noAssignRoleList.add(role);
            }
        }

        map.put("noAssignRoleList",noAssignRoleList);
        map.put("assignRoleList",assignRoleList);
        return map;
    }
    //更新角色分配表
    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        //1.先删除已有角色
        adminRoleDao.deleteAdminRoleRelationship(adminId);
        //2.重新分配新的角色
        if(roleIds!=null && roleIds.length>0){
            List list = new ArrayList();
            // “1,2,3,4,"通过逗号分解后，数据中可能出现空值，需要进行逻辑判断
            for (Long roleId : roleIds) {
                //如果为空跳过当次循环
                if(StringUtils.isEmpty(roleId)){
                    continue;
                }
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);
                list.add(adminRole);
            }
            //批量插入
            adminRoleDao.insertBatch(list);
        }
    }
}
