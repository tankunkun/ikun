package com.atguigu.interceptor;

import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Date 2022/6/25 11:08
 * @Author by:Plisetsky
 */

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 访问controller前拦截，判断是否登录，如果登录放行，否则拒绝访问controller
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        if(userInfo == null){
            //拒绝访问，返回错误提示 208未登录
            Result result = Result.build("未登录,请先登录", ResultCodeEnum.LOGIN_AUTH);

            //JSON.toJSONString()
            //response.getWriter().print();
            WebUtil.WriteJSON(response,result);//将数据转换为json返回给浏览器
            return false; //拒绝访问
        }
        return true;//放行
    }
}
