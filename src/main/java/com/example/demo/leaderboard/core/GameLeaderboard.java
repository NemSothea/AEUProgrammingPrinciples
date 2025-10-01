package com.example.demo.leaderboard.core;
import com.example.demo.leaderboard.model.*;
import com.example.demo.leaderboard.algorithms.sorting.Sorter;
import com.example.demo.leaderboard.algorithms.search.SearchStrategy;
import com.example.demo.leaderboard.exceptions.PlayerNotFoundException;
import com.example.demo.leaderboard.exceptions.DuplicatePlayerException;
import com.example.demo.leaderboard.exceptions.InvalidRankException;

import java.util.*;
public class GameLeaderboard implements Leaderboard {
    private final List<Player> players;
    private final Map<String, Player> playerMap;
    private boolean needsSorting;
    private final Sorter sorter;
    private final SearchStrategy searchStrategy;
    
    public GameLeaderboard(Sorter sorter, SearchStrategy searchStrategy) {
        this.players = new ArrayList<>();
        this.playerMap = new HashMap<>();
        this.needsSorting = false;
        this.sorter = sorter;
        this.searchStrategy = searchStrategy;
    }
    
    @Override
    public void addPlayer(Player player) {
        String playerKey = player.getName().toLowerCase();
        if (playerMap.containsKey(playerKey)) {
            throw new DuplicatePlayerException("Player already exists: " + player.getName());
        }
        
        players.add(player);
        playerMap.put(playerKey, player);
        needsSorting = true;
    }
    
    @Override
    public void updatePlayerScore(String playerName, int newScore) {
        Player player = playerMap.get(playerName.toLowerCase());
        if (player == null) {
            throw new PlayerNotFoundException("Player not found: " + playerName);
        }
        
        player.setScore(newScore);
        needsSorting = true;
    }
    
    @Override
    public boolean removePlayer(String playerName) {
        Player player = playerMap.remove(playerName.toLowerCase());
        if (player != null) {
            return players.remove(player);
        }
        return false;
    }
    
    @Override
    public List<Player> getTopNPlayers(int n) {
        ensureSorted();
        int endIndex = Math.min(n, players.size());
        return new ArrayList<>(players.subList(0, endIndex));
    }
    
    @Override
    public int getPlayerRank(String playerName) {
        ensureSorted();
        Player player = playerMap.get(playerName.toLowerCase());
        if (player == null) {
            throw new PlayerNotFoundException("Player not found: " + playerName);
        }
        return players.indexOf(player) + 1;
    }
    
    @Override
    public Player getPlayerByRank(int rank) {
        ensureSorted();
        if (rank < 1 || rank > players.size()) {
            throw new InvalidRankException("Invalid rank: " + rank);
        }
        return players.get(rank - 1);
    }
    
    @Override
    public Player findPlayerByName(String name) {
        return searchStrategy.findByName(players, name);
    }
    
    @Override
    public List<Player> findPlayersInScoreRange(int minScore, int maxScore) {
        ensureSorted();
        return searchStrategy.findByScoreRange(players, minScore, maxScore);
    }
    
    @Override
    public int getTotalPlayers() {
        return players.size();
    }
    
    @Override
    public void displayLeaderboard() {
        ensureSorted();
        
        System.out.println("\n=== LEADERBOARD ===");
        System.out.printf("%-4s %-15s %-8s%n", "Rank", "Player", "Score");
        System.out.println("---- --------------- --------");
        
        for (int i = 0; i < Math.min(players.size(), 10); i++) {
            Player player = players.get(i);
            System.out.printf("%-4d %-15s %-8d%n", i + 1, player.getName(), player.getScore());
        }
        
        if (players.size() > 10) {
            System.out.println("... and " + (players.size() - 10) + " more players");
        }
    }
    
    @Override
    public double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        return memory / (1024.0 * 1024.0);
    }
    
    @Override
    public void clear() {
        players.clear();
        playerMap.clear();
        needsSorting = false;
    }
    
    private void ensureSorted() {
        if (needsSorting) {
            sorter.sort(players);
            needsSorting = false;
        }
    }
    
    // Additional utility methods
    public void setNeedsSorting(boolean needsSorting) {
        this.needsSorting = needsSorting;
    }
    
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }
}
