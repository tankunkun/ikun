package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

/**
 * @Date 2022/6/22 14:12
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {

    private static final String PAGE_UPLOAD_SHOW = "house/upload";
    private static final String ACTION_LIST = "redirect:/house/detail/";

    @Reference
    HouseImageService houseImageService;


    //删除默认展示图片
    @RequestMapping("/deleteDefault/{houseId}/{id}")
    public String deleteDefault(@PathVariable("houseId")Long houseId,@PathVariable("id") Long id) {
        HouseImage houseImage = houseImageService.getById(id);
        String imageName = houseImage.getImageName();
        houseImageService.delete(id);//删除前查询数据，否则删除后查不到了
        //删除house默认图片
        houseImageService.updateDefaultImage(houseId,null);

        //删除七牛云内图片
        QiniuUtils.deleteFileFromQiniu(imageName);

        return ACTION_LIST + houseId;
    }

    //删除房源、房产图片
    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId")Long houseId,@PathVariable("id") Long id){

        HouseImage houseImage = houseImageService.getById(id);
        String imageName = houseImage.getImageName();
        houseImageService.delete(id);//删除前查询数据，否则删除后查不到了
        //删除七牛云内图片
        QiniuUtils.deleteFileFromQiniu(imageName);

        return ACTION_LIST + houseId;
    }

    //上传房源、房产图片，可以不返回值
    @RequestMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile[] files, //file是固定的，底层会自动生成表单项.[]接收多个表单项
                         @PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type) throws Exception { //接收上传文件，再上传到七牛云上

        if(files !=null && files.length>0){ //至少上传一个文件
            for (MultipartFile file:files){
                byte[] bytes = file.getBytes();

                //String name = file.getOriginalFilename();//原始文件名称
                //QiniuUtils.upload2Qiniu(bytes,name); 不能使用原始文件名称上传，会重名覆盖

                String newFileName = UUID.randomUUID().toString();
                //将图片上传到七牛云
                QiniuUtils.upload2Qiniu(bytes,newFileName);
                String imageUrl = "http://rdv0p81lw.hn-bkt.clouddn.com/"+newFileName;

                //如果上传的是首页默认图片，将地址更新到house表
                if(type==3){
                    houseImageService.updateDefaultImage(houseId,imageUrl);
                }

                //将图片路径信息等保存到数据库
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(newFileName);
                houseImage.setImageUrl(imageUrl);
                houseImage.setType(type);
                houseImageService.insert(houseImage);
            }

        }
        return Result.ok();
    }
    //前往上传页面
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type, Map map){

        map.put("houseId",houseId);
        map.put("type",type);
        return PAGE_UPLOAD_SHOW;
    }
}
