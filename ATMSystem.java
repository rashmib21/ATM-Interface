import java.util.Scanner;

public class ATMSystem {
    
    public static void main(String[] args) {
        BankAccount userAccount = new BankAccount("123456789", "1234", 1000.00);
        ATM atmMachine = new ATM(userAccount);
        atmMachine.start();
    }
}

class BankAccount {
    private String accountNumber;
    private String pin;
    private double balance;
    
    public BankAccount(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
    }
    
    public boolean verifyPin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    public boolean deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    public String getAccountNumber() {
        return this.accountNumber;
    }
    
    public boolean changePin(String oldPin, String newPin) {
        if (!verifyPin(oldPin)) {
            return false;
        }
        if (newPin.length() != 4 || !newPin.matches("\\d+")) {
            return false;
        }
        this.pin = newPin;
        return true;
    }
}

class ATM {
    private BankAccount account;
    private Scanner scanner;
    private boolean isAuthenticated;
    private static final int MAX_ATTEMPTS = 3;
    
    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
        this.isAuthenticated = false;
    }
    
    public void start() {
        System.out.println("Welcome to the Java ATM System!");
        
        if (!authenticateUser()) {
            System.out.println("Too many failed attempts. Your session has ended.");
            return;
        }
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getMenuChoice(1, 5);
            
            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    withdrawMoney();
                    break;
                case 4:
                    changePin();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using our ATM. Goodbye!");
                    break;
            }
        }
        scanner.close();
    }
    
    private boolean authenticateUser() {
        int attempts = 0;
        
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("\nEnter your account number: ");
            String accNumber = scanner.nextLine();
            
            System.out.print("Enter your PIN: ");
            String pin = scanner.nextLine();
            
            if (accNumber.equals(account.getAccountNumber()) && account.verifyPin(pin)) {
                this.isAuthenticated = true;
                return true;
            }
            
            attempts++;
            System.out.println("Invalid account number or PIN. Attempts remaining: " + (MAX_ATTEMPTS - attempts));
        }
        return false;
    }
    
    private void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Change PIN");
        System.out.println("5. Exit");
        System.out.print("Please enter your choice (1-5): ");
    }
    
    private int getMenuChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.print("Invalid choice. Please enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
    
    private void checkBalance() {
        System.out.printf("\nYour current balance is: $%.2f\n", account.getBalance());
    }
    
    private void depositMoney() {
        System.out.print("\nEnter amount to deposit: $");
        double amount = getPositiveAmount();
        
        if (account.deposit(amount)) {
            System.out.printf("$%.2f has been deposited successfully.\n", amount);
            System.out.printf("New balance: $%.2f\n", account.getBalance());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
    
    private void withdrawMoney() {
        System.out.print("\nEnter amount to withdraw: $");
        double amount = getPositiveAmount();
        
        if (account.withdraw(amount)) {
            System.out.printf("$%.2f has been withdrawn successfully.\n", amount);
            System.out.printf("Remaining balance: $%.2f\n", account.getBalance());
        } else {
            System.out.println("Withdrawal failed. Insufficient funds or invalid amount.");
        }
    }
    
    private void changePin() {
        System.out.print("\nEnter current PIN: ");
        String currentPin = scanner.nextLine();
        
        if (!account.verifyPin(currentPin)) {
            System.out.println("Incorrect current PIN. PIN change failed.");
            return;
        }
        
        System.out.print("Enter new PIN (4 digits): ");
        String newPin = scanner.nextLine();
        
        System.out.print("Confirm new PIN: ");
        String confirmPin = scanner.nextLine();
        
        if (!newPin.equals(confirmPin)) {
            System.out.println("New PINs do not match.");
            return;
        }
        
        if (account.changePin(currentPin, newPin)) {
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("PIN change failed. New PIN must be 4 digits.");
        }
    }
    
    private double getPositiveAmount() {
        while (true) {
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                if (amount > 0) {
                    return amount;
                }
                System.out.print("Amount must be positive. Please try again: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a numeric value: ");
            }
        }
    }
}