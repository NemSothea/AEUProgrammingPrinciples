package com.example.demo.leaderboard.algorithms.sorting;

import com.example.demo.leaderboard.model.Player;

import java.util.List;

public interface Sorter {
    void sort(List<Player> players);
    String getAlgorithmName();
}
