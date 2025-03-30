package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/transactions")
public class Transaction {
    @Autowired
    TransactionService transactionService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/addNew")
    public String addNewTransaction(Model model) {
        TransactionEntity transaction = new TransactionEntity();
        List<CategoryEntity> categories = categoryService.getCategoriesForCurrentUser(); // Retrieve all categories

        // Set the transaction and categories to the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categories);

        return "expenses/addNew"; // Return to the addNew.html page
    }
    @PostMapping("/addExpense")
    public String addExpenseTransaction(@RequestParam("amount") BigDecimal amount,
                                        @RequestParam("transactionDate") LocalDate transactionDate,
                                        @RequestParam("categoryId") Long categoryId,
                                        @RequestParam("notes") String notes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }

        transactionService.addExpense(amount, transactionDate, categoryId, notes, username);
        return "redirect:/user/expense";
    }


    @GetMapping("/listTransaction")
    public String listTransactions(Model model) {
        List<TransactionEntity> transactions = transactionService.getTransactionsForCurrentUser();
        model.addAttribute("transactions", transactions);
        return "transaction/listTransaction"; // The view name for listTransaction
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transactionService.deleteTransaction(id);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting transaction.");
        }
        return "redirect:/user/expense";
    }

    @PostMapping("/update")
    public String updateTransaction(@RequestParam Long id,
                                    @RequestParam Long categoryId,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam String createdDate) {
        TransactionEntity existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        // Tìm danh mục từ ID
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Cập nhật thông tin giao dịch
        existingTransaction.setCategory(category);
        existingTransaction.setAmount(amount);
        existingTransaction.setCreatedDate(LocalDate.parse(createdDate));

        transactionRepository.save(existingTransaction);
        return "redirect:/user/expense";
    }



    @ResponseBody
    @GetMapping("/expenseChartData")
    public List<Map<String, Object>> getExpenseChartData(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        return transactionService.getCategoryExpenseReport(month, year);
    }


    //Tạo chart xem income expense chiếm bao nhiêu % theo tháng
    @GetMapping("/incomeExpenseReport")
    public String getIncomeExpenseReport(@RequestParam(value = "month", required = false, defaultValue = "3") int month,
                                         @RequestParam(value = "year", required = false, defaultValue = "2025") int year,
                                         Model model) {
        Map<String, BigDecimal> reportData = transactionService.getIncomeAndExpenseReport(month, year);

        // Thêm dữ liệu vào model để hiển thị trên Thymeleaf
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("totalIncome", reportData.get("totalIncome"));
        model.addAttribute("totalExpense", reportData.get("totalExpense"));
        model.addAttribute("incomePercentage", reportData.get("incomePercentage"));
        model.addAttribute("expensePercentage", reportData.get("expensePercentage"));

        return "incomeExpenseReport";
    }

    @GetMapping("/incomeExpensePercentage")
    public String getIncomeAndExpensePercentage(Model model) {
        Map<String, BigDecimal> report = transactionService.getIncomeAndExpensePercentage();

        model.addAttribute("totalIncome", report.get("totalIncome"));
        model.addAttribute("totalExpense", report.get("totalExpense"));
        model.addAttribute("incomePercentage", report.get("incomePercentage"));
        model.addAttribute("expensePercentage", report.get("expensePercentage"));

        return "incomeExpensePercentage";
    }


    @GetMapping("/categoryExpenseReport")
    public String getCategoryExpenseReport(@RequestParam(value = "month", required = false, defaultValue = "3") int month,
                                           @RequestParam(value = "year", required = false, defaultValue = "2025") int year,
                                           Model model) {
        List<Map<String, Object>> report = transactionService.getCategoryExpenseReport(month, year);

        model.addAttribute("report", report);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        return "categoryExpenseReport";
    }

    @GetMapping("/categoryIncomeReport")
    public String getCategoryIncomeReport(
            @RequestParam(value = "month", required = false, defaultValue = "3") int month,
            @RequestParam(value = "year", required = false, defaultValue = "2025") int year,
            Model model) {

        List<Map<String, Object>> report = transactionService.getCategoryIncomeReport(month, year);

        model.addAttribute("report", report);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        return "categoryIncomeReport";
    }
}
