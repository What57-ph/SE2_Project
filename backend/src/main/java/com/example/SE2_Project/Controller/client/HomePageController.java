package com.example.SE2_Project.Controller.client;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class HomePageController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/show")
    public String getHomePage(){
        return "/homepage/show";
    }

    @GetMapping("/expense")
    public String expensePage(Model model) {
        List<TransactionEntity> transactions = transactionService.getAllExpenseTransactions();
        List<CategoryEntity> categories = categoryService.getAllCategories();
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        return "expenses/index";
    }

}
