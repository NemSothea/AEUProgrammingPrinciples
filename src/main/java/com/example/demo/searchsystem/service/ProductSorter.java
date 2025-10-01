package com.example.demo.searchsystem.service;

import com.example.demo.searchsystem.enums.SortOption;
import com.example.demo.searchsystem.model.Product;

import java.util.*;
import java.util.List;


class ProductSorter {
    public List<Product> sort(List<Product> products, SortOption sortOption) {
        List<Product> sortedProducts = new ArrayList<>(products);
        
        switch (sortOption) {
            case NAME_ASC:
                sortedProducts.sort(Comparator.comparing(Product::getName));
                break;
            case NAME_DESC:
                sortedProducts.sort(Comparator.comparing(Product::getName).reversed());
                break;
            case PRICE_ASC:
                sortedProducts.sort(Comparator.comparing(Product::getPrice));
                break;
            case PRICE_DESC:
                sortedProducts.sort(Comparator.comparing(Product::getPrice).reversed());
                break;
            case RATING_ASC:
                sortedProducts.sort(Comparator.comparing(Product::getRating));
                break;
            case RATING_DESC:
                sortedProducts.sort(Comparator.comparing(Product::getRating).reversed());
                break;
        }
        
        return sortedProducts;
    }
}
