package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Dto.CategoryExpenseDTO;
import com.example.SE2_Project.Dto.MonthlySummaryDTO;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Security.SecurityUtils;
import com.example.SE2_Project.Service.LoginService;
import com.example.SE2_Project.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
@Slf4j
public class LoginPageController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
   private  LoginService loginService;

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
        List<TransactionEntity> transactions = transactionService.getTransactionsForCurrentUser()
                .stream()
                .sorted(Comparator.comparing(TransactionEntity::getTransactionDate).reversed())
                .limit(7)
                .collect(Collectors.toList());

        model.addAttribute("transactions", transactions);

        model.addAttribute("summary", monthlySummary);
        model.addAttribute("year", year);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<CategoryExpenseDTO> expenseData = transactionService.getCurrentMonthExpenseCategoryTotals(username);
        List<String> expenseLabels = expenseData.stream().map(CategoryExpenseDTO::getCategory).toList();
        List<BigDecimal> expenseValues = expenseData.stream().map(CategoryExpenseDTO::getTotalAmount).toList();

        List<CategoryExpenseDTO> incomeData = transactionService.getCurrentMonthIncomeCategoryTotals(username);
        List<String> incomeLabels = incomeData.stream().map(CategoryExpenseDTO::getCategory).toList();
        List<BigDecimal> incomeValues = incomeData.stream().map(CategoryExpenseDTO::getTotalAmount).toList();

        model.addAttribute("expenseLabels", expenseLabels);
        model.addAttribute("expenseValues", expenseValues);
        model.addAttribute("incomeLabels", incomeLabels);
        model.addAttribute("incomeValues", incomeValues);

        // 👇 Gửi thêm tổng cộng để hiển thị giữa biểu đồ
        BigDecimal totalExpense = expenseValues.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncome = incomeValues.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("totalIncome", totalIncome);

        return "homepage/show";
    }
}
