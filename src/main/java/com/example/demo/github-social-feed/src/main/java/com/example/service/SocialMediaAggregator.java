package com.example.service;

import com.example.client.SocialMediaClient;
import com.example.model.SocialMediaPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SocialMediaAggregator {

    private static final Logger log = LoggerFactory.getLogger(SocialMediaAggregator.class);
    
    private final List<SocialMediaClient> clients;

    public SocialMediaAggregator(List<SocialMediaClient> clients) {
        this.clients = clients;
        log.info("SocialMediaAggregator initialized with {} clients", clients.size());
        
        // Log client names for debugging
        clients.forEach(client -> 
            log.info("Registered client: {}", client.getPlatformName()));
    }

    public CompletableFuture<List<SocialMediaPost>> getAggregatedFeed(int postsPerSource) {
        log.info("Fetching aggregated feed with {} posts per source", postsPerSource);

        try {
            List<CompletableFuture<List<SocialMediaPost>>> futures = clients.stream()
                    .map(client -> {
                        log.debug("Fetching posts from {}", client.getPlatformName());
                        return client.getRecentPosts(postsPerSource)
                                .exceptionally(ex -> {
                                    log.warn("Failed to fetch from {}: {}", client.getPlatformName(), ex.getMessage());
                                    return List.of();
                                });
                    })
                    .collect(Collectors.toList());

            // Combine all futures
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> {
                        List<SocialMediaPost> allPosts = futures.stream()
                                .map(CompletableFuture::join)
                                .flatMap(List::stream)
                                .collect(Collectors.toList());

                        log.info("Successfully aggregated {} posts from {} clients", 
                                allPosts.size(), clients.size());
                        
                        // Sort by creation date (newest first)
                        return allPosts.stream()
                                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                                .collect(Collectors.toList());
                    });
        } catch (Exception e) {
            log.error("Error in getAggregatedFeed: {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(List.of());
        }
    }

    public CompletableFuture<Boolean> isAnyClientHealthy() {
        List<CompletableFuture<Boolean>> healthChecks = clients.stream()
                .map(client -> client.isHealthy()
                        .exceptionally(ex -> {
                            log.debug("Health check failed for {}: {}", client.getPlatformName(), ex.getMessage());
                            return false;
                        }))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(healthChecks.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    boolean anyHealthy = healthChecks.stream()
                            .anyMatch(future -> future.join());
                    log.info("Overall health status: {}", anyHealthy ? "HEALTHY" : "UNHEALTHY");
                    return anyHealthy;
                });
    }

    // Add the missing method
    public CompletableFuture<SocialMediaPost> getPostFromAnySource(String postId) {
        log.info("Fetching post with ID: {}", postId);
        
        // Try to get post from any available client
        List<CompletableFuture<SocialMediaPost>> futures = clients.stream()
                .map(client -> client.getPost(postId)
                        .exceptionally(ex -> {
                            log.debug("Failed to get post {} from {}: {}", 
                                    postId, client.getPlatformName(), ex.getMessage());
                            return null;
                        }))
                .collect(Collectors.toList());

        return CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(result -> {
                    SocialMediaPost post = (SocialMediaPost) result;
                    if (post != null) {
                        log.info("Successfully found post {} from platform {}", postId, post.getPlatform());
                    } else {
                        log.warn("Post {} not found in any source", postId);
                        post = createFallbackPost(postId);
                    }
                    return post;
                })
                .exceptionally(ex -> {
                    log.error("Failed to get post {} from any source: {}", postId, ex.getMessage());
                    return createFallbackPost(postId);
                });
    }

    private SocialMediaPost createFallbackPost(String id) {
        return new SocialMediaPost(
            id, 
            "unknown", 
            "unknown", 
            "Post not available", 
            Instant.now(), 
            0, 0, 
            ""
        );
    }
}