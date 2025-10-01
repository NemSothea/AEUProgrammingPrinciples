package com.example.demo.leaderboard.exceptions;

public class InvalidRankException extends RuntimeException {
    private final int invalidRank;
    private final int minRank;
    private final int maxRank;
    
    public InvalidRankException(String message) {
        super(message);
        this.invalidRank = -1;
        this.minRank = -1;
        this.maxRank = -1;
    }
    
    public InvalidRankException(String message, Throwable cause) {
        super(message, cause);
        this.invalidRank = -1;
        this.minRank = -1;
        this.maxRank = -1;
    }
    
    public InvalidRankException(int invalidRank, int minRank, int maxRank) {
        super(String.format(
            "Invalid rank: %d. Rank must be between %d and %d (inclusive)", 
            invalidRank, minRank, maxRank
        ));
        this.invalidRank = invalidRank;
        this.minRank = minRank;
        this.maxRank = maxRank;
    }
    
    public InvalidRankException(int invalidRank, int totalPlayers) {
        super(String.format(
            "Invalid rank: %d. Rank must be between 1 and %d (inclusive)", 
            invalidRank, totalPlayers
        ));
        this.invalidRank = invalidRank;
        this.minRank = 1;
        this.maxRank = totalPlayers;
    }
    
    // Getters
    public int getInvalidRank() {
        return invalidRank;
    }
    
    public int getMinRank() {
        return minRank;
    }
    
    public int getMaxRank() {
        return maxRank;
    }
}
