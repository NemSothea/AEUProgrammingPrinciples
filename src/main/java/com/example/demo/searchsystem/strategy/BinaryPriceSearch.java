package com.example.demo.searchsystem.strategy;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;

import com.example.demo.searchsystem.model.Product;

public class BinaryPriceSearch implements SearchStrategy {
    @Override
    public List<Product> search(List<Product> products, String query) {
        try {
            double targetPrice = Double.parseDouble(query);
            List<Product> sortedProducts = new ArrayList<>(products);
            Collections.sort(sortedProducts);
            
            List<Product> results = new ArrayList<>();
            binarySearchByPrice(sortedProducts, targetPrice, 0, sortedProducts.size() - 1, results);
            return results;
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }
    
    private void binarySearchByPrice(List<Product> products, double targetPrice, 
                                   int left, int right, List<Product> results) {
        if (left > right) return;
        
        int mid = left + (right - left) / 2;
        Product midProduct = products.get(mid);
        
        if (Math.abs(midProduct.getPrice() - targetPrice) < 0.01) {
            // Found exact match, collect all products with same price
            results.add(midProduct);
            
            // Check left side for same price
            int leftIndex = mid - 1;
            while (leftIndex >= 0 && Math.abs(products.get(leftIndex).getPrice() - targetPrice) < 0.01) {
                results.add(products.get(leftIndex));
                leftIndex--;
            }
            
            // Check right side for same price
            int rightIndex = mid + 1;
            while (rightIndex < products.size() && Math.abs(products.get(rightIndex).getPrice() - targetPrice) < 0.01) {
                results.add(products.get(rightIndex));
                rightIndex++;
            }
        } else if (midProduct.getPrice() < targetPrice) {
            binarySearchByPrice(products, targetPrice, mid + 1, right, results);
        } else {
            binarySearchByPrice(products, targetPrice, left, mid - 1, results);
        }
    }
    
    @Override
    public String getStrategyName() {
        return "Binary Search by Price";
    }
}
