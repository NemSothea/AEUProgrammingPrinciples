package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubCommitResponse {
    public String sha;
    
    @JsonProperty("html_url")
    public String htmlUrl;
    
    public Commit commit;
    public User author;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit {
        public CommitAuthor author;
        public String message;
        
        @JsonProperty("comment_count")
        public Integer commentCount;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommitAuthor {
        public String name;
        public String date;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        public String login;
        public String name;
    }
}