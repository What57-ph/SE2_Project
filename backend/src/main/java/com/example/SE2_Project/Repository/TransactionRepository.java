package com.example.SE2_Project.Repository;

import com.example.SE2_Project.Entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE " +
            "MONTH(t.transactionDate) = :month AND " +
            "YEAR(t.transactionDate) = :year AND " +
            "t.type = :type")
    List<TransactionEntity> findByMonthAndType(int month, int year, String type);

    @Query("SELECT t FROM TransactionEntity t WHERE " +
            "t.amount >= :minAmount AND " +
            "t.amount <= :maxAmount AND " +
            "t.type = :type")
    List<TransactionEntity> findByAmountAndType(BigDecimal minAmount, BigDecimal maxAmount, String type);

    @Query("SELECT EXTRACT(MONTH FROM t.transactionDate) AS month, " +
            "EXTRACT(YEAR FROM t.transactionDate) AS year, " +
            "t.category.name AS category, " +
            "SUM(t.amount) AS totalAmount, " +
            "(SUM(t.amount) / (SELECT SUM(t2.amount) FROM TransactionEntity t2 WHERE " +
            "EXTRACT(MONTH FROM t2.transactionDate) = EXTRACT(MONTH FROM t.transactionDate) AND " +
            "EXTRACT(YEAR FROM t2.transactionDate) = EXTRACT(YEAR FROM t.transactionDate) AND t2.type = 'INCOME')) * 100 AS categoryPercentage " +
            "FROM TransactionEntity t " +
            "WHERE t.type = 'INCOME' AND EXTRACT(MONTH FROM t.transactionDate) = :month AND EXTRACT(YEAR FROM t.transactionDate) = :year " +
            "GROUP BY EXTRACT(MONTH FROM t.transactionDate), EXTRACT(YEAR FROM t.transactionDate), t.category.name " +
            "ORDER BY category")
    List<Object[]> findCategoryIncomeReport(int month, int year);

    @Query("SELECT EXTRACT(MONTH FROM t.transactionDate) AS month, " +
            "EXTRACT(YEAR FROM t.transactionDate) AS year, " +
            "t.category.name AS category, " +
            "SUM(t.amount) AS totalAmount, " +
            "(SUM(t.amount) / (SELECT SUM(t2.amount) FROM TransactionEntity t2 WHERE " +
            "EXTRACT(MONTH FROM t2.transactionDate) = EXTRACT(MONTH FROM t.transactionDate) AND " +
            "EXTRACT(YEAR FROM t2.transactionDate) = EXTRACT(YEAR FROM t.transactionDate) AND t2.type = 'EXPENSE')) * 100 AS categoryPercentage " +
            "FROM TransactionEntity t " +
            "WHERE t.type = 'EXPENSE' AND EXTRACT(MONTH FROM t.transactionDate) = :month AND EXTRACT(YEAR FROM t.transactionDate) = :year " +
            "GROUP BY EXTRACT(MONTH FROM t.transactionDate), EXTRACT(YEAR FROM t.transactionDate), t.category.name " +
            "ORDER BY category")
    List<Object[]> findCategoryExpenseReport(int month, int year);

    @Query("SELECT EXTRACT(MONTH FROM t.transactionDate) AS month, " +
            "EXTRACT(YEAR FROM t.transactionDate) AS year, " +
            "SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) AS totalIncome, " +
            "SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS totalExpense " +
            "FROM TransactionEntity t " +
            "WHERE EXTRACT(MONTH FROM t.transactionDate) = :month AND EXTRACT(YEAR FROM t.transactionDate) = :year " +
            "GROUP BY EXTRACT(MONTH FROM t.transactionDate), EXTRACT(YEAR FROM t.transactionDate)")
    List<Object[]> findIncomeAndExpenseReport(int month, int year);

}
