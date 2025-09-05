package com.example.demo.secureloginsystem.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginLogger {
    private static final String LOG_FILE = "src/main/java/com/example/demo/secureloginsystem/utils/login_attempts.log";
    private static LoginLogger instance;

    private LoginLogger() {
    }

    public static synchronized LoginLogger getInstance() {
        if (instance == null) {
            instance = new LoginLogger();
        }
        return instance;
    }

    public void logAttempt(String username, boolean success, String ipAddress) {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String status = success ? "SUCCESS" : "FAILURE";

        String logMessage = String.format("[%s] Login %s - Username: %s, IP: %s",
                timestamp, status, username, ipAddress);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logMessage);
            System.out.println("Logged: " + logMessage);
        } catch (IOException e) {
            System.err.println("Warning: Could not write to log file: " + e.getMessage());
        }
    }

    public static void logError(String username, String errorMessage) {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logMessage = String.format("[%s] ERROR - Username: %s, Message: %s",
                timestamp, username, errorMessage);

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(logMessage);
            System.out.println("Error logged: " + logMessage);
        } catch (IOException e) {
            System.err.println("Warning: Could not write to log file: " + e.getMessage());
        }
    }
}