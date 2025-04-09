# Banking Application

A text-based Java application that simulates a basic banking system for a school assignment.

## Project Overview

This application allows users to manage bank accounts, perform transactions (deposits and withdrawals), and view transaction history. All data is stored in CSV files for easy access and modification.

## Features

- Account management (create accounts, update account holder names)
- Process deposits and withdrawals
- Handle multiple transaction types in a single interaction
- Maintain and display transaction history
- Data persistence using CSV files

## Project Structure

```
JavaProjectGideon/
├── data/                          # Data storage directory
│   ├── account_balances.csv       # Current account balances
│   ├── accounts.csv               # Initial account setup
│   └── transactions.csv           # Transaction history
├── src/main/java/com/example/helloworld/
│   ├── model/                     # Data models
│   │   ├── Account.java           # Account class
│   │   ├── Transaction.java       # Transaction class
│   │   └── TransactionType.java   # Transaction type enum
│   ├── service/                   # Business logic
│   │   └── BankService.java       # Banking operations service
│   └── BankingApp.java            # Main application class
└── pom.xml                        # Maven project configuration
```

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven (optional, for building with Maven)

## Compiling and Running the Application

### Creating a Standalone Executable (recommended)

1. Open a terminal/command prompt
2. Navigate to the project root directory
3. Run: `mvn clean package`
4. Find the executable JAR file in the `target` directory: `target/BankingApp.jar`
5. Run the application by double-clicking the JAR file or using the command:
   ```
   java -jar target/BankingApp.jar
   ```

### Using an IDE

1. Open the project in your IDE (Eclipse, IntelliJ IDEA, VS Code, etc.)
2. Build the project using the IDE's build functionality
3. Run the `BankingApp` class

### Using Maven

1. Open a terminal/command prompt
2. Navigate to the project root directory
3. Run: `mvn clean compile`
4. Run: `mvn exec:java -Dexec.mainClass="com.example.helloworld.BankingApp"`

### Using Java Compiler Directly

1. Open a terminal/command prompt
2. Navigate to the project root directory
3. Compile the code:
   ```
   mkdir -p target/classes
   javac -d target/classes src/main/java/com/example/helloworld/*.java src/main/java/com/example/helloworld/*/*.java
   ```
4. Run the application:
   ```
   java -cp target/classes com.example.helloworld.BankingApp
   ```

## Data Files

The application uses CSV files to store data:

- `account_balances.csv`: Contains account numbers, holder names, and current balances
- `transactions.csv`: Records all transactions with timestamps

You can view and edit these files directly using a text editor or spreadsheet application.

## Usage

When you run the application, you'll see a menu with options to:

1. Select an account
2. Create a new account
3. List all accounts
0. Exit

After selecting an account, you can:

1. Make a deposit
2. Make a withdrawal
3. Perform multiple transactions
4. View transaction history
5. Update account holder name
0. Return to the main menu

## Currency

The application uses Uganda Shillings (UGX) as the currency.
