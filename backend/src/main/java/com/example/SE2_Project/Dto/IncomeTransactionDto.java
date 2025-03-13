package com.example.SE2_Project.Dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeTransactionDto {
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String notes;
}
