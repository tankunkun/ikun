package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/24 9:50
 * @Author by:Plisetsky
 */

@RestController //等同于@Controller+@ResponseBody
@RequestMapping("/house")
public class HouseController {

    @Reference
    HouseService houseService;
    @Reference
    CommunityService communityService;
    @Reference
    HouseBrokerService houseBrokerService;
    @Reference
    HouseImageService houseImageService;
    @Reference
    UserFollowService userFollowService;

    //详情页
    @RequestMapping("/info/{id}")
    public Result<Map<String,Object>> list(@PathVariable("id") Long id, HttpServletRequest request){
        House house = houseService.getById(id);
        Community community = communityService.getById(house.getCommunityId());

        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);

        List<HouseImage> houseImageList = houseImageService.findList(id, 1);

        //补充代码: 关注房源 2022 6-25
        //登录后，判断当前用户是否已关注房源，将isFollow变为动态 TODO
        boolean isFollow = false;
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        if(userInfo != null){
            Long userInfoId = userInfo.getId();
            isFollow = userFollowService.isFollow(userInfoId,id);
        }

        HashMap<String, Object> map = new HashMap<>();

        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImageList);
        //关注业务后续补充 补充
        map.put("isFollow",isFollow);
        return Result.ok(map);
    }


    //ajax渲染首页
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result<PageInfo<HouseVo>> list(@PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize,
                                          @RequestBody HouseQueryVo houseQueryVo){//坑，需要通过请求体获取

        PageInfo<HouseVo> page = houseService.findListPage(pageNum, pageSize, houseQueryVo);

        return Result.ok(page); //将分页数据封装在Result中返回(转换为json串返回的)
    }

}
