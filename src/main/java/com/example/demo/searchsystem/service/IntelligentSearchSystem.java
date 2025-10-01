package com.example.demo.searchsystem.service;

import com.example.demo.searchsystem.model.*;

import com.example.demo.searchsystem.strategy.*;
import com.example.demo.searchsystem.enums.SortOption;
import com.example.demo.searchsystem.factory.*;

import java.util.ArrayList;
import java.util.List;

public class IntelligentSearchSystem {

    private final List<Product> products;
    private final ProductSorter sorter;
    private final SearchStrategyFactory strategyFactory;

    public IntelligentSearchSystem(List<Product> products) {
        this.products = new ArrayList<>(products);
        this.sorter = new ProductSorter();
        this.strategyFactory = new SearchStrategyFactory();
    }

    // Generic search method that applies DRY principle
    public SearchResult search(String strategyKey, String query, SortOption sortOption) {
        SearchStrategy strategy = strategyFactory.getStrategy(strategyKey);

        PerformanceMeasurer.TimeResult searchTime = new PerformanceMeasurer.TimeResult();
        List<Product> searchResults = PerformanceMeasurer.measureExecutionTime(
                () -> strategy.search(products, query), searchTime);

        PerformanceMeasurer.TimeResult sortTime = new PerformanceMeasurer.TimeResult();
        List<Product> sortedResults = PerformanceMeasurer.measureExecutionTime(
                () -> sorter.sort(searchResults, sortOption), sortTime);

        return new SearchResult(sortedResults, strategy.getStrategyName(),
                searchTime.getTime(), sortTime.getTime(), searchResults.size());
    }

    // Performance comparison across all strategies
    public void compareSearchPerformance(String query) {
        System.out.println("\n=== PERFORMANCE COMPARISON ===");
        System.out.println("Query: " + query);
        System.out.println("Total products: " + products.size());
        System.out.println("\nStrategy\t\t\tResults\tSearch Time(millisecond)\tTotal Time(millisecond)");
        System.out.println("-".repeat(70));

        for (String strategyKey : strategyFactory.getAvailableStrategies()) {
            SearchResult result = search(strategyKey, query, SortOption.NAME_ASC);
            System.out.printf("%-25s\t%d\t%d\t\t%d\n",
                    result.getStrategyName(),
                    result.getResultCount(),
                    result.getSearchTime(),
                    result.getTotalTime());
        }
    }

    // Add product with validation
    public void addProduct(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
        }
    }

    // Get all products (defensive copy)
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public int getProductCount() {
        return products.size();
    }

}
