package com.example.SE2_Project.Controller.admin;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import com.example.SE2_Project.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin/show")
public class DashboardController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    public DashboardController(UserRepository userRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("")
    public String getAdminPage(Model model) {
        long userCount = userRepository.count();
        long transactionCount = transactionRepository.count();
        long categoryCount = categoryRepository.count();
        model.addAttribute("userCount", userCount);
        model.addAttribute("transactionCount", transactionCount);
        model.addAttribute("categoryCount", categoryCount);
        return "admin/show";
    }

    @GetMapping("/category")
    public String getCategoryPage() {
        return "admin/category/categoryPage";
    }

    @GetMapping("/category/add")
    public String getCategoryAddPage() {
        return "admin/category/add";
    }

    @GetMapping("/category/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id) {
        return "admin/category/update";
    }

    @GetMapping("/transaction")
    public String getTransactionPage(Model model) {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        model.addAttribute("transactions", transactions);
        return "admin/transaction/transactionList";
    }

    @GetMapping("/transaction/add")
    public String getTransactionAddPage() {
        return "admin/transaction/add";
    }

    @GetMapping("/transaction/update/{id}")
    public String getTransactionUpdatePage(@PathVariable("id") Long id, Model model) {
        TransactionEntity tr = transactionRepository.findById(id).orElse(null);
        if (tr != null) {
            long userId = tr.getUser().getId();
            List<CategoryEntity> categories = categoryRepository.findByUsers_IdAndType(userId, tr.getType());
            model.addAttribute("transaction", tr);
            model.addAttribute("categories", categories);
        }
        return "admin/transaction/update";
    }

    @PostMapping("/transaction/update")
    public String updateTransaction(@ModelAttribute TransactionEntity transactionForm) {
        TransactionEntity trans = transactionRepository.findById(transactionForm.getId()).orElseThrow();

        trans.setAmount(transactionForm.getAmount());
        trans.setTransactionDate(transactionForm.getTransactionDate());
        trans.setNotes(transactionForm.getNotes());
        trans.setCategory(transactionForm.getCategory());

        transactionRepository.save(trans);
        return "redirect:/admin/show/transaction";
    }


    @GetMapping("/user")
    public String getUserPage(Model model,
                              @RequestParam(value = "role", required = false, defaultValue = "default") String role,
                              @RequestParam(value = "status", required = false, defaultValue = "0") int status
    ) {
        List<UserEntity> users ;

        boolean dbStatus = status==1;

        if (!role.equals("default") && status != 0) {
            users = userRepository.findByRoleAndStatus(role, dbStatus);
        } else if (role.equals("default") && status != 0) {
            users = userRepository.findByStatus(dbStatus);
        } else if (!role.equals("default")) {
            users = userRepository.findByRole(role);
        } else {
            users = userService.getAllUsers(); // Lấy tất cả user
        }
        model.addAttribute("users", users);
        model.addAttribute("activeIcon", "✅" + "Active");
        model.addAttribute("deletedIcon", "❌" + "Disabled");

        return "admin/user/userList";
    }

    @GetMapping("/user/add")
    public String getUserAddPage(Model model) {
        UserEntity newUser = new UserEntity();
        model.addAttribute("newUser", newUser);
        return "admin/user/addUser";
    }

    @PostMapping("/user/create")
    public String createUser(UserEntity newUser) {
        userService.createUser(newUser);
        return "redirect:/admin/show/user";
    }

    @GetMapping("/user/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") Long id, Model model) {
        UserEntity user = userRepository.getReferenceById(id);
        model.addAttribute("user", user);
        return "admin/user/update";
    }

    @PostMapping(value = "/user/save")
    public String saveUpdate(UserEntity user, @RequestParam(value = "newPassword", required = false) String newPassword) {
        UserEntity existingUser = userRepository.getReferenceById(user.getId());
        // Cập nhật chỉ những trường có trong form
        existingUser.setName(user.getName());
        existingUser.setUsername(user.getUsername());
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }
        existingUser.setRole(user.getRole());
        existingUser.setStatus(user.isStatus());

        // Lưu lại entity sau khi cập nhật
        userRepository.save(existingUser);
        return "redirect:/admin/show/user";
    }

    @PostMapping(value = "/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/show/user";
    }

    @PostMapping(value = "/user/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return "redirect:/admin/show/user";
    }
}
