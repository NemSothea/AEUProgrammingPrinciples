package com.example.news_aggregator.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Article {
    private String id;
    private String title;
    private String description;
    private String content;
    private String url;
    private String imageUrl;
    private LocalDateTime publishedAt;
    private String sourceName;
    private String author;
    private String category;
    
    // Constructors, getters, setters, equals, hashCode, toString
    public Article() {}
    
    public Article(String id, String title, String description, String url, 
                   String sourceName, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.sourceName = sourceName;
        this.publishedAt = publishedAt;
    }
    
    // Add all getters and setters here...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id) && Objects.equals(url, article.url);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
    
    @Override
    public String toString() {
        return "Article{title='" + title + "', source='" + sourceName + "'}";
    }
}