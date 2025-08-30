package com.example.demo.grademanagementsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Custom Logger class
class CustomLogger {
    private static CustomLogger instance;
    private final String logFile;
    
    private CustomLogger() {
        this.logFile = "src/main/java/com/example/demo/grademanagementsystem/grade_management.log";
    }
    
    public static CustomLogger getInstance() {
        if (instance == null) {
            instance = new CustomLogger();
        }
        return instance;
    }
    
    public void log(String message) {
        String timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = "[" + timestamp + "] " + message;
        
        // Print to console
        System.out.println(logMessage);
        
        // Write to log file
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.println(logMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    public void logError(String message, Exception e) {
        log("ERROR: " + message + " - " + e.getMessage());
    }
    
    public void logWarning(String message) {
        log("WARNING: " + message);
    }
    
    public void logInfo(String message) {
        log("INFO: " + message);
    }
}

