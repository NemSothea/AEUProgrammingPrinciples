package com.example.demo.secureloginsystem.servics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.demo.secureloginsystem.models.User;
import com.example.demo.secureloginsystem.utils.LoginLogger;
import com.example.demo.secureloginsystem.utils.SecurityUtils;

public class SecureLoginSystem {

    private static final String USER_FILE = "src/main/java/com/example/demo/secureloginsystem/utils/users.txt";
    private static final int MAX_ATTEMPTS = 3;
    private final LoginLogger logger;
    private List<User> users;

    public SecureLoginSystem() {
        this.logger = LoginLogger.getInstance();
        this.users = new ArrayList<>();
        initializeUserFile();
        loadUsers();
    }

    private void initializeUserFile() {

        File file = new File(USER_FILE);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
                // Create default admin user with hashed password "admin123"
                String salt = SecurityUtils.generateSalt();
                String hashedPassword = SecurityUtils.hashPassword("admin123", salt);
                writer.println("admin:" + hashedPassword + ":" + salt);
                System.out.println("Created default user file with admin account");
            } catch (IOException e) {
                System.err.println("Error creating user file: " + e.getMessage());
            }
        }
    }

    private void loadUsers() {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(":");
                if (parts.length != 3) {
                    LoginLogger.logError("SYSTEM", "Invalid user format at line " + lineNumber);
                    continue;
                }

                String username = parts[0].trim();
                String passwordHash = parts[1].trim();
                users.add(new User(username, passwordHash));
            }

            System.out.println("Loaded " + users.size() + " users from file");

        } catch (IOException e) {
            LoginLogger.logError("SYSTEM", "Failed to load users: " + e.getMessage());
            throw new RuntimeException("Cannot load user database", e);
        }
    }

    public boolean authenticate(String username, String password, String ipAddress) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            logger.logAttempt(username, false, ipAddress);
            return false;
        }

        try {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    // For simplicity, we're storing salt with password in the same line
                    // In real system, you'd store salt separately
                    String storedHash = user.getPasswordHash();
                    String salt = getSaltForUser(username);

                    if (salt != null && SecurityUtils.verifyPassword(password, storedHash, salt)) {
                        logger.logAttempt(username, true, ipAddress);
                        return true;
                    }
                }
            }

            logger.logAttempt(username, false, ipAddress);
            return false;

        } catch (Exception e) {
            LoginLogger.logError(username, "Authentication error: " + e.getMessage());
            return false;
        }
    }

    private String getSaltForUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(":");
                if (parts.length == 3 && parts[0].trim().equals(username)) {
                    return parts[2].trim();
                }
            }
        } catch (IOException e) {
            LoginLogger.logError(username, "Error retrieving salt: " + e.getMessage());
        }
        return null;
    }

    public void addUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }

        // Check if user already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                throw new IllegalArgumentException("User already exists");
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE, true))) {
            String salt = SecurityUtils.generateSalt();
            String hashedPassword = SecurityUtils.hashPassword(password, salt);
            writer.println(username + ":" + hashedPassword + ":" + salt);

            // Reload users to include the new one
            loadUsers();
             System.out.println("User added and users reloaded: " + username);
            logger.logAttempt("SYSTEM", true, "127.0.0.1"); // Log user creation

        } catch (IOException e) {
            LoginLogger.logError(username, "Failed to add user: " + e.getMessage());
            throw new RuntimeException("Failed to add user to database", e);
        }
    }

    public void startLoginSession() {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        System.out.println("=== Secure Login System ===");

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            // Simulate IP address (in real system, get from request)
            String ipAddress = "192.168.1." + (int) (Math.random() * 255);

            try {
                boolean authenticated = authenticate(username, password, ipAddress);

                if (authenticated) {
                    System.out.println("Login successful! Welcome, " + username);
                    showUserMenu(scanner, username);
                    break;
                } else {
                    attempts++;
                    System.out.println("Invalid credentials. Attempts remaining: " + (MAX_ATTEMPTS - attempts));

                    if (attempts >= MAX_ATTEMPTS) {
                        System.out.println("Too many failed attempts. System locked.");
                        logger.logAttempt(username, false, ipAddress);
                    }
                }
            } catch (Exception e) {
                System.out.println("System error. Please try again later.");
                LoginLogger.logError(username, "Login session error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void showUserMenu(Scanner scanner, String username) {
        while (true) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Change Password");
            System.out.println("2. View Login History");
            System.out.println("3. Logout");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    changePassword(scanner, username);
                    break;
                case "2":
                    System.out.println("Login history feature not implemented yet");
                    break;
                case "3":
                    System.out.println("Logging out... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void changePassword(Scanner scanner, String username) {
        System.out.print("Current password: ");
        String currentPassword = scanner.nextLine().trim();

        System.out.print("New password: ");
        String newPassword = scanner.nextLine().trim();

        // Verify current password first
        String ipAddress = "127.0.0.1";
        if (authenticate(username, currentPassword, ipAddress)) {
            updatePasswordInFile(username, newPassword);
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Current password is incorrect");
        }
    }

    private void updatePasswordInFile(String username, String newPassword) {
        List<String> lines = new ArrayList<>();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3 && parts[0].trim().equals(username)) {
                    String newSalt = SecurityUtils.generateSalt();
                    String newHash = SecurityUtils.hashPassword(newPassword, newSalt);
                    line = username + ":" + newHash + ":" + newSalt;
                    userFound = true;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            LoginLogger.logError(username, "Error reading user file for update: " + e.getMessage());
            return;
        }

        if (userFound) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
                loadUsers(); // Reload users
            } catch (IOException e) {
                LoginLogger.logError(username, "Error updating user file: " + e.getMessage());
            }
        }
    }
}