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

    //数据字典
    @RequestMapping("/findZnodes")
    @ResponseBody   //响应体 异步返回数据
    //通过节点id获取子节点集合，并返回; defaultValue="0": 第一次访问时还没有树，因此赋值为0，查询根节点的子节点
    public Result findZnodes(@RequestParam(value = "id",required = false,defaultValue = "0") Long parentId){

        //传递的是节点的id，作为外键 (parentId) 来使用; 每一个树节点都是一个json对象，用map集合来表示json
        //[{ id:1, pId:0, name:"全部分类", isParent:true}]
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

