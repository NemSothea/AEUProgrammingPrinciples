package com.example.demo.searchsystem.strategy;

import com.example.demo.searchsystem.model.Product;
import java.util.List;

public interface SearchStrategy {
    List<Product> search(List<Product> products, String query);
    String getStrategyName();
}