package com.example.demo.leaderboard.algorithms.sorting;

import com.example.demo.leaderboard.model.Player;
import java.util.*;

public class QuickSort implements Sorter {
    
    @Override
    public void sort(List<Player> players) {
        if (players == null || players.size() <= 1) return;
        quickSort(players, 0, players.size() - 1);
    }
    
    private void quickSort(List<Player> players, int low, int high) {
        if (low < high) {
            // Partition the array and get the pivot index
            int pivotIndex = partition(players, low, high);
            
            // Recursively sort elements before and after partition
            quickSort(players, low, pivotIndex - 1);
            quickSort(players, pivotIndex + 1, high);
        }
    }
    
    private int partition(List<Player> players, int low, int high) {
        // Choose the rightmost element as pivot
        Player pivot = players.get(high);
        
        // Index of smaller element (indicates right position of pivot)
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            // If current element is "less than or equal to" pivot (for descending score order)
            // Remember: We want higher scores first, so we reverse the comparison
            if (players.get(j).compareTo(pivot) <= 0) {
                i++;
                // Swap players at i and j
                Collections.swap(players, i, j);
            }
        }
        
        // Swap the pivot element with the element at i+1
        Collections.swap(players, i + 1, high);
        
        return i + 1;
    }
    
    // Alternative implementation with randomized pivot for better average performance
    public void sortWithRandomPivot(List<Player> players) {
        if (players == null || players.size() <= 1) return;
        quickSortWithRandomPivot(players, 0, players.size() - 1);
    }
    
    private void quickSortWithRandomPivot(List<Player> players, int low, int high) {
        if (low < high) {
            // Randomly select pivot and swap with last element
            int randomPivotIndex = low + (int) (Math.random() * (high - low));
            Collections.swap(players, randomPivotIndex, high);
            
            int pivotIndex = partition(players, low, high);
            quickSortWithRandomPivot(players, low, pivotIndex - 1);
            quickSortWithRandomPivot(players, pivotIndex + 1, high);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Quick Sort";
    }
}