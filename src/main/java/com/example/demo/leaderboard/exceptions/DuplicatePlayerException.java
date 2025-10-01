package com.example.demo.leaderboard.exceptions;

public class DuplicatePlayerException extends RuntimeException {
    
    public DuplicatePlayerException(String message) {
        super(message);
    }
    
    public DuplicatePlayerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DuplicatePlayerException(String playerName, int existingScore) {
        super(String.format("Player '%s' already exists in the leaderboard with score: %d", 
                           playerName, existingScore));
    }
}
