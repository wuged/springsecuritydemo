package com.stjh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 登录控制器
 * @author wuge
 * @date 2020/4/28 22:03
 */
@Controller
public class LoginController {

    @PostMapping("/login")
    public String login() {
        System.out.println("登录");
        return "redirect:main.html";
    }
}
