package com.example.news_aggregator.source;

public class NewsSourceException extends Exception {
    private final ErrorType errorType;
    
    public enum ErrorType {
        RATE_LIMIT_EXCEEDED,
        AUTHENTICATION_FAILED,
        NETWORK_ERROR,
        PARSE_ERROR,
        UNKNOWN_ERROR
    }
    
    public NewsSourceException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
    
    public NewsSourceException(String message, ErrorType errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
    
    public ErrorType getErrorType() {
        return errorType;
    }
}
