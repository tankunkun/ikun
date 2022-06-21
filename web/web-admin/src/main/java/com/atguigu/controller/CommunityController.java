package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/21 9:35
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    private final static String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String ACTION_LIST = "redirect:/community";

    @Reference
    CommunityService communityService;

    @Reference
    DictService dictService;


    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        communityService.delete(id);
        return ACTION_LIST;
    }
    

    //修改
    @RequestMapping("/update")
    public String update(Community community,HttpServletRequest request){
        communityService.update(community);
        return this.successPage(null,request);
    }

    //前往编辑页面
    @RequestMapping("/edit/{id}")
    public String edit(Map map, @PathVariable("id") Long id){
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        Community community = communityService.getById(id);
        map.put("areaList",areaList);
        map.put("community",community);
        return PAGE_EDIT;
    }

    //添加
    @RequestMapping("/save")
    public String save(Community community,HttpServletRequest request){
        communityService.insert(community);
        //成功页面不传消息默认为 操作成功
        return this.successPage(null,request);
    }

    //前往新增页面
    @RequestMapping("/create")
    public String create(Map map){
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        //数据回显，返回请求参数
        map.put("areaList",areaList);
        return PAGE_CREATE;
    }

    //前往主页
    @RequestMapping
    public String index(HttpServletRequest request,Map map){
        //1. 分页数据查询
        Map<String, Object> filters = getFilters(request);//获取请求参数
        //如果参数为空，返回空字符串""，前台判断显示"请显示区域"
        if(!filters.containsKey("areaId")) {
            filters.put("areaId", "");
        }
        if(!filters.containsKey("plateId")) {
            filters.put("plateId", "");
        }
        //page信息
        PageInfo<Community> page = communityService.findPage(filters);

        //2. 获取下拉列选(区域)
        List<Dict> areaList = dictService.findListByDictCode("beijing");

        //3. 数据回显，返回请求参数
        map.put("areaList",areaList);
        map.put("page",page);
        map.put("filters",filters);

        return PAGE_INDEX;
    }



}
