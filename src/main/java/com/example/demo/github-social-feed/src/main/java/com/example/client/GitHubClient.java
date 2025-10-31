package com.example.client;

import com.example.model.GitHubCommitResponse;
import com.example.model.SocialMediaPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GitHubClient implements SocialMediaClient {

    private static final Logger log = LoggerFactory.getLogger(GitHubClient.class);
    private final WebClient webClient;

    @Value("${github.owner:octocat}")
    private String owner;

    @Value("${github.repo:Hello-World}")
    private String repo;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getPlatformName() {
        return "github";
    }

    @Override
    public CompletableFuture<List<SocialMediaPost>> getRecentPosts(int limit) {
        String uri = String.format("/repos/%s/%s/commits?per_page=%d", owner, repo, limit);

        return webClient.get()
                .uri(uri)
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .bodyToFlux(GitHubCommitResponse.class)
                .take(limit)
                .map(this::mapToSocialMediaPost)
                .collectList()
                .doOnError(error -> log.error("Error fetching GitHub posts: {}", error.getMessage()))
                .onErrorReturn(List.of())
                .toFuture();
    }

    @Override
    public CompletableFuture<SocialMediaPost> getPost(String id) {
        String sha = id.replace("github:", "");
        String uri = String.format("/repos/%s/%s/commits/%s", owner, repo, sha);

        return webClient.get()
                .uri(uri)
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .bodyToMono(GitHubCommitResponse.class)
                .map(this::mapToSocialMediaPost)
                .doOnError(error -> log.error("Error fetching GitHub post {}: {}", id, error.getMessage()))
                .onErrorReturn(createFallbackPost(id))
                .toFuture();
    }

    @Override
    public CompletableFuture<Boolean> isHealthy() {
        String uri = String.format("/repos/%s/%s", owner, repo);

        return webClient.get()
                .uri(uri)
                .header("Accept", "application/vnd.github.v3+json")
                .exchangeToMono(response -> {
                    boolean healthy = response.statusCode().is2xxSuccessful();
                    log.debug("GitHub health check: {}", healthy ? "HEALTHY" : "UNHEALTHY");
                    return Mono.just(healthy);
                })
                .doOnError(error -> {
                    log.warn("GitHub health check failed: {}", error.getMessage());
                })
                .onErrorReturn(false)
                .toFuture();
    }

    public Flux<SocialMediaPost> getRecentPostsReactive(int limit) {
        String uri = String.format("/repos/%s/%s/commits?per_page=%d", owner, repo, limit);

        return webClient.get()
                .uri(uri)
                .header("Accept", "application/vnd.github.v3+json")
                .retrieve()
                .bodyToFlux(GitHubCommitResponse.class)
                .take(limit)
                .map(this::mapToSocialMediaPost)
                .onErrorResume(error -> {
                    log.error("Error in reactive GitHub stream: {}", error.getMessage());
                    return Flux.empty();
                });
    }

    private SocialMediaPost mapToSocialMediaPost(GitHubCommitResponse commit) {
        try {
            String id = "github:" + (commit.sha != null ? commit.sha.substring(0, 7) : "unknown");
            
            String author = "unknown";
            Instant createdAt = Instant.now();
            String message = "";
            int comments = 0;

            // Safely access nested properties
            if (commit.commit != null) {
                if (commit.commit.author != null) {
                    author = commit.commit.author.name != null ? commit.commit.author.name : "unknown";
                    if (commit.commit.author.date != null) {
                        try {
                            createdAt = Instant.parse(commit.commit.author.date);
                        } catch (Exception e) {
                            log.debug("Failed to parse date: {}", commit.commit.author.date);
                        }
                    }
                }
                message = commit.commit.message != null ? truncateMessage(commit.commit.message) : "";
                // Use 0 if commentCount is null
                comments = commit.commit.commentCount != null ? commit.commit.commentCount : 0;
            }

            // Fallback to author login if available
            if ("unknown".equals(author) && commit.author != null && commit.author.login != null) {
                author = commit.author.login;
            }

            String url = commit.htmlUrl != null ? commit.htmlUrl : 
                        String.format("https://github.com/%s/%s/commit/%s", owner, repo, commit.sha);

            return new SocialMediaPost(id, "github", author, message, createdAt, 0, comments, url);
            
        } catch (Exception e) {
            log.error("Error mapping GitHub commit to SocialMediaPost", e);
            return createFallbackPost("github:error");
        }
    }

    private String truncateMessage(String message) {
        if (message == null) return "";
        String firstLine = message.split("\n")[0];
        return firstLine.length() > 200 ? firstLine.substring(0, 200) + "..." : firstLine;
    }

    private SocialMediaPost createFallbackPost(String id) {
        return new SocialMediaPost(
            id, 
            "github", 
            "unknown", 
            "Failed to load post", 
            Instant.now(), 
            0, 0, 
            "https://github.com"
        );
    }
}