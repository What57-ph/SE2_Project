package com.example.SE2_Project.Controller.pages;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest")
public class RegisterController {

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }
}
