package com.example.news_aggregator.cache;

import com.example.news_aggregator.model.Article;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ArticleCache {
    private final Map<String, CacheEntry> cache;
    private final ScheduledExecutorService cleanupScheduler;

    public ArticleCache() {
        this.cache = new ConcurrentHashMap<>();
        this.cleanupScheduler = Executors.newScheduledThreadPool(1);
        this.cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, 1, 1, TimeUnit.MINUTES);
    }

    public List<Article> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getArticles();
        }
        return null;
    }

    public void put(String key, List<Article> articles) {
        if (key != null && articles != null) {
            cache.put(key, new CacheEntry(articles, 10)); // 10 minutes expiration
        }
    }

    private void cleanupExpiredEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static class CacheEntry {
        private final List<Article> articles;
        private final long createdAt;
        private final long expireAfterMs;

        public CacheEntry(List<Article> articles, long expireMinutes) {
            this.articles = articles;
            this.createdAt = System.currentTimeMillis();
            this.expireAfterMs = expireMinutes * 60 * 1000;
        }

        public List<Article> getArticles() {
            return articles;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - createdAt > expireAfterMs;
        }
    }
}
