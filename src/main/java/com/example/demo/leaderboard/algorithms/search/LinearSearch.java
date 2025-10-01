package com.example.demo.leaderboard.algorithms.search;

import com.example.demo.leaderboard.model.Player;
import java.util.*;

public class LinearSearch implements SearchStrategy {

    @Override
    public Player findByName(List<Player> players, String name) {
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
        for (Player player : players) {
            if (player.getScore() >= minScore && player.getScore() <= maxScore) {
                result.add(player);
            }
        }
        return result;
    }

    @Override
    public String getSearchType() {
        return "Linear Search";
    }
}
