package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Security.SecurityUtils;
import com.example.SE2_Project.Service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("")
@Slf4j
public class LoginPageController {
    @Autowired
   private  LoginService loginService;

    @GetMapping("/guest/login")
    public String loginPage() {
        return "auth/login";
    }


    @GetMapping("/homepage")
    public String homepage() {
        return "expenses/homepage";
    }

    @GetMapping("/process-after-login")
    public String processAfterLogin() {
        return loginService.processAfterLogin();
    }
}
