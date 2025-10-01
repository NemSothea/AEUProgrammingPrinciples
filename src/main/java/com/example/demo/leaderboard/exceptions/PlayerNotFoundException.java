package com.example.demo.leaderboard.exceptions;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
    
    public PlayerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}