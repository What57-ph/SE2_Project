package com.example.SE2_Project.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;
    private String type;
    private LocalDate createdDate;
    private LocalDate transactionDate;
    private boolean status;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}
