package com.example.demo.searchsystem.factory;
import com.example.demo.searchsystem.strategy.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SearchStrategyFactory {
    private final Map<String, SearchStrategy> strategies;
    
    public SearchStrategyFactory() {
        strategies = new HashMap<>();
        initializeStrategies();
    }
    
    private void initializeStrategies() {
        strategies.put("linear", new LinearNameSearch());
        strategies.put("binary", new BinaryPriceSearch());
        strategies.put("category", new CategoryFilterSearch());
        strategies.put("rating", new RatingRankingSearch(3.0));
    }
    
    public SearchStrategy getStrategy(String strategyKey) {
        SearchStrategy strategy = strategies.get(strategyKey);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid search strategy: " + strategyKey);
        }
        return strategy;
    }
    
    public Set<String> getAvailableStrategies() {
        return strategies.keySet();
    }
    
    public void registerStrategy(String key, SearchStrategy strategy) {
        strategies.put(key, strategy);
    }
}
