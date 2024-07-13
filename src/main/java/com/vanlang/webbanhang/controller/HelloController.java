package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Category;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController{
    @GetMapping("/")
    public String hello(){
        return "Hello Controller";
    }


}
