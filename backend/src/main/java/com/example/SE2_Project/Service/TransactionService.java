package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.ExpenseTransactionDto;
import com.example.SE2_Project.Dto.IncomeTransactionDto;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Logic thêm thu nhập
    public TransactionEntity addIncome(IncomeTransactionDto transactionEntity) {

        // Tạo đối tượng giao dịch
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setTransactionDate(transactionEntity.getTransactionDate());
        transaction.setNotes(transactionEntity.getNotes());
        transaction.setStatus(true);  // Giá trị mặc định cho status
        transaction.setCreatedDate(LocalDate.now());
        transaction.setType("INCOME");

        // Lấy thông tin người dùng
        Optional<UserEntity> userOptional = userRepository.findById(transactionEntity.getUserId());
        UserEntity user = userOptional.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        // Lấy thông tin danh mục
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(transactionEntity.getCategoryId());
        CategoryEntity category = categoryOptional.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

        // Gán thông tin người dùng và danh mục vào giao dịch
        transaction.setUser(user);
        transaction.setCategory(category);

        // Lưu giao dịch vào cơ sở dữ liệu
        return transactionRepository.save(transaction);
    }

    public TransactionEntity updateIncome(long id, IncomeTransactionDto transactionEntity) {
        Optional<TransactionEntity> transactionSearch = transactionRepository.findById(id);
        TransactionEntity transaction = transactionSearch.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khoản thu nhập"));

        // Cập nhật thông tin giao dịch
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setTransactionDate(transactionEntity.getTransactionDate());
        transaction.setNotes(transactionEntity.getNotes());
        transaction.setCreatedDate(LocalDate.now());

        Optional<UserEntity> userOptional = userRepository.findById(transactionEntity.getUserId());
        UserEntity user = userOptional.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(transactionEntity.getCategoryId());
        CategoryEntity category = categoryOptional.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

        transaction.setUser(user);
        transaction.setCategory(category);

        return transactionRepository.save(transaction);
    }

    public void deleteIncomeTransaction(long id) {
        transactionRepository.deleteById(id);
    }
    public List<TransactionEntity> getAllIncomeTransaction() {
        return transactionRepository.findAll();
    }

    public TransactionEntity addExpenseTransaction(ExpenseTransactionDto transactionDto, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        CategoryEntity category = categoryRepository.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        TransactionEntity transaction = new TransactionEntity();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setNotes(transactionDto.getNotes());
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setStatus(true);
        transaction.setType("EXPENSE");  // Set type to expense

        return transactionRepository.save(transaction);
    }

    public TransactionEntity updateExpenseTransaction(Long id, ExpenseTransactionDto transactionDto) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense Transaction not found"));

        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setNotes(transactionDto.getNotes());
        transaction.setCategory(categoryRepository.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found")));

        return transactionRepository.save(transaction);
    }

    public void deleteExpenseTransaction(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense Transaction not found"));

        transactionRepository.delete(transaction);
    }

    public TransactionEntity getExpenseTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense Transaction not found"));
    }

    public List<TransactionEntity> getAllExpenseTransactions() {
        return transactionRepository.findAll();
    }

    public List<TransactionEntity> getTransactionsByMonthAndType(int month, int year, String type) {
        return transactionRepository.findByMonthAndType(month, year, type);
    }

    public List<TransactionEntity> getTransactionsByAmountAndType(BigDecimal minAmount, BigDecimal maxAmount, String type) {
        return transactionRepository.findByAmountAndType(minAmount, maxAmount, type);
    }

    public List<Object[]> getCategoryIncomeReport(int month, int year) {
        return transactionRepository.findCategoryIncomeReport(month, year);
    }

    public List<Object[]> getCategoryExpenseReport(int month, int year) {
        return transactionRepository.findCategoryExpenseReport(month, year);
    }

    public Object[] getIncomeAndExpenseReport(int month, int year) {
        List<Object[]> result = transactionRepository.findIncomeAndExpenseReport(month, year);

        if (result.isEmpty()) {
            return new Object[]{"No data found"};
        }

        Object[] data = result.get(0);

        BigDecimal totalIncome = (BigDecimal) data[2]; // Tổng thu nhập
        BigDecimal totalExpense = (BigDecimal) data[3]; // Tổng chi tiêu

        // Tính tỷ lệ phần trăm
        BigDecimal total = totalIncome.add(totalExpense);
        BigDecimal incomePercentage = total.compareTo(BigDecimal.ZERO) > 0 ? (totalIncome.divide(total, 4, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
        BigDecimal expensePercentage = total.compareTo(BigDecimal.ZERO) > 0 ? (totalExpense.divide(total, 4, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;

        return new Object[]{incomePercentage, expensePercentage};  // Trả về tỷ lệ phần trăm của INCOME và EXPENSE
    }
}
