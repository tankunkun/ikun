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

    @Reference
    DictService dictService;

    //数据字典
    //传递的是节点的id，作为外键 (parentId) 来使用
    @RequestMapping("/findZnodes")
    @ResponseBody   //异步返回数据，而不是请求转发
    public Result findZnodes(@RequestParam(value = "id",required = false,defaultValue = "0") Long parentId){//通过节点id获取子节点集合，返回
        //每一个数节点都是一个json对象，用map集合来表示json
        //
        List<Map<String,Object>> data = dictService.findZnodesByParentId(parentId);
        //工具类 将数据封装到Result对象上，返回json串
        return Result.ok(data);
    }

    @RequestMapping
    public String index(){
        return PAGE_INDEX;
    }


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

