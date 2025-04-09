package com.example.helloworld.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a bank transaction with type, amount, and timestamp.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private TransactionType type;
    private double amount;
    private double resultingBalance;
    private LocalDateTime timestamp;
    
    public Transaction(TransactionType type, double amount, double resultingBalance) {
        this.type = type;
        this.amount = amount;
        this.resultingBalance = resultingBalance;
        this.timestamp = LocalDateTime.now();
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getResultingBalance() {
        return resultingBalance;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        String action = (type == TransactionType.DEPOSIT) ? "Deposit" : "Withdrawal";
        return String.format("[%s] %s: UGX %,.0f | Balance: UGX %,.0f", 
                timestamp.format(FORMATTER), 
                action, 
                amount, 
                resultingBalance);
    }
}
