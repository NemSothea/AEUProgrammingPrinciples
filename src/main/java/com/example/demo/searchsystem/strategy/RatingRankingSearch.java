package com.example.demo.searchsystem.strategy;

import com.example.demo.searchsystem.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RatingRankingSearch implements SearchStrategy {
    private final double minRating;
    
    public RatingRankingSearch(double minRating) {
        this.minRating = minRating;
    }
    
    @Override
    public List<Product> search(List<Product> products, String query) {
        try {
            double targetRating = Double.parseDouble(query);
            return products.stream()
                    .filter(product -> product.getRating() >= targetRating)
                    .sorted((p1, p2) -> Double.compare(p2.getRating(), p1.getRating())) // Descending by rating
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Rating-based Ranking Search (min: " + minRating + ")";
    }
}
