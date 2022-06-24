package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.RegisterVo;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Date 2022/6/24 15:45
 * @Author by:Plisetsky
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    UserInfoService userInfoService;
    //localhost:8001/userInfo/register/[object%20Object]
    //ajax提交post请求，通过请求体接收json数据，转换为对象
    @RequestMapping("/register")
    public Result getCode(@RequestBody RegisterVo registerVo,
                          HttpServletRequest request){
        String code = registerVo.getCode();
        String nickName = registerVo.getNickName();
        String password = registerVo.getPassword();
        String phone = registerVo.getPhone();

        //1.数据校验 (数据合法性)
        if(StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(nickName) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(phone)){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }

        //2.手机验证码是否有效(五分钟有效)
        //3.手机验证码是否一致
        String sendCode = (String) request.getSession().getAttribute("CODE");
        if(sendCode==null || !code.equals(sendCode)){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        //4.手机号是否被占用(表中字段unique)
        UserInfo userInfo = userInfoService.getByPhone(phone);
        if(userInfo!=null){
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        //5.封装数据保存数据(加密存储)
        userInfo = new UserInfo();
        //类型可以不同，属性名相同就可以拷贝
        BeanUtils.copyProperties(registerVo,userInfo);
        //密码加密
        userInfo.setPassword(MD5.encrypt(password));
        //设置状态码
        userInfo.setStatus(1);

        userInfoService.insert(userInfo);
        return Result.ok();
    }

    //发送手机验证码
    @RequestMapping("/getCode/{phone}")
    public Result getCode(@PathVariable("phone") String phone, HttpServletRequest request){
        //模拟发送手机验证码
        String code = "1111";
        request.getSession().setAttribute("CODE",code);
        return Result.ok(code);
    }
}
