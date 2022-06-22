package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/22 9:42
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String ACTION_LIST = "redirect:/house/detail/";
    @Reference
    HouseBrokerService houseBrokerService;
    @Reference
    AdminService adminService;

    //删除
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseBrokerService.delete(id);
        return ACTION_LIST+houseId;
    }

    //修改
    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,HouseBroker houseBroker,HttpServletRequest reques){
        //修改经纪人 名称和头像也要更新
        Long adminId = houseBroker.getBrokerId();
        Admin admin = adminService.getById(adminId);

        HouseBroker dbBroker = houseBrokerService.getById(id);
        dbBroker.setBrokerId(houseBroker.getBrokerId());

        //更新名称和图片
        dbBroker.setBrokerName(admin.getName());
        dbBroker.setBrokerHeadUrl(admin.getHeadUrl());

        houseBrokerService.update(dbBroker);

        return this.successPage(null,reques);
    }

    //前往编辑/houseUser/edit/6
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map){

        List<Admin> adminList = adminService.findAll();
        HouseBroker houseBroker = houseBrokerService.getById(id);

        map.put("adminList",adminList);
        map.put("houseBroker",houseBroker);
        return PAGE_EDIT;
    }

    //存储
    @RequestMapping("/save")
    public String save(HouseBroker houseBroker, HttpServletRequest reques){
        Long adminId = houseBroker.getBrokerId();
        Admin admin = adminService.getById(adminId);
        houseBroker.setBrokerName(admin.getName());//给冗余字段赋值
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());

        //判断经纪人是否重复添加
        List<HouseBroker> brokers = houseBrokerService.findListByHouseId(houseBroker.getHouseId());
        for (HouseBroker broker : brokers) {
            if(broker.getBrokerId()==adminId){
                return this.successPage("添加失败，该经纪人已存在",reques);
            }
        }

        houseBrokerService.insert(houseBroker);

        return this.successPage(null,reques);
    }

    //前往创建页面
    @RequestMapping("/create")
    //public String create(Long houseId){
    public String create(HouseBroker houseBroker, Map map){//houseId封装在Broker对象内
        List<Admin> adminList = adminService.findAll();


        map.put("adminList",adminList);
        map.put("houseBroker",houseBroker);
        return PAGE_CREATE;
    }
}
