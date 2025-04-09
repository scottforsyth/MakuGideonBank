package com.example.helloworld.service;

import com.example.helloworld.model.Account;
import com.example.helloworld.model.Transaction;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing bank accounts and transactions.
 */
public class BankService {
    private static final String DATA_DIRECTORY = "data";
    private static final String ACCOUNTS_CSV = DATA_DIRECTORY + File.separator + "accounts.csv";
    private static final String ACCOUNT_BALANCES_CSV = DATA_DIRECTORY + File.separator + "account_balances.csv";
    private static final String TRANSACTIONS_CSV = DATA_DIRECTORY + File.separator + "transactions.csv";
    
    private Map<String, Account> accounts;
    
    public BankService() {
        accounts = new HashMap<>();
        loadAccounts();
    }
    
    /**
     * Creates a new account with the given account number.
     * 
     * @param accountNumber The unique account number
     * @return The newly created account
     * @throws IllegalArgumentException if the account number already exists
     */
    public Account createAccount(String accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists");
        }
        
        Account account = new Account(accountNumber);
        accounts.put(accountNumber, account);
        saveAccounts();
        return account;
    }
    
    /**
     * Creates a new account with the given account number and account holder name.
     * 
     * @param accountNumber The unique account number
     * @param accountHolderName The name of the account holder
     * @return The newly created account
     * @throws IllegalArgumentException if the account number already exists
     */
    public Account createAccount(String accountNumber, String accountHolderName) {
        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists");
        }
        
        Account account = new Account(accountNumber, accountHolderName);
        accounts.put(accountNumber, account);
        saveAccounts();
        return account;
    }
    
    /**
     * Updates the account holder name for an existing account.
     * 
     * @param accountNumber The account number
     * @param accountHolderName The new account holder name
     * @throws IllegalArgumentException if the account is not found
     */
    public void updateAccountHolderName(String accountNumber, String accountHolderName) {
        Account account = getAccount(accountNumber);
        account.setAccountHolderName(accountHolderName);
        saveAccounts();
    }
    
    /**
     * Gets an account by its account number.
     * 
     * @param accountNumber The account number to look up
     * @return The account if found
     * @throws IllegalArgumentException if the account is not found
     */
    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        return account;
    }
    
    /**
     * Gets all accounts in the system.
     * 
     * @return A map of account numbers to accounts
     */
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>(accounts);
    }
    
    /**
     * Deposits money into an account.
     * 
     * @param accountNumber The account number
     * @param amount The amount to deposit
     * @throws IllegalArgumentException if the account is not found or amount is invalid
     */
    public void deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.deposit(amount);
        saveAccounts();
    }
    
    /**
     * Withdraws money from an account.
     * 
     * @param accountNumber The account number
     * @param amount The amount to withdraw
     * @throws IllegalArgumentException if the account is not found, amount is invalid, or insufficient funds
     */
    public void withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        account.withdraw(amount);
        saveAccounts();
    }
    
    /**
     * Gets the transaction history for an account.
     * 
     * @param accountNumber The account number
     * @return The list of transactions
     * @throws IllegalArgumentException if the account is not found
     */
    public List<Transaction> getTransactionHistory(String accountNumber) {
        Account account = getAccount(accountNumber);
        return account.getTransactionHistory();
    }
    
    /**
     * Saves all accounts to CSV files.
     */
    private void saveAccounts() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        // Save account balances to CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNT_BALANCES_CSV))) {
            // Write header
            writer.println("accountNumber,accountHolderName,balance");
            
            // Write account data
            for (Account account : accounts.values()) {
                writer.println(account.getAccountNumber() + "," + 
                               account.getAccountHolderName() + "," + 
                               account.getBalance());
            }
            
            System.out.println("Saved " + accounts.size() + " accounts to " + ACCOUNT_BALANCES_CSV);
        } catch (IOException e) {
            System.err.println("Error saving accounts to CSV: " + e.getMessage());
        }
        
        // Save transactions to CSV
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_CSV))) {
            // Write header
            writer.println("accountNumber,timestamp,type,amount,resultingBalance");
            
            // Write transaction data
            for (Account account : accounts.values()) {
                String accountNumber = account.getAccountNumber();
                for (Transaction transaction : account.getTransactionHistory()) {
                    writer.println(accountNumber + "," + 
                                   transaction.getTimestamp() + "," + 
                                   transaction.getType() + "," + 
                                   transaction.getAmount() + "," + 
                                   transaction.getResultingBalance());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions to CSV: " + e.getMessage());
        }
    }
    
    /**
     * Loads all accounts from CSV files.
     */
    private void loadAccounts() {
        File accountsFile = new File(ACCOUNTS_CSV);
        File balancesFile = new File(ACCOUNT_BALANCES_CSV);
        
        // First try to load from account_balances.csv (has current balances)
        if (balancesFile.exists()) {
            try {
                // Skip the header line and process each account line
                List<String> lines = Files.lines(Paths.get(ACCOUNT_BALANCES_CSV))
                        .skip(1) // Skip header
                        .collect(Collectors.toList());
                
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String accountNumber = parts[0];
                        String accountHolderName = parts[1];
                        double balance = 0.0;
                        
                        // Parse balance
                        try {
                            balance = Double.parseDouble(parts[2]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid balance format for account " + accountNumber + ": " + parts[2]);
                        }
                        
                        Account account = new Account(accountNumber, accountHolderName);
                        
                        // If balance is not zero, simulate a deposit to create a transaction record
                        if (balance > 0) {
                            account.deposit(balance);
                        }
                        
                        accounts.put(accountNumber, account);
                    }
                }
                
                System.out.println("Loaded " + accounts.size() + " accounts from " + ACCOUNT_BALANCES_CSV);
                return;
            } catch (IOException e) {
                System.err.println("Error loading accounts from " + ACCOUNT_BALANCES_CSV + ": " + e.getMessage());
            }
        }
        
        // If account_balances.csv doesn't exist, try accounts.csv
        if (accountsFile.exists()) {
            try {
                // Skip the header line and process each account line
                List<String> lines = Files.lines(Paths.get(ACCOUNTS_CSV))
                        .skip(1) // Skip header
                        .collect(Collectors.toList());
                
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String accountNumber = parts[0];
                        String accountHolderName = parts[1];
                        double balance = 0.0;
                        
                        // Parse balance if available
                        if (parts.length >= 3 && !parts[2].isEmpty()) {
                            try {
                                balance = Double.parseDouble(parts[2]);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid balance format for account " + accountNumber + ": " + parts[2]);
                            }
                        }
                        
                        Account account = new Account(accountNumber, accountHolderName);
                        
                        // If balance is not zero, simulate a deposit to create a transaction record
                        if (balance > 0) {
                            account.deposit(balance);
                        }
                        
                        accounts.put(accountNumber, account);
                    }
                }
                
                System.out.println("Loaded " + accounts.size() + " accounts from " + ACCOUNTS_CSV);
                
                // Save to account_balances.csv for future use
                saveAccounts();
            } catch (IOException e) {
                System.err.println("Error loading accounts from " + ACCOUNTS_CSV + ": " + e.getMessage());
            }
        }
    }
}
