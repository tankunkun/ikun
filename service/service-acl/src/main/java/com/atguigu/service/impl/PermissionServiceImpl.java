package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.PermissionDao;
import com.atguigu.dao.RolePermissionDao;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.service.PermissionService;
import com.atguigu.util.PermissionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/27 9:03
 * @Author by:Plisetsky
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    PermissionDao permissionDao;
    @Autowired
    RolePermissionDao rolePermissionDao;

    @Override
    public BaseDao<Permission> getEntityDao() {
        return permissionDao;
    }

    //查询权限列表(权限树)
    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //1.查询所有的许可(权限)节点
        List <Permission> allPermission = permissionDao.findAll();

        List<Map<String, Object>> zBides = new ArrayList<>();
        //2.哪些节点需要勾选? 查询当前角色所对应的许可
        List<Long> permissionIdList = rolePermissionDao.findPermissionIdListByRoleId(roleId);

        for (Permission permission : allPermission) {
            Map<String,Object> map = new HashMap<>();
            //无需设置 open:true  ，因为页面对整棵树进行了设置，全部展开
            //{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
            map.put("id",permission.getId());
            map.put("pId",permission.getParentId());
            map.put("name",permission.getName());
            //check 表示复选框是否打钩，表示拥有的许可权限回显
            if(permissionIdList.contains(permission.getId())){
                map.put("checked",true);
            }
            zBides.add(map);
        }
        return zBides;
    }

    //更新存储权限
    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        //1.先删除旧关系
        rolePermissionDao.deleteByRoleId(roleId);

        //2.增加一批新的关系数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for(Long permissionId : permissionIds) {
            if(StringUtils.isEmpty(permissionId)){
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setPermissionId(permissionId);
            rolePermission.setRoleId(roleId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionDao.insertBatch(rolePermissionList);
    }

    //动态菜单 用户权限
    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        List<Permission> list = null;
        //如果是系统管理员，默认id=1，直接查询所有权限，无需条件关联查询
        if(adminId.intValue() == 1){
            list = permissionDao.findAll(); //可以不用查询type=2 按钮，三级节点(此处没做处理)
        }else {
            //如果是普通管理员，根据用户获取角色，在获取响应权限，进行条件关联查询
            list = permissionDao.findPermissionListByAdminId(adminId); //按照类型type=1进行查询
        }
        //工具类，组装父子关系
        List<Permission> relationList = PermissionHelper.bulid(list);

        return relationList;//组装父子关系才能返回
    }

    //菜单管理 获取权限集合
    @Override
    public List<Permission> findAllMenu() {
        //全部权限列表
        List<Permission> permissionList = permissionDao.findAll();
        if(CollectionUtils.isEmpty(permissionList)) return null;

        //构建树形数据,总共三级
        //把权限数据构建成树形结构数据
        List<Permission> result = PermissionHelper.bulid(permissionList);
        return result;
    }

    //获取选项权限集合?
    @Override
    public List<String> findCodeListByAdminId(Long id) {
        return permissionDao.findCodeListByAdminId(id);
    }

    @Override
    public List<String> findAllCode() {
        return permissionDao.findAllCode();
    }
}
