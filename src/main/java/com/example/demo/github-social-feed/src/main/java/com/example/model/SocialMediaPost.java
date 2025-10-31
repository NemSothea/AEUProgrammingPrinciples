package com.example.model;

import java.time.Instant;

public class SocialMediaPost {
    private String id;
    private String platform;
    private String author;
    private String content;
    private Instant createdAt;
    private int likeCount;
    private int commentCount;
    private String url;

    // Default constructor
    public SocialMediaPost() {}

    // Full constructor
    public SocialMediaPost(String id, String platform, String author, String content,
                          Instant createdAt, int likeCount, int commentCount, String url) {
        this.id = id;
        this.platform = platform;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.url = url;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public String toString() {
        return "SocialMediaPost{" +
                "id='" + id + '\'' +
                ", platform='" + platform + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", url='" + url + '\'' +
                '}';
    }
}