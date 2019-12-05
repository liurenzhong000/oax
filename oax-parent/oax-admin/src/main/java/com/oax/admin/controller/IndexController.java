package com.oax.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/")
    public String index() {
        return "forward:/index.html";
    }


    @RequestMapping("/wetest")
    public String wetest(){
        return "forward:/index1.html";
    }
}
