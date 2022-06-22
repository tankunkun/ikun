package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/22 23:54
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping("/houseUser")
public class HouseUserController extends BaseController {

    private static final String PAGE_CREATE = "houseUser/create";
    private static final String PAGE_EDIT = "houseUser/edit";
    private static final String ACTION_LIST = "redirect:/house/detail/";

    @Reference
    HouseUserService houseUserService;

    //删除
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,@PathVariable("id") Long id){
        houseUserService.delete(id);
        return ACTION_LIST+houseId;
    }


    //修改
    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,HouseUser houseUser, HttpServletRequest request){
        //查询到houseId，赋值给表单houseUser
        HouseUser originalHouseUser = houseUserService.getById(id);
        houseUser.setHouseId(originalHouseUser.getHouseId());
        houseUserService.update(houseUser);
        return successPage(null,request);
    }

    //前往编辑
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map){
        HouseUser houseUser = houseUserService.getById(id);
        map.put("houseUser",houseUser);
        return PAGE_EDIT;
    }

    //新增
    @RequestMapping("/save")
    public String save(HouseUser houseUser, HttpServletRequest request){
        houseUserService.insert(houseUser);
        return successPage(null,request);
    }

    //前往新增
    @RequestMapping("/create")
    public String create(HouseUser houseUser, Map map){
        //主要传递 houseId
        map.put("houseUser",houseUser);
        return PAGE_CREATE;
    }
}
