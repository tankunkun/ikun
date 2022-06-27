package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private static final String PAGE_ASSIGN_SHOW = "role/assignShow";

    //使用dubbo框架提供的远程依赖注入注解(别导错包)，Spring框架提供的@Autowired只能注入本地项目的Bean
    @Reference
    RoleService roleService;

    @Reference
    PermissionService permissionService;

    //存储权限
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,
                             @RequestParam("permissionIds") Long[] permissionIds, //Spring将字符串自动转换为数组
                             HttpServletRequest request){

        //map中存放的是两个下拉列选的集合
        permissionService.assignPermission(roleId,permissionIds);
        return this.successPage(null,request);
    }

    //分配权限列表
    @PreAuthorize("hasAuthority('role.assgin')")
    @RequestMapping("/assignShow/{roleId}")
    public String assignPermission(@PathVariable("roleId") Long roleId,Map map){

        //{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true},
        List<Map<String,Object>> permissionList = permissionService.findPermissionByRoleId(roleId);

        //map.put("permissionList",permissionList);
        //这里解析成了json字符串，前端就需要重新解析为json对象
        map.put("zNodes", JSON.toJSONString(permissionList));
        map.put("roleId",roleId);
        return PAGE_ASSIGN_SHOW;
    }


    //删除
    @PreAuthorize("hasAuthority('role.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id,ModelMap model){
        roleService.delete(id);
        return ACTION_LIST;
    }

    //修改
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping(value="/update")
    public String update(Map map,Role role,HttpServletRequest request) {
        roleService.update(role);

        return this.successPage("修改成功",request);
    }

    //前往修改页面
    @PreAuthorize("hasAuthority('role.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/save")
    public String save(Role role, Map map,HttpServletRequest request){  //springMVC根据反射创建Bean,调用参数名称的set方法,将参数注入到对象
        roleService.insert(role);
        //Map 和 ModelMap 本质相同，map调用put即可
        return this.successPage("添加成功",request);
    }

    //前往新增页面
    @PreAuthorize("hasAuthority('role.create')")
    @RequestMapping("/create")
    public String create(){

        return PAGE_CREATE;
    }

    //设置权限控制注解，在访问方法前需要校验控制权限
    @PreAuthorize("hasAuthority('role.show')")
    /**
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
