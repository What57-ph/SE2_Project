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
import java.util.Set;

@Controller
@RequestMapping("/user")
public class HomePageController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/transactions")
    public String expensePage(Model model) {
        List<TransactionEntity> transactions = transactionService.getTransactionsForCurrentUser();
        Set<CategoryEntity> categories = categoryService.getCategoriesForCurrentUser();
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        return "expenses/index";
    }
    @GetMapping("/transactionType")
    public String transactionTypePage(Model model){
        List<TransactionEntity> expensesList=transactionService.findTransactionByTypeAndUserId("EXPENSE");
        model.addAttribute("expenses",expensesList);
        List<TransactionEntity> incomeList=transactionService.findTransactionByTypeAndUserId("INCOME");
        model.addAttribute("incomes",incomeList);
        Set<CategoryEntity> categories = categoryService.getCategoriesForCurrentUser();
        model.addAttribute("categories", categories);
        return "expenses/transactionType";
    }
    @GetMapping("/calendar")
    public String calendarPage(Model model) {
        return "/calendar/calendar";
    }
    @GetMapping("/income")
    public String incomePage(Model model) {
        List<TransactionEntity> transactions = transactionService.getTransactionsForCurrentUser();
        List<CategoryEntity> categories = (List<CategoryEntity>) categoryService.getCategoriesForCurrentUser();
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        return "income/index";
    }


}
