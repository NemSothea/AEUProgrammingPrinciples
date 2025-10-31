package com.example.news_aggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.news_aggregator.source.NewsSourceException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class HttpClientService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AtomicLong lastRequestTime;
    private final long rateLimitMs;
    
    public HttpClientService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.lastRequestTime = new AtomicLong(0);
        this.rateLimitMs = 1000; // 1 second between requests
    }
    
    public JsonNode executeGetRequest(String url, String authHeader) throws NewsSourceException {
        respectRateLimit();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "NewsAggregator/1.0");
        headers.setAccept(java.util.Collections.singletonList(MediaType.APPLICATION_JSON));
        
        if (authHeader != null) {
            headers.set("Authorization", authHeader);
        }
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
            
            HttpStatusCode statusCode = response.getStatusCode();
            
            if (statusCode == HttpStatus.TOO_MANY_REQUESTS) {
                throw new NewsSourceException("Rate limit exceeded for URL: " + url, 
                    NewsSourceException.ErrorType.RATE_LIMIT_EXCEEDED);
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new NewsSourceException("Authentication failed for URL: " + url, 
                    NewsSourceException.ErrorType.AUTHENTICATION_FAILED);
            } else if (statusCode == HttpStatus.FORBIDDEN) {
                throw new NewsSourceException("Access forbidden for URL: " + url, 
                    NewsSourceException.ErrorType.AUTHENTICATION_FAILED);
            } else if (!statusCode.is2xxSuccessful()) {
                throw new NewsSourceException("HTTP error " + statusCode + " for URL: " + url, 
                    NewsSourceException.ErrorType.NETWORK_ERROR);
            }
            
            String responseBody = response.getBody();
            if (responseBody == null || responseBody.trim().isEmpty()) {
                throw new NewsSourceException("Empty response from URL: " + url,
                    NewsSourceException.ErrorType.NETWORK_ERROR);
            }
            
            return objectMapper.readTree(responseBody);
            
        } catch (Exception e) {
            if (e instanceof NewsSourceException) {
                throw (NewsSourceException) e;
            }
            throw new NewsSourceException("Request failed for URL: " + url + " - " + e.getMessage(), 
                NewsSourceException.ErrorType.NETWORK_ERROR, e);
        }
    }
    
    private void respectRateLimit() throws NewsSourceException {
        long now = System.currentTimeMillis();
        long lastTime = lastRequestTime.get();
        long timeSinceLastRequest = now - lastTime;
        
        if (timeSinceLastRequest < rateLimitMs) {
            try {
                TimeUnit.MILLISECONDS.sleep(rateLimitMs - timeSinceLastRequest);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NewsSourceException("Rate limit sleep interrupted", 
                    NewsSourceException.ErrorType.UNKNOWN_ERROR);
            }
        }
        lastRequestTime.set(System.currentTimeMillis());
    }
}