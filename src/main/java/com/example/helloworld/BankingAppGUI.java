package com.example.helloworld;

import com.example.helloworld.model.Account;
import com.example.helloworld.model.Transaction;
import com.example.helloworld.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 * A GUI-based banking application that allows users to manage accounts,
 * make deposits and withdrawals, and view transaction history.
 */
public class BankingAppGUI extends JFrame {
    private static final BankService bankService = new BankService();
    
    private JPanel mainPanel;
    private JPanel accountsPanel;
    private JPanel operationsPanel;
    private CardLayout cardLayout;
    
    private Account currentAccount;
    
    public BankingAppGUI() {
        // Set up the window
        setTitle("Banking Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Create the main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels for different screens
        createAccountsPanel();
        createOperationsPanel();
        
        // Add panels to the main panel
        mainPanel.add(accountsPanel, "accounts");
        mainPanel.add(operationsPanel, "operations");
        
        // Show the accounts panel initially
        cardLayout.show(mainPanel, "accounts");
        
        // Add the main panel to the content pane
        getContentPane().add(mainPanel);
    }
    
    private void createAccountsPanel() {
        accountsPanel = new JPanel(new BorderLayout());
        accountsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create a panel for the title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Banking Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        
        // Create a panel for the account list
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        
        // Get all accounts
        Map<String, Account> accounts = bankService.getAllAccounts();
        
        if (accounts.isEmpty()) {
            JLabel noAccountsLabel = new JLabel("No accounts found.");
            listPanel.add(noAccountsLabel);
        } else {
            for (Account account : accounts.values()) {
                JPanel accountPanel = new JPanel(new BorderLayout());
                accountPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                
                JLabel accountLabel = new JLabel(account.toString());
                accountLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
                
                JButton selectButton = new JButton("Select");
                selectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        currentAccount = account;
                        updateOperationsPanel();
                        cardLayout.show(mainPanel, "operations");
                    }
                });
                
                accountPanel.add(accountLabel, BorderLayout.CENTER);
                accountPanel.add(selectButton, BorderLayout.EAST);
                
                listPanel.add(accountPanel);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton createAccountButton = new JButton("Create New Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewAccount();
            }
        });
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAccountsPanel();
            }
        });
        
        buttonPanel.add(createAccountButton);
        buttonPanel.add(refreshButton);
        
        // Add components to the accounts panel
        accountsPanel.add(titlePanel, BorderLayout.NORTH);
        accountsPanel.add(scrollPane, BorderLayout.CENTER);
        accountsPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void createOperationsPanel() {
        operationsPanel = new JPanel(new BorderLayout());
        operationsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create a panel for the title
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Account Operations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton backButton = new JButton("Back to Accounts");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAccountsPanel();
                cardLayout.show(mainPanel, "accounts");
            }
        });
        
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(backButton, BorderLayout.EAST);
        
        // Create a panel for the account info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel accountInfoLabel = new JLabel("Select an account to view details");
        infoPanel.add(accountInfoLabel);
        
        // Create a panel for the operations buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAccount != null) {
                    performDeposit();
                }
            }
        });
        
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAccount != null) {
                    performWithdrawal();
                }
            }
        });
        
        JButton viewHistoryButton = new JButton("View Transaction History");
        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAccount != null) {
                    viewTransactionHistory();
                }
            }
        });
        
        JButton updateNameButton = new JButton("Update Account Holder Name");
        updateNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAccount != null) {
                    updateAccountHolderName();
                }
            }
        });
        
        buttonsPanel.add(depositButton);
        buttonsPanel.add(withdrawButton);
        buttonsPanel.add(viewHistoryButton);
        buttonsPanel.add(updateNameButton);
        
        // Add components to the operations panel
        operationsPanel.add(titlePanel, BorderLayout.NORTH);
        operationsPanel.add(infoPanel, BorderLayout.CENTER);
        operationsPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private void updateOperationsPanel() {
        if (currentAccount != null) {
            // Update the account info label
            JPanel infoPanel = (JPanel) operationsPanel.getComponent(1);
            JLabel accountInfoLabel = (JLabel) infoPanel.getComponent(0);
            accountInfoLabel.setText(currentAccount.toString());
        }
    }
    
    private void refreshAccountsPanel() {
        // Remove the current accounts panel
        mainPanel.remove(accountsPanel);
        
        // Create a new accounts panel
        createAccountsPanel();
        
        // Add the new accounts panel
        mainPanel.add(accountsPanel, "accounts");
        
        // Refresh the display
        revalidate();
        repaint();
    }
    
    private void createNewAccount() {
        String accountNumber = JOptionPane.showInputDialog(this, "Enter account number:");
        if (accountNumber != null && !accountNumber.isEmpty()) {
            String accountHolderName = JOptionPane.showInputDialog(this, "Enter account holder name:");
            
            try {
                Account account = bankService.createAccount(accountNumber, accountHolderName);
                JOptionPane.showMessageDialog(this, "Account created successfully: " + account);
                refreshAccountsPanel();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void performDeposit() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount (UGX):");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                bankService.deposit(currentAccount.getAccountNumber(), amount);
                JOptionPane.showMessageDialog(this, "Deposit successful!\nNew balance: UGX " + 
                        String.format("%,.0f", currentAccount.getBalance()));
                updateOperationsPanel();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void performWithdrawal() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter withdrawal amount (UGX):");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                bankService.withdraw(currentAccount.getAccountNumber(), amount);
                JOptionPane.showMessageDialog(this, "Withdrawal successful!\nNew balance: UGX " + 
                        String.format("%,.0f", currentAccount.getBalance()));
                updateOperationsPanel();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewTransactionHistory() {
        List<Transaction> transactions = bankService.getTransactionHistory(currentAccount.getAccountNumber());
        
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for this account.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction History for ").append(currentAccount.getAccountNumber());
        sb.append(" (").append(currentAccount.getAccountHolderName()).append("):\n\n");
        
        for (Transaction transaction : transactions) {
            sb.append(transaction).append("\n");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateAccountHolderName() {
        String currentName = currentAccount.getAccountHolderName();
        String newName = JOptionPane.showInputDialog(this, 
                "Current account holder name: " + (currentName.isEmpty() ? "[Not set]" : currentName) + 
                "\nEnter new account holder name:");
        
        if (newName != null && !newName.isEmpty()) {
            try {
                bankService.updateAccountHolderName(currentAccount.getAccountNumber(), newName);
                JOptionPane.showMessageDialog(this, "Account holder name updated successfully.");
                updateOperationsPanel();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        // Create and display the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            BankingAppGUI app = new BankingAppGUI();
            app.setVisible(true);
        });
    }
}
