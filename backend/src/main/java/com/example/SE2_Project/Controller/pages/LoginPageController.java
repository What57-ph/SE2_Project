package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Security.SecurityUtils;
import com.example.SE2_Project.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
public class LoginPageController {

    @Autowired
   private  LoginService loginService;

    @GetMapping("/guest/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/report")
    public String reportPage() {
        return "report/report";
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "expenses/index";
    }

    @GetMapping("/process-after-login")
    public String processAfterLogin() {
        return loginService.processAfterLogin();
    }
}
