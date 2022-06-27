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
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference
    AdminService adminService;

    @Reference
    PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getByUsername(username);
        if(admin == null){
            //return null;
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<String> codeList = null;
        if(admin.getId() == 1){ //超级管理员
            codeList = permissionService.findAllCode();
        }else {
            codeList = permissionService.findCodeListByAdminId(admin.getId());
        }
        Set<GrantedAuthority> auths = new HashSet();
        if(codeList!=null && codeList.size()>0){
            for (String code : codeList) {
                auths.add(new SimpleGrantedAuthority(code));
            }
        }
        return new User(admin.getUsername(),admin.getPassword(), auths);

        //User implements
        //权限集合 : 暂时设置空，无任何权限
        /*return new User(admin.getUsername(),admin.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
    }
}
