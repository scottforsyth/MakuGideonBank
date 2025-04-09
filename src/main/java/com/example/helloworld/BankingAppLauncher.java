package com.example.helloworld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Launcher application that allows the user to choose between
 * the GUI version and the command-line version of the Banking Application.
 */
public class BankingAppLauncher extends JFrame {
    
    public BankingAppLauncher() {
        // Set up the window
        setTitle("Banking Application Launcher");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        // Create a panel with a border layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create a label for the title
        JLabel titleLabel = new JLabel("Banking Application");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Create a label for the instructions
        JLabel instructionsLabel = new JLabel("Select the version you want to run:");
        instructionsLabel.setHorizontalAlignment(JLabel.CENTER);
        
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // Create buttons for the GUI and command-line versions
        JButton guiButton = new JButton("GUI Version");
        guiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchGUIVersion();
            }
        });
        
        JButton cliButton = new JButton("Command-Line Version");
        cliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchCommandLineVersion();
            }
        });
        
        // Add the buttons to the button panel
        buttonPanel.add(guiButton);
        buttonPanel.add(cliButton);
        
        // Add the components to the main panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(instructionsLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add the panel to the content pane
        getContentPane().add(panel);
    }
    
    private void launchGUIVersion() {
        // Hide the launcher window
        setVisible(false);
        
        // Launch the GUI version
        SwingUtilities.invokeLater(() -> {
            BankingAppGUI app = new BankingAppGUI();
            app.setVisible(true);
        });
        
        // Dispose the launcher window
        dispose();
    }
    
    private void launchCommandLineVersion() {
        // Hide the launcher window
        setVisible(false);
        
        // Launch the command-line version in a new thread
        new Thread(() -> {
            BankingApp.main(new String[0]);
        }).start();
        
        // Dispose the launcher window
        dispose();
    }
    
    public static void main(String[] args) {
        // Check if there are any command-line arguments
        if (args.length > 0) {
            // If the first argument is "cli", launch the command-line version
            if (args[0].equalsIgnoreCase("cli")) {
                BankingApp.main(new String[0]);
                return;
            }
            // If the first argument is "gui", launch the GUI version
            else if (args[0].equalsIgnoreCase("gui")) {
                SwingUtilities.invokeLater(() -> {
                    BankingAppGUI app = new BankingAppGUI();
                    app.setVisible(true);
                });
                return;
            }
        }
        
        // If no arguments or invalid arguments, show the launcher
        SwingUtilities.invokeLater(() -> {
            BankingAppLauncher launcher = new BankingAppLauncher();
            launcher.setVisible(true);
        });
    }
}
