package com.example.demo.searchsystem.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<Product> results;
    private String strategyName;
    private long searchTime;
    private long sortTime;
    private int resultCount;

    public SearchResult(List<Product> results, String strategyName,
            long searchTime, long sortTime, int resultCount) {
        this.results = new ArrayList<>(results);
        this.strategyName = strategyName;
        this.searchTime = searchTime;
        this.sortTime = sortTime;
        this.resultCount = resultCount;
    }

    // Getters
    public List<Product> getResults() {
        return results;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public long getSortTime() {
        return sortTime;
    }

    public long getTotalTime() {
        return searchTime + sortTime;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void displayResults() {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Strategy: " + strategyName);
        System.out.println("Found " + resultCount + " products");
        System.out.println("Search Time: " + searchTime + " millisecond, Sort Time: " + sortTime + " millisecond");
        System.out.println("\nProducts:");
        results.forEach(System.out::println);
    }
}