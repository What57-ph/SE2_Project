package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Dto.CategoryExpenseDTO;
import com.example.SE2_Project.Dto.MonthlySummaryDTO;
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
import java.util.Set;

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
        Set<CategoryEntity> categories = categoryService.getCategoriesForCurrentUser(); // Retrieve all categories

        // Set the transaction and categories to the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categories);

        return "expenses/addNew";
    }
    @PostMapping("/addExpense")
    public String addExpenseTransaction(@RequestParam("amount") BigDecimal amount,
                                        @RequestParam("transactionDate") LocalDate transactionDate,
                                        @RequestParam("categoryId") Long categoryId,
                                        @RequestParam("notes") String notes,
                                        RedirectAttributes redirectAttributes
                                        ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }

        transactionService.addExpense(amount, transactionDate, categoryId, notes, username);
        redirectAttributes.addFlashAttribute("success", "Create new transaction successfully.");


        return "redirect:/user/transactions";
    }

    @PostMapping("/addIncome")
    public String addIncomeTransaction(@RequestParam("amount") BigDecimal amount,
                                       @RequestParam("transactionDate") LocalDate transactionDate,
                                       @RequestParam("categoryId") Long categoryId,
                                       @RequestParam("notes") String notes
                                       ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must not be null");
        }
        transactionService.addIncome(amount, transactionDate, categoryId, notes, username);
        return "redirect:/user/transactions";
    }


    @GetMapping("/listTransaction")
    public String listTransactions(Model model) {
        List<TransactionEntity> transactions = transactionService.getTransactionsForCurrentUser();
        model.addAttribute("transactions", transactions);
        return "transaction/listTransaction";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transactionService.deleteTransaction(id);
            redirectAttributes.addFlashAttribute("successMessage", "Transaction deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting transaction.");
        }
        return "redirect:/user/transactions";
    }

    @PostMapping("/update")
    public String updateTransaction(@RequestParam Long id,
                                    @RequestParam Long categoryId,
                                    @RequestParam BigDecimal amount,
                                    @RequestParam String createdDate,
                                    RedirectAttributes redirectAttributes) {
        TransactionEntity existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existingTransaction.setCategory(category);
        existingTransaction.setAmount(amount);
        existingTransaction.setCreatedDate(LocalDate.parse(createdDate));

        transactionRepository.save(existingTransaction);
        redirectAttributes.addFlashAttribute("updateMessage","Update transaction successfully");
        return "redirect:/user/transactions";
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
    public String getIncomeAndExpensePercentage(@RequestParam(value = "month", required = false, defaultValue = "3") int month,
                                                @RequestParam(value = "year", required = false, defaultValue = "2025") int year,
                                                Model model) {

        List<Map<String, Object>> report = transactionService.getCategoryIncomeReport(month, year);
        model.addAttribute("report", report);
        model.addAttribute("month" , month);
        model.addAttribute("year" , year);


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
        model.addAttribute("reportType", "expense");

        return "report/categoryExpenseReport";
    }

    @ResponseBody
    @GetMapping("/incomeChartData")
    public List<Map<String, Object>> getIncomeChartData(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {

        return transactionService.getCategoryIncomeReport(month, year);
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
        model.addAttribute("reportType", "income");

        return "report/categoryExpenseReport";
    }

    @GetMapping("/monthly-summary")
    public String getMonthlySummary(
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {

        if (year == null) {
            year = LocalDate.now().getYear();  // Lấy năm hiện tại nếu không được truyền vào
        }

        List<MonthlySummaryDTO> monthlySummary = transactionService.getMonthlySummary(year);
        model.addAttribute("summary", monthlySummary);
        model.addAttribute("year", year);

        return "report/monthlySummary";
    }

    @GetMapping("/currentMonthExpensePie")
    public String viewCurrentMonthExpensePieChart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<CategoryExpenseDTO> data = transactionService.getCurrentMonthExpenseCategoryTotals(username);

        // Tách dữ liệu ra để gán vào model (labels + values)
        List<String> categories = data.stream().map(CategoryExpenseDTO::getCategory).toList();
        List<BigDecimal> amounts = data.stream().map(CategoryExpenseDTO::getTotalAmount).toList();

        model.addAttribute("categories", categories);
        model.addAttribute("amounts", amounts);

        return "report/currentMonthExpensePie"; // trỏ đến file HTML Thymeleaf
    }

    @GetMapping("/currentMonthIncomePie")
    public String viewCurrentMonthIncomePieChart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<CategoryExpenseDTO> data = transactionService.getCurrentMonthIncomeCategoryTotals(username);

        List<String> categories = data.stream().map(CategoryExpenseDTO::getCategory).toList();
        List<BigDecimal> amounts = data.stream().map(CategoryExpenseDTO::getTotalAmount).toList();

        model.addAttribute("categories", categories);
        model.addAttribute("amounts", amounts);

        return "report/currentMonthIncomePie";  // trỏ tới view HTML
    }
}
