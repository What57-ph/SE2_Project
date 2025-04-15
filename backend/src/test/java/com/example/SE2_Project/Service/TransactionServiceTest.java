package com.example.SE2_Project.Service;


import com.example.SE2_Project.Dto.ExpenseTransactionDto;
import com.example.SE2_Project.Dto.IncomeTransactionDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private UserEntity user;
    private CategoryEntity category;

    @BeforeEach
    public void setUp() {
        // Set up mock user and category
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");

        category = new CategoryEntity();
        category.setId(1L);
        category.setName("Food");
    }

    @Test
    public void testAddExpense() {
        // Given
        BigDecimal amount = new BigDecimal("100.00");
        LocalDate transactionDate = LocalDate.now();
        Long categoryId = 1L;
        String notes = "Test Expense";

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        transactionService.addExpense(amount, transactionDate, categoryId, notes, "testUser");

        // Then
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    public void testAddIncome() {
        // Given
        BigDecimal amount = new BigDecimal("200.00");
        LocalDate transactionDate = LocalDate.now();
        Long categoryId = 1L;
        String notes = "Test Income";

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        transactionService.addIncome(amount, transactionDate, categoryId, notes, "testUser");

        // Then
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    public void testUpdateIncomeTransaction() {
        // Given
        Long transactionId = 1L;
        IncomeTransactionDto incomeTransactionDto = new IncomeTransactionDto();
        incomeTransactionDto.setAmount(new BigDecimal("300.00"));
        incomeTransactionDto.setTransactionDate(LocalDate.now());
        incomeTransactionDto.setNotes("Updated income");
        incomeTransactionDto.setCategoryId(1L);
        incomeTransactionDto.setUserId(1L);

        TransactionEntity existingTransaction = new TransactionEntity();
        existingTransaction.setId(transactionId);
        existingTransaction.setAmount(new BigDecimal("200.00"));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When
        TransactionEntity updatedTransaction = transactionService.updateIncome(transactionId, incomeTransactionDto);

        // Then
        assertEquals(new BigDecimal("300.00"), updatedTransaction.getAmount());
        verify(transactionRepository, times(1)).save(updatedTransaction);
    }

    @Test
    public void testDeleteIncomeTransaction() {
        // Given
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(new TransactionEntity()));

        // When
        transactionService.deleteIncomeTransaction(transactionId);

        // Then
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    public void testGetTransactionsForCurrentUser() {
        // Given
        String username = "testUser";
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(1L);

        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(username);
        when(transactionRepository.findByUserUsername(username)).thenReturn(List.of(transaction));

        // When
        var transactions = transactionService.getTransactionsForCurrentUser();

        // Then
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transaction.getId(), transactions.get(0).getId());
    }
}
