package com.example.SE2_Project.Controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    @GetMapping("")
    public String getAdminPage(){
        return "admin/show";
    }
    @GetMapping("/category")
    public String getCategoryPage(){
        return "admin/category/categoryPage";
    }
    @GetMapping("/category/update/{id}")
    public String getUpdatePage(@PathVariable("id") Long id){
        return "admin/category/update/"+id;
    }
    @GetMapping("/transaction")
    public String getTransactionPage(){
        return "admin/transaction/transactionList";
    }
    @GetMapping("/transaction/update/{id}")
    public String getTransactionUpdatePage(@PathVariable("id") Long id){
        return "admin/transaction/update/"+id;
    }
    @GetMapping("/user")
    public String getUserPage(){
        return "admin/user/userList";
    }
    @GetMapping("/user/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") Long id){
        return "admin/user/update"+id;
    }
}
