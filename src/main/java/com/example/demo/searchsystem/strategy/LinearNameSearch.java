package com.example.demo.searchsystem.strategy;

import com.example.demo.searchsystem.model.Product;

import java.util.ArrayList;
import java.util.List;

// Linear Search by Name
public class LinearNameSearch implements SearchStrategy {
    @Override
    public List<Product> search(List<Product> products, String query) {
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(product);
            }
        }
        return results;
    }

    @Override
    public String getStrategyName() {
        return "Linear Search by Name";
    }
}
