    package com.example.SE2_Project.Controller;

    import com.example.SE2_Project.Dto.IncomeTransactionDto;
    import com.example.SE2_Project.Entity.TransactionEntity;
    import com.example.SE2_Project.Service.TransactionService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/transactions")
    public class TransactionController {

            @Autowired
             private TransactionService transactionService;


            @PostMapping("/addIncome")
               public TransactionEntity addIncomeTransaction(@RequestBody IncomeTransactionDto transaction) {
                TransactionEntity transactionEntity = transactionService.addIncome(transaction);
                return transactionEntity;

            }

            @GetMapping("/get/all")
               public List<TransactionEntity> getAllTransactions() {
               return transactionService.getAllIncomeTransaction();
            }

    }
