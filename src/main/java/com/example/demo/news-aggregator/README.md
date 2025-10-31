Here are the comprehensive questions/text for the News Aggregator Service project:

## **Lab Activity - News Aggregator Service**

### **Project Title: Build a Secure News Aggregation Service**

---

## **Problem Statement**

In today's digital age, people consume news from multiple sources, but it's challenging to get a comprehensive view across different platforms. You are tasked with building a **secure news aggregation service** that fetches, processes, and displays news articles from multiple news APIs in a unified interface.

---

## **Core Requirements**

### **1. Multiple News Sources Integration**
- Integrate with **2-3 different news APIs** (e.g., NewsAPI, The Guardian, New York Times)
- Support for fetching **top headlines** by category
- Support for **searching articles** by keywords
- Normalize data from different API formats into a consistent structure

### **2. Security & Authentication**
- Implement **secure API key management**
- Protect sensitive configuration data
- Validate and sanitize all input parameters
- Use secure HTTP practices (headers, timeouts)

### **3. Performance & Reliability**
- Implement **rate limiting** to respect API quotas
- Add **caching mechanism** to reduce API calls
- Handle API failures gracefully without service disruption
- Implement proper **error handling** and logging

### **4. Data Processing**
- Parse JSON responses from different APIs
- Normalize article data into common format
- Remove duplicate articles across sources
- Sort articles by publication date

---

## **Technical Specifications**

### **Architecture Requirements:**
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   News Sources  │───▶│  News Aggregator │───▶│   Web Interface │
│  - NewsAPI      │    │  - Cache         │    │  - Thymeleaf    │
│  - The Guardian │    │  - Rate Limiting │    │  - REST API     │
│  - NY Times     │    │  - Error Handling│    │  - Search       │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### **Core Classes to Implement:**

**1. Data Model:**
```java
public class Article {
    private String id;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private LocalDateTime publishedAt;
    private String sourceName;
    private String author;
    private String category;
}
```

**2. News Source Interface:**
```java
public interface NewsSource {
    String getSourceName();
    List<Article> getTopHeadlines(String category) throws NewsSourceException;
    List<Article> searchArticles(String query) throws NewsSourceException;
    boolean isHealthy();
}
```

**3. Main Aggregator:**
```java
public class NewsAggregator {
    private List<NewsSource> sources;
    private ArticleCache cache;
    
    public List<Article> getAggregatedNews(String category) {
        // Aggregate from multiple sources
    }
}
```

---

## **Implementation Tasks**

### **Phase 1: Core Infrastructure**
- [ ] Set up Spring Boot project with Maven
- [ ] Create data models and interfaces
- [ ] Implement API key management system
- [ ] Create HTTP client service with rate limiting

### **Phase 2: News Source Integration**
- [ ] Implement NewsAPI integration
- [ ] Add at least one additional news source
- [ ] Create mock news source for testing
- [ ] Implement data normalization

### **Phase 3: Caching & Performance**
- [ ] Implement article caching mechanism
- [ ] Add rate limiting between API calls
- [ ] Handle duplicate articles
- [ ] Implement proper error handling

### **Phase 4: Web Interface**
- [ ] Create Spring MVC controller
- [ ] Build Thymeleaf templates
- [ ] Add category filtering
- [ ] Implement search functionality

### **Phase 5: Security & Reliability**
- [ ] Secure API key storage
- [ ] Input validation and sanitization
- [ ] Health checks for news sources
- [ ] Comprehensive error handling

---

## **Quality Attributes**

| Aspect | Requirements |
|--------|--------------|
| **Security** | Secure API key storage, input validation, secure HTTP headers |
| **Modularity** | Clean separation with interfaces, easy to add new news sources |
| **Reliability** | Graceful degradation, error handling, fallback mechanisms |
| **Performance** | Caching, rate limiting, efficient data processing |
| **Maintainability** | Clean code, proper documentation, testable components |

---

## **Testing Requirements**

- Unit tests for core components
- Integration tests for news source connections
- Mock testing for API failures
- Performance testing for caching

---

## **Deliverables**

1. **Complete Spring Boot Application** with all features
2. **Source code** with proper documentation
3. **Configuration files** (application.properties)
4. **API documentation** for extending the service
5. **Test cases** for critical functionality

---

## **Evaluation Criteria**

| Criteria | Weight | Description |
|----------|---------|-------------|
| **Functionality** | 40% | All requirements implemented and working |
| **Code Quality** | 25% | Clean, modular, well-documented code |
| **Security** | 15% | Proper API key management and input validation |
| **Error Handling** | 10% | Graceful handling of failures |
| **Performance** | 10% | Efficient caching and rate limiting |

---

## **Bonus Features** (Optional)

- **REST API** endpoints for mobile clients
- **User preferences** for favorite categories/sources
- **Advanced filtering** (date range, source selection)
- **Real-time updates** using WebSockets
- **Docker containerization**

---

## **Getting Started Instructions**

1. **Initialize Project**: Use Spring Initializr with:
   - Spring Boot 3.2+
   - Java 21
   - Dependencies: Web, Thymeleaf, DevTools

2. **Get API Keys**: Register for free accounts at:
   - NewsAPI: https://newsapi.org

3. **Implementation Order**: Follow the phase-based approach above

---

**Note**: This project demonstrates real-world skills in API integration, security, performance optimization, and full-stack development with Spring Boot.