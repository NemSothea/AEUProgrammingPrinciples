package com.example.demo.leaderboard.algorithms.sorting;

import com.example.demo.leaderboard.algorithms.search.SearchStrategy;
import com.example.demo.leaderboard.model.Player;
import java.util.*;

public class BinarySearch implements SearchStrategy {
    
    @Override
    public Player findByName(List<Player> players, String name) {
        // Note: Binary search requires sorted list by name, but our list is sorted by score
        // For name search with binary search, we'd need a separate index sorted by name
        // For now, fall back to linear search for name
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }
    
    @Override
    public List<Player> findByScoreRange(List<Player> players, int minScore, int maxScore) {
        List<Player> result = new ArrayList<>();
        
        if (players == null || players.isEmpty()) {
            return result;
        }
        
        // Find the starting index using binary search
        int startIndex = findStartIndex(players, minScore);
        
        if (startIndex == -1) {
            return result; // No players found with score >= minScore
        }
        
        // Collect all players in the range
        for (int i = startIndex; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getScore() <= maxScore) {
                result.add(player);
            } else {
                break; // Since list is sorted by score descending
            }
        }
        
        return result;
    }
    
    /**
     * Find the first index where player score >= minScore using binary search
     */
    private int findStartIndex(List<Player> players, int minScore) {
        int left = 0;
        int right = players.size() - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int currentScore = players.get(mid).getScore();
            
            if (currentScore >= minScore) {
                result = mid; // Found a candidate, but check if there's a better one to the left
                right = mid - 1; // Move left to find the first occurrence
            } else {
                left = mid + 1; // Move right since scores are descending
            }
        }
        
        return result;
    }
    
    /**
     * Alternative implementation using Collections.binarySearch for score lookup
     */
    public List<Player> findByScoreRangeOptimized(List<Player> players, int minScore, int maxScore) {
        List<Player> result = new ArrayList<>();
        
        if (players == null || players.isEmpty()) {
            return result;
        }
        
        // Create a dummy player for binary search
        Player dummyMax = new Player("", maxScore);
        Player dummyMin = new Player("", minScore);
        
        // Find the bounds using binary search
        int maxIndex = Collections.binarySearch(players, dummyMax);
        int minIndex = Collections.binarySearch(players, dummyMin);
        
        // Handle binary search return values
        maxIndex = maxIndex >= 0 ? maxIndex : -maxIndex - 1;
        minIndex = minIndex >= 0 ? minIndex : -minIndex - 1;
        
        // Adjust indices for our descending order
        int startIndex = minIndex;
        int endIndex = maxIndex;
        
        // Collect players in range
        for (int i = startIndex; i < endIndex; i++) {
            Player player = players.get(i);
            if (player.getScore() >= minScore && player.getScore() <= maxScore) {
                result.add(player);
            }
        }
        
        return result;
    }
    
    /**
     * Binary search to find a player with exact score
     */
    public Player findByExactScore(List<Player> players, int targetScore) {
        int left = 0;
        int right = players.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Player midPlayer = players.get(mid);
            
            if (midPlayer.getScore() == targetScore) {
                return midPlayer;
            } else if (midPlayer.getScore() > targetScore) {
                // Since list is sorted descending, go right for lower scores
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return null;
    }
    
    @Override
    public String getSearchType() {
        return "Binary Search";
    }
}
