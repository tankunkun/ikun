package com.atguigu.controller;

import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Date 2022/6/15 14:43
 * @Author by:Plisetsky
 */

@Controller
@RequestMapping("/role")
public class RoleController {

    private final static String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String ACTION_LIST = "redirect:/role"; //重定向
    private static final String PAGE_SUCCESS = "common/successPage";
    private static final String PAGE_EDIT = "role/edit";
    @Autowired
    RoleService roleService;

    //删除
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id,ModelMap model){
        roleService.delete(id);
        return ACTION_LIST;
    }

    //修改
    @RequestMapping(value="/update")
    public String update(Map map,Role role) {
        roleService.update(role);
        map.put("messagePage","修改成功");
        return PAGE_SUCCESS;
    }

    //前往修改页面
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    //添加
    @RequestMapping("/save")
    public String save(Role role, Map map){  //springMVC根据反射创建Bean,调用参数名称的set方法,将参数注入到对象
        roleService.insert(role);
        //Map 和 ModelMap 本质相同，map调用put即可
        map.put("messagePage","添加成功");
        //return ACTION_LIST;   返回列表，效果不好
        return PAGE_SUCCESS;
    }

    //前往新增页面
    @RequestMapping("/create")
    public String create(){

        return PAGE_CREATE;
    }

    /**
     * 分页查询
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
        map.put("page",pageInfo);
        //用于数据回显
        map.put("filters",filters);
        return PAGE_INDEX;
    }

    //角色列表
    /*@RequestMapping
    public String index(ModelMap model) {
        List<Role> list = roleService.findAll();
        model.addAttribute("list", list);
        return PAGE_INDEX;
    }*/
    /**
     * 封装页面提交的分页参数及搜索条件
     * @param request
     * @return
     */
    private Map<String, Object> getFilters(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> filters = new TreeMap();
        while(paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            String[] values = request.getParameterValues(paramName);
            if (values != null && values.length != 0) {
                if (values.length > 1) {
                    filters.put(paramName, values);
                } else {
                    filters.put(paramName, values[0]);
                }
            }
        }
        //如果没有提交请求参数(页数)，给该参数设置默认值
        if(!filters.containsKey("pageNum")) {
            filters.put("pageNum", 1);
        }
        if(!filters.containsKey("pageSize")) {
            filters.put("pageSize", 10);
        }

        return filters;
    }

}
