package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.IncomeTransactionDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategryRepository categoryRepository;

    public TransactionEntity addIncome(IncomeTransactionDto transactionEntity) {
        Optional<UserEntity> userOptional = userRepository.findById(transactionEntity.getUserId());
        UserEntity user = userOptional.orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(transactionEntity.getCategoryId());
        CategoryEntity category = categoryOptional.orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));

        TransactionEntity transaction = new TransactionEntity();
        transaction.setAmount(transactionEntity.getAmount());
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setTransactionDate(transactionEntity.getTransactionDate());
        transaction.setNotes(transactionEntity.getNotes());
        transaction.setStatus(true);
        transaction.setCreatedDate(LocalDate.now());
        transaction.setType("INCOME");

        return transactionRepository.save(transaction);

    }


    public TransactionEntity updateIncome(long id, IncomeTransactionDto transactionEntity) {
        Optional<TransactionEntity> transactionSearch = transactionRepository.findById(id);
        TransactionEntity transaction = transactionSearch.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khoản thu nhập"));

        Optional<UserEntity> userOptional = userRepository.findById(transactionEntity.getUserId());
        UserEntity user = userOptional.orElseThrow(() -> new IllegalArgumentException("User khong ton tai "));

        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(transactionEntity.getCategoryId());
        CategoryEntity category = categoryOptional.orElseThrow(() -> new IllegalArgumentException("Danh muc khong tai."));

        transaction.setAmount(transactionEntity.getAmount());
        transaction.setTransactionDate(transactionEntity.getTransactionDate());
        transaction.setNotes(transactionEntity.getNotes());
        transaction.setCategory(category);
        transaction.setCreatedDate(LocalDate.now());

        return transactionRepository.save(transaction);

    }

    public void deleteIncomeTransaction(long id) {
         transactionRepository.deleteById(id);
    }
}
