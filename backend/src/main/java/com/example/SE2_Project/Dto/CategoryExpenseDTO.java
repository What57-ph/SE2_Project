package com.example.SE2_Project.Dto;

import java.math.BigDecimal;

public class CategoryExpenseDTO {
    private String category;
    private BigDecimal totalAmount;

    public CategoryExpenseDTO(String category, BigDecimal totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
