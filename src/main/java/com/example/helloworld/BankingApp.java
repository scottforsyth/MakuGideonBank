package com.example.helloworld;

import com.example.helloworld.model.Account;
import com.example.helloworld.model.Transaction;
import com.example.helloworld.service.BankService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * A text-based banking application that allows users to manage accounts,
 * make deposits and withdrawals, and view transaction history.
 */
public class BankingApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankService bankService = new BankService();
    
    public static void main(String[] args) {
        System.out.println("Welcome to the Banking Application");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    selectAccountMenu();
                    break;
                case 2:
                    createAccountMenu();
                    break;
                case 3:
                    listAllAccounts();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the Banking Application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    

    
    private static void displayMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Select Account");
        System.out.println("2. Create New Account");
        System.out.println("3. List All Accounts");
        System.out.println("0. Exit");
    }
    
    private static void selectAccountMenu() {
        listAllAccounts();
        String accountNumber = getStringInput("Enter account number (or 0 to go back): ");
        
        if (accountNumber.equals("0")) {
            return;
        }
        
        try {
            Account account = bankService.getAccount(accountNumber);
            accountOperationsMenu(account);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void accountOperationsMenu(Account account) {
        boolean accountMenu = true;
        
        while (accountMenu) {
            System.out.println("\n===== ACCOUNT OPERATIONS =====");
            System.out.println(account);
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Multiple Transactions");
            System.out.println("4. View Transaction History");
            System.out.println("5. Update Account Holder Name");
            System.out.println("0. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    depositMenu(account);
                    break;
                case 2:
                    withdrawMenu(account);
                    break;
                case 3:
                    multipleTransactionsMenu(account);
                    break;
                case 4:
                    viewTransactionHistory(account);
                    break;
                case 5:
                    updateAccountHolderNameMenu(account);
                    break;
                case 0:
                    accountMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void depositMenu(Account account) {
        double amount = getDoubleInput("Enter deposit amount (UGX): ");
        try {
            bankService.deposit(account.getAccountNumber(), amount);
            System.out.println("Deposit successful!");
            System.out.println("New balance: UGX " + String.format("%,.0f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void withdrawMenu(Account account) {
        double amount = getDoubleInput("Enter withdrawal amount (UGX): ");
        try {
            bankService.withdraw(account.getAccountNumber(), amount);
            System.out.println("Withdrawal successful!");
            System.out.println("New balance: UGX " + String.format("%,.0f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void multipleTransactionsMenu(Account account) {
        System.out.println("\n===== MULTIPLE TRANSACTIONS =====");
        
        // First transaction: Deposit
        double depositAmount = getDoubleInput("Enter deposit amount (UGX): ");
        try {
            bankService.deposit(account.getAccountNumber(), depositAmount);
            System.out.println("Deposit successful!");
            System.out.println("Balance after deposit: UGX " + String.format("%,.0f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error with deposit: " + e.getMessage());
            return;
        }
        
        // Second transaction: Withdrawal
        double withdrawalAmount = getDoubleInput("Enter withdrawal amount (UGX): ");
        try {
            bankService.withdraw(account.getAccountNumber(), withdrawalAmount);
            System.out.println("Withdrawal successful!");
            System.out.println("Final balance: UGX " + String.format("%,.0f", account.getBalance()));
        } catch (IllegalArgumentException e) {
            System.out.println("Error with withdrawal: " + e.getMessage());
        }
    }
    
    private static void viewTransactionHistory(Account account) {
        List<Transaction> transactions = bankService.getTransactionHistory(account.getAccountNumber());
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }
        
        System.out.println("\n===== TRANSACTION HISTORY =====");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
    
    private static void createAccountMenu() {
        String accountNumber = getStringInput("Enter new account number: ");
        String accountHolderName = getStringInput("Enter account holder name: ");
        
        try {
            Account account = bankService.createAccount(accountNumber, accountHolderName);
            System.out.println("Account created successfully: " + account);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void listAllAccounts() {
        Map<String, Account> accounts = bankService.getAllAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        System.out.println("\n===== ALL ACCOUNTS =====");
        for (Account account : accounts.values()) {
            System.out.println(account);
        }
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }
    
    private static void updateAccountHolderNameMenu(Account account) {
        System.out.println("\n===== UPDATE ACCOUNT HOLDER NAME =====");
        System.out.println("Current account holder name: " + 
                (account.getAccountHolderName().isEmpty() ? "[Not set]" : account.getAccountHolderName()));
        
        String newName = getStringInput("Enter new account holder name: ");
        try {
            bankService.updateAccountHolderName(account.getAccountNumber(), newName);
            System.out.println("Account holder name updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
