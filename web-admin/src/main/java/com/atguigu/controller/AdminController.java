package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


// ctrl + r  查找替换
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_CREATE = "admin/create";
    private static final String ACTION_LIST = "redirect:/admin";
    private static final String PAGE_EDIT = "admin/edit";

    @Autowired
    AdminService adminService;


    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        adminService.delete(id); //返回结果表示sql语句对数据库起作用的行数
        return ACTION_LIST;
    }

    @RequestMapping("/update")
    public String update(Admin admin,Map map,HttpServletRequest request){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        adminService.update(admin);
        return this.successPage("修改成功",request);
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map){
        Admin admin = adminService.getById(id);
        map.put("admin",admin);
        return PAGE_EDIT;
    }

    @RequestMapping("/save")
    public String save(Admin admin,Map map,HttpServletRequest request){
        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");
        adminService.insert(admin);
        return this.successPage("添加成功",request);
    }

    @RequestMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }

    /**
     * 分页查询
     *      根据查询条件进行查询
     *          adminName = ''
     *          pageNum = 1   隐藏域
     *          pageSize = 10  隐藏域
     * @param map
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request,Map map){
        Map<String,Object> filters =  getFilters(request);
        //分页对象，将集合数据，pageNum,pageSize,pages,total等
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        map.put("page",pageInfo);
        map.put("filters",filters);
        return PAGE_INDEX;
    }

}
