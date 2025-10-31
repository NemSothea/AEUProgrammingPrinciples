package com.example.web;

import com.example.model.SocialMediaPost;
import com.example.service.SocialMediaAggregator;
import com.example.client.GitHubClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class SocialMediaController {

    private final GitHubClient gitHubClient;
    private final SocialMediaAggregator aggregator;

    public SocialMediaController(GitHubClient gitHubClient, SocialMediaAggregator aggregator) {
        this.gitHubClient = gitHubClient;
        this.aggregator = aggregator;
    }

    // GitHub-specific endpoints
    @GetMapping("/github/posts")
    public CompletableFuture<List<SocialMediaPost>> getGitHubPosts(
            @RequestParam(defaultValue = "10") int limit) {
        return gitHubClient.getRecentPosts(limit);
    }

    @GetMapping("/github/posts/{id}")
    public CompletableFuture<SocialMediaPost> getGitHubPost(@PathVariable String id) {
        return gitHubClient.getPost(id);
    }

    @GetMapping("/github/health")
    public CompletableFuture<Boolean> getGitHubHealth() {
        return gitHubClient.isHealthy();
    }

    // Aggregated feed endpoint (MAIN EXERCISE REQUIREMENT)
    @GetMapping("/feed")
    public CompletableFuture<List<SocialMediaPost>> getAggregatedFeed(
            @RequestParam(defaultValue = "5") int postsPerSource) {
        return aggregator.getAggregatedFeed(postsPerSource);
    }

    @GetMapping("/health")
    public CompletableFuture<Boolean> getOverallHealth() {
        return aggregator.isAnyClientHealthy();
    }

    // FIXED: Correct method name
    @GetMapping("/posts/{postId}")
    public CompletableFuture<SocialMediaPost> getPostFromAnySource(@PathVariable String postId) {
        return aggregator.getPostFromAnySource(postId);
    }

    // Reactive endpoints
    @GetMapping(value = "/github/stream", produces = "application/stream+json")
    public Flux<SocialMediaPost> getGitHubStream(@RequestParam(defaultValue = "10") int limit) {
        return gitHubClient.getRecentPostsReactive(limit);
    }
}