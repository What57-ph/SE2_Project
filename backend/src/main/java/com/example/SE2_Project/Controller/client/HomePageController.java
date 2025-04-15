package com.example.SE2_Project.Controller.client;

import com.example.SE2_Project.Dto.CategoryExpenseDTO;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
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
    public String expensePage(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "7") int size,
                              @RequestParam(defaultValue = "transactionDate,asc") String sort) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Tách field và thứ tự
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        String sortOrder = sortParams.length > 1 ? sortParams[1] : "asc";

        // Tạo sort đúng cách
        Sort sortObj = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<TransactionEntity> transactionPage = transactionService.getTransactionsForUserPaginated(username, pageable);
        Set<CategoryEntity> categories = categoryService.getCategoriesForCurrentUser();
        model.addAttribute("categories", categories);
        model.addAttribute("transactionPage", transactionPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transactionPage.getTotalPages());
        model.addAttribute("sort", sort); // để Thymeleaf dùng lại

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
