    package com.example.SE2_Project.Controller;

    import com.example.SE2_Project.Dto.ExpenseTransactionDto;
    import com.example.SE2_Project.Dto.IncomeTransactionDto;
    import com.example.SE2_Project.Entity.TransactionEntity;
    import com.example.SE2_Project.Service.TransactionService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.math.BigDecimal;
    import java.util.List;
    import java.util.Map;

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
        @PostMapping("/delete/{id}")
        public void deleteTransaction(@PathVariable long id) {
            transactionService.deleteIncomeTransaction(id);
        }


        @PostMapping("/addExpense")
        public TransactionEntity addExpenseTransaction(@RequestBody ExpenseTransactionDto transactionDto) {
            return transactionService.addExpenseTransaction(transactionDto);
        }

        @PutMapping("/updateExpense/{id}")
        public TransactionEntity updateExpenseTransaction(@PathVariable Long id, @RequestBody ExpenseTransactionDto transactionDto) {
            return transactionService.updateExpenseTransaction(id, transactionDto);
        }

        @DeleteMapping("/deleteExpense/{id}")
        public void deleteExpenseTransaction(@PathVariable Long id) {
            transactionService.deleteExpenseTransaction(id);
        }

        @GetMapping("/getExpense/{id}")
        public TransactionEntity getExpenseTransaction(@PathVariable Long id) {
            return transactionService.getExpenseTransaction(id);
        }

        @GetMapping("/getAllExpenses")
        public List<TransactionEntity> getAllExpenseTransactions() {
            return transactionService.getAllExpenseTransactions();
        }

        @GetMapping("/search")
        public List<TransactionEntity> getTransactionsByMonthAndType(
                @RequestParam("month") int month,
                @RequestParam("year") int year,
                @RequestParam("type") String type) {

            return transactionService.getTransactionsByMonthAndType(month, year, type);
        }

        @GetMapping("/searchByAmountAndType")
        public List<TransactionEntity> getTransactionsByAmountAndType(
                @RequestParam("minAmount") BigDecimal minAmount,
                @RequestParam("maxAmount") BigDecimal maxAmount,
                @RequestParam("type") String type) {

            return transactionService.getTransactionsByAmountAndType(minAmount, maxAmount, type);
        }
        @GetMapping("/categoryIncomeReport")
        public List<Map<String, Object>> getCategoryIncomeReport(
                @RequestParam("month") int month,
                @RequestParam("year") int year) {
            return transactionService.getCategoryIncomeReport(month, year);
        }

        @GetMapping("/categoryExpenseReport")
        public List<Map<String, Object>> getCategoryExpenseReport(
                @RequestParam("month") int month,
                @RequestParam("year") int year) {
            return transactionService.getCategoryExpenseReport(month, year);
        }

        @GetMapping("/incomeExpenseReport")
        public Map<String, BigDecimal> getIncomeExpenseReport(
                @RequestParam("month") int month,
                @RequestParam("year") int year) {
            return transactionService.getIncomeAndExpenseReport(month, year);
        }
        @GetMapping("/incomeExpensePercentage")
        public Map<String, BigDecimal> getIncomeAndExpensePercentage() {
            return transactionService.getIncomeAndExpensePercentage();
        }
    }
