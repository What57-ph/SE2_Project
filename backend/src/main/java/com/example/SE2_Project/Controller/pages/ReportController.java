package com.example.SE2_Project.Controller.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {
    @GetMapping("")
    public String reportPage(Model model) {
        return "report/report";
    }


}
