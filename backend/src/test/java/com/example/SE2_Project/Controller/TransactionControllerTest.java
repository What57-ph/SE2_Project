package com.example.SE2_Project.Controller;

import com.example.SE2_Project.Controller.pages.Transaction;
import com.example.SE2_Project.Dto.MonthlySummaryDTO;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.TransactionEntity;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private Transaction transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testAddNewTransactionPage() throws Exception {
        mockMvc.perform(get("/transactions/addNew"))
                .andExpect(status().isOk())
                .andExpect(view().name("expenses/addNew"));
    }

    @Test
    public void testAddExpenseTransaction() throws Exception {
        // Mock the authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the service call
        doNothing().when(transactionService).addExpense(any(BigDecimal.class), any(LocalDate.class), anyLong(), anyString(), anyString());

        // Call the POST method
        mockMvc.perform(post("/transactions/addExpense")
                        .param("amount", "100.00")
                        .param("transactionDate", "2025-04-15")
                        .param("categoryId", "1")
                        .param("notes", "Test expense"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/transactions"));
    }

    @Test
    public void testAddIncomeTransaction() throws Exception {
        // Mock the authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock the service call
        doNothing().when(transactionService).addIncome(any(BigDecimal.class), any(LocalDate.class), anyLong(), anyString(), anyString());

        // Call the POST method
        mockMvc.perform(post("/transactions/addIncome")
                        .param("amount", "200.00")
                        .param("transactionDate", "2025-04-15")
                        .param("categoryId", "2")
                        .param("notes", "Test income"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/transactions"));
    }

    @Test
    public void testListTransactions() throws Exception {
        // Mock the service call
        List<TransactionEntity> transactions = List.of(new TransactionEntity(), new TransactionEntity());
        when(transactionService.getTransactionsForCurrentUser()).thenReturn(transactions);

        mockMvc.perform(get("/transactions/listTransaction"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/listTransaction"))
                .andExpect(model().attribute("transactions", transactions));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        Long transactionId = 1L;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(post("/transactions/delete/{id}", transactionId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/transactions"));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        Long transactionId = 1L;
        Long categoryId = 2L;
        BigDecimal amount = BigDecimal.valueOf(150.00);
        String createdDate = "2025-04-15";

        TransactionEntity existingTransaction = new TransactionEntity();
        existingTransaction.setId(transactionId);
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        existingTransaction.setCategory(category);
        existingTransaction.setAmount(amount);
        existingTransaction.setCreatedDate(LocalDate.parse(createdDate));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        mockMvc.perform(post("/transactions/update")
                        .param("id", String.valueOf(transactionId))
                        .param("categoryId", String.valueOf(categoryId))
                        .param("amount", amount.toString())
                        .param("createdDate", createdDate))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/transactions"));
    }

    @Test
    public void testGetCategoryExpenseReport() throws Exception {
        mockMvc.perform(get("/transactions/categoryExpenseReport")
                        .param("month", "4")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(view().name("report/categoryExpenseReport"));
    }

    @Test
    public void testGetMonthlySummary() throws Exception {
        List<MonthlySummaryDTO> summary = List.of(new MonthlySummaryDTO(), new MonthlySummaryDTO());
        when(transactionService.getMonthlySummary(anyInt())).thenReturn(summary);

        mockMvc.perform(get("/transactions/monthly-summary")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(view().name("report/monthlySummary"))
                .andExpect(model().attribute("summary", summary));
    }
}