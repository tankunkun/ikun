package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.result.Result;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2022/6/24 9:50
 * @Author by:Plisetsky
 */
@RestController //等同于@Controller+@ResponseBody
@RequestMapping("/house")
public class HouseController {

    @Reference
    HouseService houseService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result<PageInfo<HouseVo>> list(@PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize,
                                          @RequestBody HouseQueryVo houseQueryVo){//坑，需要通过请求体获取

        PageInfo<HouseVo> page = houseService.findListPage(pageNum, pageSize, houseQueryVo);

        return Result.ok(page); //将分页数据封装在Result中返回(转换为json串返回的)
    }

}
