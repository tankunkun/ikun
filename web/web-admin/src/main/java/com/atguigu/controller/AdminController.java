package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


// ctrl + r  查找替换
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_CREATE = "admin/create";
    private static final String ACTION_LIST = "redirect:/admin";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String PAGE_UPLOAD = "admin/upload";
    private static final String PAGE_ASSIGN_ROLE ="admin/assignShow";

    @Reference
    AdminService adminService;
    @Reference
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //更新角色分配表
    /*@RequestMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") String roleIds,//自己分解字符串，不好
                             HttpServletRequest request){
        //注意:操作中间表数据，通过多对多两端任意接口都行，中间表不提供业务接口
        roleService.assignRole(adminId,roleIds);
        return this.successPage(null,request);
    }*/
    @PreAuthorize("hasAuthority('admin.assgin')")
    @RequestMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") Long[] roleIds,//[1,2,3,4,null]
                             HttpServletRequest request){
        //注意:操作中间表数据，通过多对多两端任意接口都行，中间表不提供业务接口
        roleService.assignRole(adminId,roleIds);
        return this.successPage(null,request);
    }

    //分配角色页面，需要准备两个下拉列表集合:noAssignRoleList assignRoleList
    @PreAuthorize("hasAuthority('admin.assgin')")
    @RequestMapping("/assignRole/{id}")
    public String assignRole(@PathVariable("id") Long id,Map map){

        //map中存放的是两个下拉列选的集合
        Map massignMap = roleService.getSelectMapByAdminId(id);

        map.putAll(massignMap);
        map.put("adminId",id);
        return PAGE_ASSIGN_ROLE;
    }

    //头像上传
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id,
                         @PathVariable("file") MultipartFile file,HttpServletRequest request) throws IOException {
        //1.上传到七牛云
        byte[] bytes = file.getBytes();
        String newFileName = UUID.randomUUID().toString();
        QiniuUtils.upload2Qiniu(bytes,newFileName);

        //2.存储到数据库
        String imageUrl = "http://rdv0p81lw.hn-bkt.clouddn.com/"+newFileName;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(imageUrl);
        adminService.update(admin);

        return this.successPage(AdminController.MESSAGE_SUCCESS, request);
    }


    //前往头像上传
    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Map map){
        map.put("id",id);

        return PAGE_UPLOAD;
    }

    //删除
    @PreAuthorize("hasAuthority('admin.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        adminService.delete(id); //返回结果表示sql语句对数据库起作用的行数
        return ACTION_LIST;
    }
    //修改
    @PreAuthorize("hasAuthority('admin.edit')")
    @RequestMapping("/update")
    public String update(Admin admin,Map map,HttpServletRequest request){ //springMVC框架根据反射创建bean对象，并调用参数名称的set方法，将参数封装到bean对象中。
        adminService.update(admin);
        return this.successPage("修改成功",request);
    }
    //前往编辑
    @PreAuthorize("hasAuthority('admin.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Map map){
        Admin admin = adminService.getById(id);
        map.put("admin",admin);
        return PAGE_EDIT;
    }

    //保存 TODO passwordEncoder 加密
    @PreAuthorize("hasAuthority('admin.create')")
    @RequestMapping("/save")
    public String save(Admin admin,Map map,HttpServletRequest request){
        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");
        //加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        adminService.insert(admin);
        return this.successPage("添加成功",request);
    }
    @PreAuthorize("hasAuthority('admin.create')")
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
    @PreAuthorize("hasAuthority('admin.show')")
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
