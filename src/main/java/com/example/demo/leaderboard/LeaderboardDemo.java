/***
 * Exercise: Leaderboard Management System
• Create a comprehensive leaderboard system for a gaming application
• Core Requirements:
– Player Management:
• Add new players with scores
• Update existing player scores
• Remove players from leaderboard 
• Ranking Algorithms:
– Sort players by score (descending)
– Handle tie-breaking (by name alphabetically)
– Maintain top N players efficiently 
• Search Functionality: 
• Find player by name (linear search)
• Find players within score range (binary search)
• Search by rank position 
• Performance Analysis: 
– Measure time complexity of operations
– Compare different sorting algorithms
– Analyze memory usage

 * ## Key Features and Analysis

### Time Complexity Analysis:
- **Add Player**: O(1) average, O(n) worst case when resizing
- **Update Score**: O(1) + O(n log n) when sorting needed
- **Remove Player**: O(n) for list removal
- **Linear Search**: O(n)
- **Binary Search Range**: O(log n + k) where k is result size
- **Sorting**: O(n log n) for all algorithms

### Memory Usage:
- **ArrayList**: O(n) for player storage
- **HashMap**: O(n) for quick lookups
- **Overall**: O(n) space complexity

### Sorting Algorithm Comparison:
- **Collections.sort**: Optimized TimSort, stable and efficient
- **Merge Sort**: Consistent O(n log n), stable but uses extra space
- **Quick Sort**: O(n log n) average, O(n²) worst case, in-place

### Performance Optimizations:
1. **Lazy Sorting**: Only sort when necessary
2. **HashMap Lookup**: O(1) player access by name
3. **Binary Search**: Efficient range queries
4. **Top N Optimization**: O(1) after sorting

This system provides a comprehensive leaderboard management solution with performance monitoring and multiple sorting algorithm implementations.

*/
package com.example.demo.leaderboard;

import com.example.demo.leaderboard.core.GameLeaderboard;
import com.example.demo.leaderboard.core.Leaderboard;
import com.example.demo.leaderboard.model.Player;
import com.example.demo.leaderboard.algorithms.sorting.*;
import com.example.demo.leaderboard.algorithms.search.*;
import com.example.demo.leaderboard.utils.PerformanceMetrics;

import java.util.List;

public class LeaderboardDemo {
    public static void main(String[] args) {
        System.out.println("=== LEADERBOARD MANAGEMENT SYSTEM DEMO ===\n");
        
        // Create leaderboard with MergeSort and LinearSearch
        Leaderboard leaderboard = new GameLeaderboard(new MergeSort(), new LinearSearch());
        
        // Demo basic operations
        demoBasicOperations(leaderboard);
        
        // Demo performance comparison
        demoPerformanceComparison();
        
        // Demo search functionality
        demoSearchFunctionality(leaderboard);
    }
    
    private static void demoBasicOperations(Leaderboard leaderboard) {
        System.out.println("1. BASIC OPERATIONS DEMO:");
        
        // Add players
        leaderboard.addPlayer(new Player("Alice", 1500));
        leaderboard.addPlayer(new Player("Bob", 1800));
        leaderboard.addPlayer(new Player("Charlie", 1200));
        leaderboard.addPlayer(new Player("Diana", 1800)); // Tie with Bob
        
        // Display leaderboard
        leaderboard.displayLeaderboard();
        
        // Update score
        leaderboard.updatePlayerScore("Alice", 1900);
        System.out.println("\nAfter updating Alice's score:");
        leaderboard.displayLeaderboard();
        
        // Get top players
        List<Player> top2 = leaderboard.getTopNPlayers(2);
        System.out.println("\nTop 2 players: " + top2);
    }
    
    private static void demoPerformanceComparison() {
        System.out.println("\n\n2. PERFORMANCE COMPARISON:");
        
        Sorter[] sorters = {new MergeSort(), new QuickSort()};
        SearchStrategy[] searchers = {new LinearSearch(), new BinarySearch()};
        
        for (Sorter sorter : sorters) {
            for (SearchStrategy searcher : searchers) {
                System.out.println("\n--- Testing " + sorter.getAlgorithmName() + 
                                 " with " + searcher.getSearchType() + " ---");
                
                Leaderboard lb = new GameLeaderboard(sorter, searcher);
                PerformanceMetrics pm = new PerformanceMetrics();
                
                // Add multiple players
                pm.startTimer();
                for (int i = 0; i < 1000; i++) {
                    lb.addPlayer(new Player("Player" + i, (int) (Math.random() * 10000)));
                }
                pm.stopTimer();
                System.out.printf("Add 1000 players: %.2f ms\n", 
                    PerformanceMetrics.convertToMilliseconds(pm.getElapsedTime()));
                
                // Get top players
                pm.startTimer();
                lb.getTopNPlayers(10);
                pm.stopTimer();
                System.out.printf("Get top 10: %.2f ms\n",
                    PerformanceMetrics.convertToMilliseconds(pm.getElapsedTime()));
            }
        }
    }
    
    private static void demoSearchFunctionality(Leaderboard leaderboard) {
        System.out.println("\n\n3. SEARCH FUNCTIONALITY DEMO:");
        
        // Find player by name
        Player player = leaderboard.findPlayerByName("Bob");
        System.out.println("Found Bob: " + player);
        
        // Find players in score range
        List<Player> inRange = leaderboard.findPlayersInScoreRange(1500, 1800);
        System.out.println("Players with scores 1500-1800: " + inRange);
        
        // Get player rank
        int rank = leaderboard.getPlayerRank("Diana");
        System.out.println("Diana's rank: " + rank);
    }
}