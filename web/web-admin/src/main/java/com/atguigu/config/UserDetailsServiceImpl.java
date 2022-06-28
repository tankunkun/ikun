package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date 2022/6/27 16:02
 * @Author by:Plisetsky
 */
//加载用户信息权限集合
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    AdminService adminService;

    @Reference
    PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.通过表单username获取admin对象，框架底层通过编码器自动解析密码进行匹配
        Admin admin = adminService.getByUsername(username);
        if(admin == null){
            //return null;
            throw new UsernameNotFoundException("用户名不存在");
        }

        //2.1 获取选项权限集合
        List<String> codeList = null;
        if(admin.getId() == 1){ //超级管理员
            codeList = permissionService.findAllCode();
        }else {
            codeList = permissionService.findCodeListByAdminId(admin.getId());
        }
        //2.2 构建权限对象集合
        Set<GrantedAuthority> auths = new HashSet();
        if(codeList!=null && codeList.size()>0){
            for (String code : codeList) {
                //将code(权限字段)构建成权限对象，SimpleGrantedAuthority是GrantedAuthority的实现类
                auths.add(new SimpleGrantedAuthority(code));
            }
        }
        // org.springframework.security.core.userdetails.User implements UserDetails
        // User 是 UserDetails的实现类
        return new User(admin.getUsername(),admin.getPassword(), auths);
    }
}
