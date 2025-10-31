package com.example.news_aggregator.controller;

import com.example.news_aggregator.model.Article;
import com.example.news_aggregator.service.NewsAggregator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class NewsController {
    
    private final NewsAggregator newsAggregator;
    
    public NewsController(NewsAggregator newsAggregator) {
        this.newsAggregator = newsAggregator;
    }
    
    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String category) {
        List<Article> articles = newsAggregator.getAggregatedNews(category);
        model.addAttribute("articles", articles);
        model.addAttribute("category", category);
        model.addAttribute("healthySources", newsAggregator.getHealthySources());
        return "index";
    }
}
