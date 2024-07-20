package com.vanlang.webbanhang.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("")
public class HomepageController {
    @GetMapping("/")
    public String  Home(){
        return "/index";
    }
}
