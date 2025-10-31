package com.example.client;



import com.example.model.SocialMediaPost;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SocialMediaClient {
    String getPlatformName();
    CompletableFuture<List<SocialMediaPost>> getRecentPosts(int limit);
    CompletableFuture<SocialMediaPost> getPost(String id);
    CompletableFuture<Boolean> isHealthy();
}
