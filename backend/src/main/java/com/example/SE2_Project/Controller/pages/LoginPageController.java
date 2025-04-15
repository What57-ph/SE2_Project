package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Dto.MonthlySummaryDTO;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Security.SecurityUtils;
import com.example.SE2_Project.Service.LoginService;
import com.example.SE2_Project.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("")
@Slf4j
public class LoginPageController {
    @Autowired
   private  LoginService loginService;
    @Autowired
   private TransactionService transactionService;

    @GetMapping("/guest/login")
    public String loginPage() {
        return "auth/login";
    }


    @GetMapping("/expense")
    public String homepage() {
        return "expenses/index";
    }

    @GetMapping("/process-after-login")
    public String processAfterLogin() {
        return loginService.processAfterLogin();
    }

    @GetMapping("/homepage/show")
    public String getHomePage(Model model){
        Integer year = LocalDate.now().getYear();
        List<MonthlySummaryDTO> monthlySummary = transactionService.getMonthlySummary(year);
        List<TransactionEntity> transactions=transactionService.getTransactionsForCurrentUser();
        model.addAttribute("transactions",transactions);
        model.addAttribute("summary", monthlySummary);
        model.addAttribute("year", year);

        return "homepage/show";

    }
}
