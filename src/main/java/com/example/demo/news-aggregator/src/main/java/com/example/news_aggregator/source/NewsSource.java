package com.example.news_aggregator.source;

import com.example.news_aggregator.model.Article;
import java.util.List;

public interface NewsSource {
    String getSourceName();
    List<Article> getTopHeadlines(String category) throws NewsSourceException;
    List<Article> searchArticles(String query) throws NewsSourceException;
    boolean isHealthy();
}
