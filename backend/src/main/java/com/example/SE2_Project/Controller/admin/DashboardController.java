package com.example.SE2_Project.Controller.admin;

import com.example.SE2_Project.Entity.UserEntity;
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

import static java.rmi.server.LogStream.log;

@Slf4j
@Controller
@RequestMapping("/admin/show")
public class DashboardController {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String getAdminPage(Model model) {
        long userCount = userRepository.count();
        model.addAttribute("userCount", userCount);
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
    public String getTransactionPage() {
        return "admin/transaction/transactionList";
    }

    @GetMapping("/transaction/add")
    public String getTransactionAddPage() {
        return "admin/transaction/add";
    }

    @GetMapping("/transaction/update/{id}")
    public String getTransactionUpdatePage(@PathVariable("id") Long id) {
        return "admin/transaction/update";
    }

    @GetMapping("/user")
    public String getUserPage(Model model) {
        List<UserEntity> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("activeIcon", "✅");
        model.addAttribute("deletedIcon", "❌");

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
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "redirect:/admin/show/user";
    }
}
