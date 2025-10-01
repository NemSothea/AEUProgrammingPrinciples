package com.example.demo.leaderboard.core;

import com.example.demo.leaderboard.model.Player;
import java.util.List;

public interface Leaderboard {
    // Player Management
    void addPlayer(Player player);

    void updatePlayerScore(String playerName, int newScore);

    boolean removePlayer(String playerName);

    // Ranking
    List<Player> getTopNPlayers(int n);

    int getPlayerRank(String playerName);

    Player getPlayerByRank(int rank);

    // Search Functionality
    Player findPlayerByName(String name);

    List<Player> findPlayersInScoreRange(int minScore, int maxScore);

    // Utility
    int getTotalPlayers();

    void displayLeaderboard();

    double getMemoryUsage();

    void clear();
}
