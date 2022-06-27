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
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/userFollow")
public class UserFollowController extends BaseController {


    @Reference
    private UserFollowService userFollowService;


    //取消关注
    @GetMapping(value = "/auth/cancelFollow/{id}")
    public Result findListPage (@PathVariable("id") Long id){
        userFollowService.delete(id);
        return Result.ok();
    }

    //关注列表
    @GetMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               HttpServletRequest request) {
        UserInfo userInfo = (UserInfo)request.getSession().getAttribute("USER");
        Long userId = userInfo.getId();
        PageInfo<UserFollowVo> pageInfo =
                userFollowService.findListPage(pageNum, pageSize, userId);
        return Result.ok(pageInfo);
    }


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