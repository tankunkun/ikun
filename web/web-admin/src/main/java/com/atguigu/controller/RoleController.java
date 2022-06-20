package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * @Date 2022/6/15 14:43
 * @Author by:Plisetsky
 */

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private final static String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String ACTION_LIST = "redirect:/role"; //重定向
    //private static final String PAGE_SUCCESS = "common/successPage";
    private static final String PAGE_EDIT = "role/edit";

    @Reference
    RoleService roleService;

    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id,ModelMap model){
        roleService.delete(id);
        return ACTION_LIST;
    }

    //修改
    @RequestMapping(value="/update")
    public String update(Map map,Role role,HttpServletRequest request) {
        roleService.update(role);

        return this.successPage("修改成功",request);
    }

    //前往修改页面
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }


    @RequestMapping("/save")
    public String save(Role role, Map map,HttpServletRequest request){  //springMVC根据反射创建Bean,调用参数名称的set方法,将参数注入到对象
        roleService.insert(role);
        //Map 和 ModelMap 本质相同，map调用put即可
        return this.successPage("添加成功",request);
    }

    //前往新增页面
    @RequestMapping("/create")
    public String create(){

        return PAGE_CREATE;
    }

    /*
     * 分页查询角色列表
     *   根据条件进行查询
     *      roleName = ''
     *      pageNum = 1 隐藏域
     *      pageSize = 10 y隐藏域
     */
    @RequestMapping
    public String index(HttpServletRequest request,Map map) {
        Map<String, Object> filters = getFilters(request);
        //分页对象，将集合数据，pageNum、pageSize、total、pages等
        PageInfo<Role> pageInfo = roleService.findPage(filters);

        //用于数据回显
        map.put("page",pageInfo);
        map.put("filters",filters);
        return PAGE_INDEX;
    }
}
