    package com.example.SE2_Project.Controller;

    import com.example.SE2_Project.Dto.IncomeTransactionDto;
    import com.example.SE2_Project.Entity.CategoryEntity;
    import com.example.SE2_Project.Entity.TransactionEntity;
    import com.example.SE2_Project.Service.CategoryService;
    import com.example.SE2_Project.Service.TransactionService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @Controller
    @RequestMapping("/transactions")
    public class TransactionController {

            @Autowired
             private TransactionService transactionService;
            @Autowired
            private CategoryService categoryService;


            @PostMapping("/addIncome")
               public String addIncomeTransaction(@ModelAttribute("transaction") IncomeTransactionDto transaction) {
                transactionService.addIncome(transaction);

                return "redirect:/get/all";

            }
            @GetMapping("/get/all")
               public String getAllTransactions(Model model) {
                    List<TransactionEntity> transactions=transactionService.getAllIncomeTransaction();
                    model.addAttribute("transactions",transactions);
               return "expenses/index";
            }
            @GetMapping("/addNew")
            public String getAddTransactionPage(Model model) {
                model.addAttribute("transaction", new IncomeTransactionDto());
                List<CategoryEntity> categories=categoryService.getAllCategories();
                model.addAttribute("categories",categories);
                return "expenses/addNew";
            }


    }
