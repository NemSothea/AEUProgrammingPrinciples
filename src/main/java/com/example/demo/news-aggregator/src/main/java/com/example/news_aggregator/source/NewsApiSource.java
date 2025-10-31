package com.example.news_aggregator.source;


import com.example.news_aggregator.model.Article;
import com.example.news_aggregator.security.ApiKeyManager;
import com.example.news_aggregator.service.HttpClientService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewsApiSource implements NewsSource {
    private static final String BASE_URL = "https://newsapi.org/v2";
    private static final String SOURCE_NAME = "NewsAPI";
    
    private final HttpClientService httpClient;
    private final ApiKeyManager apiKeyManager;
    
    public NewsApiSource(HttpClientService httpClient, ApiKeyManager apiKeyManager) {
        this.httpClient = httpClient;
        this.apiKeyManager = apiKeyManager;
    }
    
    @Override
    public String getSourceName() {
        return SOURCE_NAME;
    }
    
    @Override
    public List<Article> getTopHeadlines(String category) throws NewsSourceException {
        try {
            String url = BASE_URL + "/top-headlines?country=us&pageSize=10" + 
                        (category != null && !category.isEmpty() ? "&category=" + category : "");
            
            // Add API key to URL
            url = apiKeyManager.getUrlWithApiKey("newsapi", url);
            
            System.out.println("Fetching from NewsAPI: " + url.replaceAll("apiKey=[^&]+", "apiKey=***"));
            
            JsonNode response = httpClient.executeGetRequest(url, null);
            return parseArticles(response);
            
        } catch (NewsSourceException e) {
            System.err.println("NewsAPI Error: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<Article> searchArticles(String query) throws NewsSourceException {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
        
        String url = BASE_URL + "/everything?q=" + query + "&sortBy=publishedAt&pageSize=10";
        url = apiKeyManager.getUrlWithApiKey("newsapi", url);
        
        JsonNode response = httpClient.executeGetRequest(url, null);
        return parseArticles(response);
    }
    
    @Override
    public boolean isHealthy() {
        try {
            // Simple health check - try to get 1 article
            String testUrl = BASE_URL + "/top-headlines?country=us&pageSize=1";
            testUrl = apiKeyManager.getUrlWithApiKey("newsapi", testUrl);
            JsonNode response = httpClient.executeGetRequest(testUrl, null);
            return response.has("articles") && response.get("articles").isArray();
        } catch (NewsSourceException e) {
            System.err.println("NewsAPI health check failed: " + e.getMessage());
            return false;
        }
    }
    
    private List<Article> parseArticles(JsonNode response) {
        List<Article> articles = new ArrayList<>();
        
        if (!response.has("articles") || !response.get("articles").isArray()) {
            System.err.println("Invalid response format from NewsAPI");
            return articles;
        }
        
        JsonNode articlesNode = response.get("articles");
        
        for (JsonNode articleNode : articlesNode) {
            Article article = parseArticle(articleNode);
            if (article != null) {
                articles.add(article);
            }
        }
        
        System.out.println("Parsed " + articles.size() + " articles from NewsAPI");
        return articles;
    }
    
    private Article parseArticle(JsonNode articleNode) {
        try {
            if (!articleNode.has("title") || articleNode.get("title").isNull()) {
                return null;
            }
            
            String title = articleNode.get("title").asText();
            if ("[Removed]".equals(title) || title == null || title.trim().isEmpty()) {
                return null;
            }
            
            Article article = new Article();
            article.setId(generateArticleId(articleNode));
            article.setTitle(title);
            article.setDescription(articleNode.has("description") && !articleNode.get("description").isNull() ? 
                                 articleNode.get("description").asText() : "");
            article.setUrl(articleNode.has("url") ? articleNode.get("url").asText() : "");
            article.setImageUrl(articleNode.has("urlToImage") && !articleNode.get("urlToImage").isNull() ? 
                              articleNode.get("urlToImage").asText() : null);
            article.setSourceName(articleNode.has("source") && articleNode.get("source").has("name") ? 
                                articleNode.get("source").get("name").asText() : SOURCE_NAME);
            article.setAuthor(articleNode.has("author") && !articleNode.get("author").isNull() ? 
                            articleNode.get("author").asText() : "Unknown");
            
            // Parse published date
            if (articleNode.has("publishedAt") && !articleNode.get("publishedAt").isNull()) {
                String publishedAt = articleNode.get("publishedAt").asText();
                try {
                    article.setPublishedAt(LocalDateTime.parse(
                        publishedAt.replace("Z", ""), 
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    ));
                } catch (DateTimeParseException e) {
                    article.setPublishedAt(LocalDateTime.now());
                }
            } else {
                article.setPublishedAt(LocalDateTime.now());
            }
            
            return article;
        } catch (Exception e) {
            System.err.println("Error parsing article: " + e.getMessage());
            return null;
        }
    }
    
    private String generateArticleId(JsonNode articleNode) {
        String url = articleNode.has("url") ? articleNode.get("url").asText() : "";
        String title = articleNode.has("title") ? articleNode.get("title").asText() : "";
        return String.valueOf((url + title).hashCode());
    }
}
