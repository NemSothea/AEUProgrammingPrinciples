/**
Build an intelligent search system that demonstrates multiple 
algorithms:
• Requirements:
– Data Structure: Create a Product class with name, category, price, rating 
– Search Algorithms: Implement multiple search strategies: 
• Linear search by name
• Binary search by price (sorted)
• Category filtering
• Rating-based ranking 
– Sorting Options: Allow sorting by different criteria 
– Performance Comparison: Measure and compare algorithm performance
• Focus: Apply DRY, modularity, and performance optimization principles

## Key Features:
This intelligent search system demonstrates:

1. **Modular Design**:
   - Separate classes for different responsibilities (Product, SearchStrategy implementations, Sorter, etc.)
   - Interface-based design for search strategies

2. **Multiple Search Algorithms**:
   - **Linear Search**: Simple name-based search
   - **Binary Search**: Efficient price-based search on sorted data
   - **Category Filtering**: Stream-based category filtering
   - **Rating-based Ranking**: Filters by minimum rating and sorts by rating

3. **DRY Principles**:
   - Generic search method that handles all strategies
   - Reusable sorting and performance measurement utilities
   - Common interface for all search strategies

4. **Performance Optimization**:
   - Binary search for price queries
   - Defensive copying to maintain data integrity
   - Performance measurement and comparison
   - Efficient sorting with comparators

5. **Flexible Sorting**:
   - Multiple sort options (name, price, rating in ascending/descending order)
   - Separate sorting service for maintainability

6. **Extensibility**:
   - Easy to add new search strategies by implementing SearchStrategy
   - Simple to add new sorting options
   - Modular architecture allows independent development

 */


package com.example.demo.searchsystem;

import java.util.Arrays;
import java.util.List;

import com.example.demo.searchsystem.enums.SortOption;
import com.example.demo.searchsystem.model.Product;
import com.example.demo.searchsystem.model.SearchResult;
import com.example.demo.searchsystem.service.IntelligentSearchSystem;

public class ProductSearchDemo {
    public static void main(String[] args) {
        // Create sample products
        List<Product> sampleProducts = Arrays.asList(
            new Product("Laptop", "Electronics", 999.99, 4.5),
            new Product("Smartphone", "Electronics", 699.99, 4.3),
            new Product("Headphones", "Electronics", 199.99, 4.7),
            new Product("Book: Java Programming", "Books", 49.99, 4.8),
            new Product("Desk Chair", "Furniture", 299.99, 4.2),
            new Product("Coffee Maker", "Home Appliances", 89.99, 4.0),
            new Product("Running Shoes", "Sports", 129.99, 4.6),
            new Product("Wireless Mouse", "Electronics", 29.99, 4.4),
            new Product("Python Programming Book", "Books", 54.99, 4.9),
            new Product("Gaming Monitor", "Electronics", 399.99, 4.5)
        );
        
        // Initialize search system
        IntelligentSearchSystem searchSystem = new IntelligentSearchSystem(sampleProducts);
        
        // Demo different search strategies
        System.out.println("=== INTELLIGENT PRODUCT SEARCH SYSTEM ===\n");
        
        // 1. Linear Search by Name
        SearchResult result1 = searchSystem.search("linear", "Book", SortOption.NAME_ASC);
        result1.displayResults();
        
        // 2. Binary Search by Price
        SearchResult result2 = searchSystem.search("binary", "399.99", SortOption.PRICE_DESC);
        result2.displayResults();
        
        // 3. Category Filter
        SearchResult result3 = searchSystem.search("category", "Electronics", SortOption.RATING_DESC);
        result3.displayResults();
        
        // 4. Rating-based Search
        SearchResult result4 = searchSystem.search("rating", "4.5", SortOption.RATING_DESC);
        result4.displayResults();
        
        // Performance Comparison
        searchSystem.compareSearchPerformance("Electronics");
        searchSystem.compareSearchPerformance("4.5");
        
        // Demonstrate adding new product
        System.out.println("\n=== ADDING NEW PRODUCT ===");
        Product newProduct = new Product("Mechanical Keyboard", "Electronics", 149.99, 4.8);
        searchSystem.addProduct(newProduct);
        
        SearchResult newSearch = searchSystem.search("linear", "Keyboard", SortOption.NAME_ASC);
        newSearch.displayResults();
    }
}
