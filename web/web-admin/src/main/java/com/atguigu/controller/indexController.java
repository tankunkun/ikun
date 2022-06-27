package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/15 15:31
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping
public class indexController {

    private static final String PAGE_FRAM = "frame/index";
    private static final String PAGE_MAIN = "frame/main";

    @Reference
    AdminService adminService;
    @Reference
    PermissionService permissionService;

    //框架首页
    @RequestMapping("/")
    public String index(Map map){
        //准备两个数据
        //从session域中获取登录的用户信息,登录功能还没做，临时设置一个登录用户即可
        Long adminId = 1L; //假设用户id=1
        Admin admin = adminService.getById(adminId);
        //左侧菜单树，自己通过双层for循环迭代生成的，当前集合值只存放父节点，子节点通过
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(adminId);

        map.put("admin",admin);
        map.put("permissionList",permissionList);

        return PAGE_FRAM;
    }
    //框架主页
    @RequestMapping("/main")
    public String main(){

        return PAGE_MAIN;
    }
}
