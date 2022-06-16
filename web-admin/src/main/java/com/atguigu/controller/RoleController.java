package com.atguigu.controller;

import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @Date 2022/6/15 14:43
 * @Author by:Plisetsky
 */

@Controller
@RequestMapping("/role")
public class RoleController {

    private final static String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String ACTION_LIST = "redirect:/role";
    private static final String PAGE_SUCCESS = "common/successPage";
    @Autowired
    RoleService roleService;

    //添加
    @RequestMapping("/save")
    public String save(Role role, Map map){  //springMVC根据反射创建Bean,调用参数名称的set方法,将参数注入到对象
        roleService.insert(role);
        //Map 和 ModelMap 本质相同，map调用put即可
        map.put("messagePage","添加成功");
        //return ACTION_LIST;
        return PAGE_SUCCESS;
    }

    //跳转新增
    @RequestMapping("/create")
    public String create(){

        return PAGE_CREATE;
    }

    @RequestMapping
    public String index(ModelMap model) {
        List<Role> list = roleService.findAll();
        model.addAttribute("list", list);
        return PAGE_INDEX;
    }

}
