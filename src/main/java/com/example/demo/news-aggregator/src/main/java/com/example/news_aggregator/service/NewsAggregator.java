package com.example.news_aggregator.service;


import com.example.news_aggregator.model.Article;
import com.example.news_aggregator.source.NewsSource;
import com.example.news_aggregator.source.NewsSourceException;
import com.example.news_aggregator.cache.ArticleCache;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsAggregator {
    private final List<NewsSource> newsSources;
    private final ArticleCache cache;
    
    public NewsAggregator(List<NewsSource> newsSources, ArticleCache cache) {
        this.newsSources = new ArrayList<>(newsSources);
        this.cache = cache;
    }
    
    public List<Article> getAggregatedNews(String category) {
        String cacheKey = "headlines_" + (category != null ? category : "all");
        
        List<Article> cachedArticles = cache.get(cacheKey);
        if (cachedArticles != null) {
            return cachedArticles;
        }
        
        List<Article> allArticles = new ArrayList<>();
        
        for (NewsSource source : newsSources) {
            try {
                List<Article> articles = source.getTopHeadlines(category);
                if (articles != null) {
                    allArticles.addAll(articles);
                }
            } catch (NewsSourceException e) {
                System.err.println("Error from " + source.getSourceName() + ": " + e.getMessage());
            }
        }
        
        List<Article> uniqueArticles = removeDuplicates(allArticles);
        uniqueArticles.sort((a1, a2) -> {
            if (a1.getPublishedAt() == null && a2.getPublishedAt() == null) return 0;
            if (a1.getPublishedAt() == null) return 1;
            if (a2.getPublishedAt() == null) return -1;
            return a2.getPublishedAt().compareTo(a1.getPublishedAt());
        });
        
        cache.put(cacheKey, uniqueArticles);
        return uniqueArticles;
    }
    
    private List<Article> removeDuplicates(List<Article> articles) {
        return articles.stream()
                .filter(Objects::nonNull)
                .filter(article -> article.getUrl() != null && !article.getUrl().isEmpty())
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(
                        Article::getUrl,
                        article -> article,
                        (existing, replacement) -> existing
                    ),
                    map -> new ArrayList<>(map.values())
                ));
    }
    
    public List<String> getHealthySources() {
        return newsSources.stream()
                .filter(NewsSource::isHealthy)
                .map(NewsSource::getSourceName)
                .collect(Collectors.toList());
    }
}
