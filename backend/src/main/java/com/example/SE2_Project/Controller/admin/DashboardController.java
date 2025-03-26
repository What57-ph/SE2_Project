package com.example.SE2_Project.Controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/show")
public class DashboardController {
    @GetMapping("")
    public String getAdminPage(){
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
    public String getUserPage(){
        return "admin/user/userList";
    }
    @GetMapping("/user/add")
    public String getUserAddPage(){
        return "admin/user/add";
    }
    @GetMapping("/user/update/{id}")
    public String getUserUpdatePage(@PathVariable("id") Long id){
        return "admin/user/update";
    }


}
