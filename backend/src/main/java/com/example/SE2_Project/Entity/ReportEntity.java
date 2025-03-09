package com.example.SE2_Project.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class ReportEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalIncome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalExpense;

    @Column(nullable = false)
    private Integer reportMonth;

    @Column(nullable = false)
    private Integer reportYear;



}

