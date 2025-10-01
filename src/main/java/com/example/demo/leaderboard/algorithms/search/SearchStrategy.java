package com.example.demo.leaderboard.algorithms.search;

import com.example.demo.leaderboard.model.Player;
import java.util.List;

public interface SearchStrategy {
    Player findByName(List<Player> players, String name);
    List<Player> findByScoreRange(List<Player> players, int minScore, int maxScore);
    String getSearchType();
}
