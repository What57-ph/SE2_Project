package com.example.SE2_Project.Controller.admin;

import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/show")
public class DashboardController {

    private final UserRepository userRepository;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String getAdminPage(Model model){
        long userCount = userRepository.count();
        model.addAttribute("userCount", userCount);
        return "admin/show";
    }

    @GetMapping("/category")
    public String getCategoryPage(){
        return "admin/category/categoryPage";
    }
    @GetMapping("/category/add")
    public String getCategoryAddPage(){
        return "admin/category/add";
    }
    @GetMapping("/category/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id){
        return "admin/category/update";
    }
    @GetMapping("/transaction")
    public String getTransactionPage(){
        return "admin/transaction/transactionList";
    }
    @GetMapping("/transaction/add")
    public String getTransactionAddPage(){
        return "admin/transaction/add";
    }
    @GetMapping("/transaction/update/{id}")
    public String getTransactionUpdatePage(@PathVariable("id") Long id){
        return "admin/transaction/update";
    }

    @GetMapping("/user")
    public String getUserPage(Model model){
        List<UserEntity> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user/userList";
    }

    @GetMapping("/user/add")
    public String getUserAddPage(){
        return "admin/user/add";
    }

    @GetMapping("/user/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") Long id, Model model){
        UserEntity user = userRepository.getReferenceById(id);
        model.addAttribute("user", user);
        return "admin/user/update";
    }

    @PostMapping(value = "/user/save")
    public String saveUpdate(UserEntity user){
        userRepository.save(user);
        return "redirect:/admin/show/user";
    }

}
