package com.example.demo.leaderboard.utils;

public class PerformanceMetrics {
    private long startTime;
    private long endTime;
    
    public void startTimer() {
        startTime = System.nanoTime();
    }
    
    public void stopTimer() {
        endTime = System.nanoTime();
    }
    
    public long getElapsedTime() {
        return endTime - startTime;
    }
    
    public static double convertToMilliseconds(long nanoseconds) {
        return nanoseconds / 1_000_000.0;
    }
    
    public static double convertToSeconds(long nanoseconds) {
        return nanoseconds / 1_000_000_000.0;
    }
}
