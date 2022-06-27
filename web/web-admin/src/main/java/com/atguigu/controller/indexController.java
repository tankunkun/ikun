package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    private static final String PAGE_LOGIN = "frame/login";

    @Reference
    AdminService adminService;
    @Reference
    PermissionService permissionService;

    //框架首页
    @RequestMapping("/")
    public String index(Map map){
        //准备两个数据
        //从session域中获取登录的用户信息,登录功能还没做，临时设置一个登录用户即可

        //Long adminId = 1L; //假设用户id=1
        //Admin admin = adminService.getById(adminId);

        //代码补充 TODO
        //通过 SecurityContextHolder 从县城中获取认证对象
        //(框架过滤器会将session域的用户对象存放到当前线程上(ThreadLocal)) 比session效率高
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        Admin admin = adminService.getByUsername(user.getUsername());

        //左侧菜单树，自己通过双层for循环迭代生成的，当前集合值只存放父节点，子节点通过
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(admin.getId());
        map.put("admin",admin);
        map.put("permissionList",permissionList);

        return PAGE_FRAM;
    }
    //框架主页
    @RequestMapping("/main")
    public String main(){
        return PAGE_MAIN;
    }

    @RequestMapping("/login")
    public String login(){
        return PAGE_LOGIN;
    }
}
