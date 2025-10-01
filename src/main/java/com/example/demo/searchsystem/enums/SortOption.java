package com.example.demo.searchsystem.enums;

public enum SortOption {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    PRICE_ASC("Price (Low to High)"),
    PRICE_DESC("Price (High to Low)"),
    RATING_ASC("Rating (Low to High)"),
    RATING_DESC("Rating (High to Low)");
    
    private final String displayName;
    
    SortOption(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
