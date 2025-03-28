package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Dto.ExpenseTransactionDto;
import com.example.SE2_Project.Dto.IncomeTransactionDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import com.example.SE2_Project.Service.UserService;
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
import java.util.Optional;

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
        List<CategoryEntity> categories = categoryService.getAllCategories(); // Retrieve all categories

        // Set the transaction and categories to the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categories);

        return "expenses/addNew"; // Return to the addNew.html page
    }
    @PostMapping("/addExpense")
    public String addExpenseTransaction(@ModelAttribute("transaction") TransactionEntity transaction, Model model) {
        // Check if categoryId is null or invalid
        if (transaction.getCategoryId() == null) {
            // Handle the error: category ID must be selected
            model.addAttribute("error", "Please select a category.");
            return "redirect:/transactions/addNew";  // Redirect to the form with an error message
        }

        // Lấy userId từ người dùng hiện tại đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Lấy username của người dùng
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Lấy categoryId từ form
        Long categoryId = transaction.getCategoryId();  // Get categoryId from the form input

        // Kiểm tra xem categoryId có hợp lệ không
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Tạo TransactionEntity từ form
        transaction.setAmount(transaction.getAmount());
        transaction.setTransactionDate(transaction.getTransactionDate());
        transaction.setNotes(transaction.getNotes());
        transaction.setStatus(true);  // Giá trị mặc định cho status
        transaction.setCreatedDate(LocalDate.now());
        transaction.setType("EXPENSE");

        // Gán User và Category vào Transaction
        transaction.setUser(user);
        transaction.setCategory(category);

        // Lưu Transaction vào cơ sở dữ liệu
        transactionRepository.save(transaction);

        // Redirect tới trang expense list sau khi lưu thành công
        return "redirect:/user/expense";
    }

    // Handle GET request to display list of transactions
    @GetMapping("/listTransaction")
    public String listTransactions(Model model) {
        List<TransactionEntity> transactions = transactionService.getAllExpenseTransactions();
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


}
