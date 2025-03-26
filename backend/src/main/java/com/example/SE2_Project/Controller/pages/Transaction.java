package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import com.example.SE2_Project.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class Transaction {
    @Autowired
    TransactionService transactionService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @GetMapping("/transactions/addNew")
    public String addNewTransaction(Model model) {
        // Tạo một đối tượng transaction mới
        TransactionEntity transaction = new TransactionEntity();
        List<CategoryEntity> categories = categoryService.getAllCategories();  // Lấy danh sách các category từ service
        model.addAttribute("transaction", transaction);  // Thêm transaction vào model
        model.addAttribute("categories", categories);  // Thêm danh sách category vào model
        return "expenses/addNew";  // Trả về trang addNew.html
    }
}
