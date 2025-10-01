package com.example.demo.searchsystem.strategy;

import java.util.*;

import java.util.stream.Collectors;

import com.example.demo.searchsystem.model.Product;

public class CategoryFilterSearch implements SearchStrategy {
    @Override
    public List<Product> search(List<Product> products, String query) {
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(query))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getStrategyName() {
        return "Category Filter Search";
    }
}
