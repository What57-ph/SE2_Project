package com.example.SE2_Project.Controller;

import com.example.SE2_Project.Dto.IncomeTransactionDto;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
        @Autowired
         private TransactionService transactionService;

        
        @PostMapping("/addIncome")
        public String addIncomeTransaction(IncomeTransactionDto transaction, Model model) {
            TransactionEntity transactionEntity = transactionService.addIncome(transaction);
            model.addAttribute("addEmployee",transactionEntity );
            return "";

        }

}
