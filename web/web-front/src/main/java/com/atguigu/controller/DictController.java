package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/20 15:42
 * @Author by:Plisetsky
 */
//二手房管理
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private static final String PAGE_INDEX = "dict/index";
    //注意，Dubbo下的Reference注解
    @Reference
    DictService dictService;


    //小区管理
    //根据上级id获取子节点数据列表
    @GetMapping(value = "/findListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findListByParentId(@PathVariable Long parentId) {
        List<Dict> list = dictService.findListByParentId(parentId);
        return Result.ok(list);
    }

    //根据编码获取子节点数据列表
    @GetMapping(value = "/findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findListByDictCode(dictCode);
        return Result.ok(list);
    }

}

