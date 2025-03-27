package com.example.SE2_Project.Controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomePageController {
    @GetMapping("/")
    public String getHomePage(){
        return "client/homepage/show";
    }
    @GetMapping("/expense/add")
    public String getTransactionPage(){
        return "client/calendar/calendar";
    }

}
