package com.example.SE2_Project.Controller;

import com.example.SE2_Project.Controller.pages.Transaction;
import com.example.SE2_Project.Service.CategoryService;
import com.example.SE2_Project.Service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(Transaction.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @SuppressWarnings("removal")
    @MockBean
    private CategoryService categoryService;

    // Các repository mock vì được autowired trong controller
    @MockBean private com.example.SE2_Project.Repository.CategoryRepository categoryRepository;
    @MockBean private com.example.SE2_Project.Repository.TransactionRepository transactionRepository;
    @MockBean private com.example.SE2_Project.Repository.UserRepository userRepository;

    @Test
    void testAddNewTransaction() throws Exception {
        Mockito.when(categoryService.getCategoriesForCurrentUser()).thenReturn(Set.of());

        mockMvc.perform(get("/transactions/addNew")
                        .with(user("testuser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("expenses/addNew"));
    }


    @Test
    void testAddExpenseTransaction() throws Exception {
        mockMvc.perform(post("/transactions/addExpense")
                        .with(user("testuser").roles("USER"))
                        .with(csrf())  // <- fix lỗi 403
                        .param("amount", "100")
                        .param("transactionDate", LocalDate.now().toString())
                        .param("categoryId", "1")
                        .param("notes", "Lunch"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/transactions"));
    }

    @Test
    void testGetExpenseChartData() throws Exception {
        Mockito.when(transactionService.getCategoryExpenseReport(3, 2025))
                .thenReturn(List.of(Map.of("category", "Food", "amount", 100)));

        mockMvc.perform(get("/transactions/expenseChartData")
                        .param("month", "3")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Food"));
    }
}

