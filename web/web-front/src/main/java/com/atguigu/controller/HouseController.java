package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Community;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseBroker;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.CommunityService;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

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


    //详情页
    @RequestMapping("/info/{id}")
    public Result<Map<String,Object>> list(@PathVariable("id") Long id){
        House house = houseService.getById(id);
        Community community = communityService.getById(house.getCommunityId());

        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        List<HouseImage> houseImageList = houseImageService.findList(id, 1);
        HashMap<String, Object> map = new HashMap<>();

        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImageList);
        //关注业务后续补充
        map.put("isFollow",false);
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
