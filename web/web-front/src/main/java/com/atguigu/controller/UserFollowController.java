package com.atguigu.controller;

/**
 * @Date 2022/6/25 10:25
 * @Author by:Plisetsky
 */
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/userFollow")
public class UserFollowController extends BaseController {

    @Reference
    private UserFollowService userFollowService;

    /**
     * 关注房源
     * @param houseId
     * @param request
     * @return
     */
    @GetMapping("/auth/follow/{houseId}")
    public Result follow(@PathVariable("houseId") Long houseId, HttpServletRequest request){
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("USER");
        Long userId = userInfo.getId();
        userFollowService.follow(userId, houseId);
        return Result.ok();
    }

}