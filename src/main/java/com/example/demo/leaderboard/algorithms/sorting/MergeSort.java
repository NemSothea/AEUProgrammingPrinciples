package com.example.demo.leaderboard.algorithms.sorting;
import com.example.demo.leaderboard.model.Player;
import java.util.*;

public class MergeSort implements Sorter {
    
    @Override
    public void sort(List<Player> players) {
        if (players.size() <= 1) return;
        mergeSort(players, 0, players.size() - 1);
    }
    
    private void mergeSort(List<Player> players, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(players, left, mid);
            mergeSort(players, mid + 1, right);
            merge(players, left, mid, right);
        }
    }
    
    private void merge(List<Player> players, int left, int mid, int right) {
        List<Player> leftList = new ArrayList<>(players.subList(left, mid + 1));
        List<Player> rightList = new ArrayList<>(players.subList(mid + 1, right + 1));
        
        int i = 0, j = 0, k = left;
        
        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).compareTo(rightList.get(j)) <= 0) {
                players.set(k++, leftList.get(i++));
            } else {
                players.set(k++, rightList.get(j++));
            }
        }
        
        while (i < leftList.size()) {
            players.set(k++, leftList.get(i++));
        }
        
        while (j < rightList.size()) {
            players.set(k++, rightList.get(j++));
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Merge Sort";
    }
}