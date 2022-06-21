package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/21 14:23
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {

    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String ACTION_LIST = "redirect:/house";
    private static final String PAGE_DETAIL = "house/detail";
    @Reference
    HouseService houseService;
    @Reference
    CommunityService communityService;
    @Reference
    DictService dictService;
    @Reference
    HouseImageService houseImageService;
    @Reference
    HouseBrokerService houseBrokerService;
    @Reference
    HouseUserService houseUserService;

    @RequestMapping("/detail/{id}")
    public String detail(Map map,@PathVariable Long id){

        //1.房源对象
        //在业务层，重写getById方法，关联字典名称
        House house = houseService.getById(id);
        //2.小区对象
        //在业务层重写，获取小区名字
        Community community = communityService.getById(house.getCommunityId());

        //3.普通房源图片集合
        //表示普通房源图片
        List<HouseImage> houseImage1List = houseImageService.findList(id, 1);

        //4.房产图片集合
        //查询房产图片，不能在前端系统显示，只能让后台管理员看到
        List<HouseImage> houseImage2List = houseImageService.findList(id, 2);


        //5.经纪人集合
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);

        //6.房东集合
        List<HouseUser> houseUserList = houseUserService.findListByHouseId(id);


        map.put("house",house);
        map.put("community",community);
        map.put("houseImage1List",houseImage1List);
        map.put("houseImage2List",houseImage2List);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseUserList",houseUserList);

        return PAGE_DETAIL;
    }


    //发布
    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        houseService.publish(id, status);
        return ACTION_LIST;
    }

    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        houseService.delete(id);
        return ACTION_LIST;
    }

    //修改
    @RequestMapping("/update")
    public String update(House house,HttpServletRequest request){
        houseService.update(house);
        return this.successPage(null,request);
    }

    //前往编辑
    @RequestMapping("/edit/{id}")
    public String create(Map map, @PathVariable("id") Long id){
        //查询房源实体对象
        House house = houseService.getById(id);

        //2. 所有小区数据
        List<Community> communityList = communityService.findAll();
        //3. 6个数据字段集合
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");

        map.put("house",house);

        map.put("communityList",communityList);
        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);

        return PAGE_EDIT;
    }

    //保存
    @RequestMapping("/save")
    public String save(House house,HttpServletRequest request){
        houseService.insert(house);
        return this.successPage(null,request);
    }

    //前往新增
    @RequestMapping("/create")
    public String create(Map map){

        //1. 所有小区数据
        List<Community> communityList = communityService.findAll();
        //2. 6个数据字段集合
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");

        map.put("communityList",communityList);
        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);

        return PAGE_CREATE;
    }

    //主页
    @RequestMapping
    public String index(Map map, HttpServletRequest request){
        Map<String, Object> filters = getFilters(request);
        //1. 分页数据
        PageInfo<House> page = houseService.findPage(filters);

        //2. 所有小区数据
        List<Community> communityList = communityService.findAll();

        //3. 6个数据字段集合
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");


        //4. filters数据，用于回显
        map.put("page",page);
        map.put("communityList",communityList);
        map.put("houseTypeList",houseTypeList);
        map.put("floorList",floorList);
        map.put("buildStructureList",buildStructureList);
        map.put("directionList",directionList);
        map.put("decorationList",decorationList);
        map.put("houseUseList",houseUseList);

        map.put("filters",filters);
        return PAGE_INDEX;
    }
}
