package com.example.news_aggregator.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApiKeyManager {
    private final Map<String, String> apiKeys = new ConcurrentHashMap<>();
    
    @Value("${newsapi.key:demo_key}")
    private String newsApiKey;
    
    @PostConstruct
    public void init() {
        // Load from environment as fallback
        String envKey = System.getenv("NEWS_API_KEY");
        if (envKey != null && !envKey.isEmpty()) {
            apiKeys.put("newsapi", envKey);
        } else if (newsApiKey != null && !newsApiKey.isEmpty()) {
            apiKeys.put("newsapi", newsApiKey);
        }
        
        System.out.println("=== API Keys Loaded ===");
        System.out.println("Available keys: " + apiKeys.keySet());
        System.out.println("NewsAPI key present: " + hasApiKey("newsapi"));
        System.out.println("NewsAPI key value: " + (hasApiKey("newsapi") ? "***" + getApiKey("newsapi").substring(Math.max(0, getApiKey("newsapi").length() - 4)) : "MISSING"));
    }
    
    public String getApiKey(String source) {
        return apiKeys.get(source);
    }
    
    public String getUrlWithApiKey(String source, String baseUrl) {
        String apiKey = getApiKey(source);
        if (apiKey == null || apiKey.equals("demo_key") || apiKey.equals("your_newsapi_key_here")) {
            System.out.println("WARNING: Using demo key or placeholder for " + source);
            return baseUrl + (baseUrl.contains("?") ? "&" : "?") + "apiKey=demo_key";
        }
        
        return baseUrl + (baseUrl.contains("?") ? "&" : "?") + "apiKey=" + apiKey;
    }
    
    public boolean hasApiKey(String source) {
        return apiKeys.containsKey(source) && 
               apiKeys.get(source) != null && 
               !apiKeys.get(source).isEmpty() &&
               !apiKeys.get(source).equals("your_newsapi_key_here");
    }
    
    public boolean isValidApiKey(String source) {
        if (!hasApiKey(source)) return false;
        String key = getApiKey(source);
        return !key.equals("demo_key") && !key.equals("your_newsapi_key_here");
    }
}
