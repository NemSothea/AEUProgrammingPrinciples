package com.example.demo.leaderboard.model;


public class Player implements Comparable<Player> {
    private final String name;
    private int score;
    private final long joinTime;
    
    public Player(String name, int score) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        
        this.name = name.trim();
        this.score = score;
        this.joinTime = System.currentTimeMillis();
    }
    
    // Getters
    public String getName() { return name; }
    public int getScore() { return score; }
    public long getJoinTime() { return joinTime; }
    
    // Setter with validation
    public void setScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.score = score;
    }
    
    @Override
    public int compareTo(Player other) {
        // Primary: Higher score first (descending)
        if (this.score != other.score) {
            return Integer.compare(other.score, this.score);
        }
        // Secondary: Alphabetical name for tie-breaking
        int nameCompare = this.name.compareToIgnoreCase(other.name);
        if (nameCompare != 0) {
            return nameCompare;
        }
        // Tertiary: Join time for complete tie-breaking
        return Long.compare(this.joinTime, other.joinTime);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return name.equalsIgnoreCase(player.name);
    }
    
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Player{name='%s', score=%d, joinTime=%d}", name, score, joinTime);
    }
}