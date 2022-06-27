package com.atguigu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Date 2022/6/27 14:25
 * @Author by:Plisetsky
 */

@Configuration //声明为配置类，相当于一个xml
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的默认行为
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启基于方法级别的细粒度权限控制 即在controller方法上加权限注解即可，例如@PreAuthorize("hasAuthority('role.show')")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{


    @Autowired
    UserDetailsService userDetailsService;

    /*认证：
        1.基于内存的认证方式(了解)
        2.基于数据库的认证方式(重要)*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //1. 基于内存的认证方式，写死用户名称和密码，分配空的角色(没有权限)
        /*auth.inMemoryAuthentication()
                .withUser("lucy")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");*/

        //2.基于数据库的认证方式(重点)
        auth.userDetailsService(userDetailsService);
    }

    //授权:
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //默认授权: 不登陆系统，所有的资源都不允许访问
        //super.configure(http);
        //自定义授权控制

        //1.设置同源资源允许访问 同源(资源父路径一致的,协议,ip,port)的资源允许访问
        http.headers().frameOptions().sameOrigin();

        //2.授权静态资源不登录允许访问
        http.authorizeRequests()
                .antMatchers("/static/**","/login").permitAll()//允许匿名用户访问的路径
                .anyRequest().authenticated();//其他页面需要验证
        //3.授权自定义的登录页面
        //loginPage("/login")表示，通过这个映射跳转到自己的登录页，登陆成功后去向哪里
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");

        //4.授权注销路径
        //logoutUrl("/logout") 通过这个请求路径注销系统，销毁session，注销后去到哪
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");

        //5.关闭跨站请求伪造功能
        //开启会自动生成<input type="hidden" name="_csrf" value="f3f9c374-b7b5-4c21-b925-8a32b3a0ed27"/>
        http.csrf().disable();
    }

    //设置加密方式
    //声明一个bean对象,等价于<bean id='PasswordEncoder' class='org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;'>
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
