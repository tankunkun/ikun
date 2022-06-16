package com.atguigu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Date 2022/6/15 15:31
 * @Author by:Plisetsky
 */
@Controller
@RequestMapping
public class indexController {

    private static final String PAGE_FRAM = "frame/index";
    private static final String PAGE_MAIN = "frame/main";

    @RequestMapping("/")
    public String index(){
        return PAGE_FRAM;
    }

    @RequestMapping("/main")
    public String main(){
        return PAGE_MAIN;
    }
}
