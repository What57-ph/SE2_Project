package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.IncomeTransactionDto;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Logic cập nhật thu nhập
    public TransactionEntity updateIncome(long id, IncomeTransactionDto transactionEntity) {
        Optional<TransactionEntity> transactionSearch = transactionRepository.findById(id);
        TransactionEntity transaction = transactionSearch.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khoản thu nhập"));

        // Cập nhật thông tin giao dịch
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setTransactionDate(transactionEntity.getTransactionDate());
        transaction.setNotes(transactionEntity.getNotes());
        transaction.setCreatedDate(LocalDate.now());

        // Lấy thông tin người dùng
        Optional<UserEntity> userOptional = userRepository.findById(transactionEntity.getUserId());
        UserEntity user = userOptional.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        // Lấy thông tin danh mục
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(transactionEntity.getCategoryId());
        CategoryEntity category = categoryOptional.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục"));

        // Cập nhật người dùng và danh mục vào giao dịch
        transaction.setUser(user);
        transaction.setCategory(category);

        return transactionRepository.save(transaction);
    }

    // Logic xóa thu nhập
    public void deleteIncomeTransaction(long id) {
        transactionRepository.deleteById(id);
    }
    public List<TransactionEntity> getAllIncomeTransaction() {
        return transactionRepository.findAll();  // Hoặc có thể lọc theo loại "INCOME" nếu cần
    }
}
