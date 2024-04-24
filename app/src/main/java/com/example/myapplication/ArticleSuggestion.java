package com.example.myapplication;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;


public class ArticleSuggestion {

    // Define a simple Article class with title and tags
    public static class Article {
        private String title;
        private List<String> tags;

        public Article(String title, List<String> tags) {
            this.title = title;
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getTags() {
            return tags;
        }

        // Check if the article has a tag related to politics
        public boolean hasPoliticsTag() {
            return tags.contains("politics");
        }

        // Check if the article has a tag related to sports
        public boolean hasSportsTag() {
            return tags.contains("sports");
        }
    }

    // Method to retrieve all articles (replace with your actual implementation)
    public static List<Article> getAllArticles() {
        List<Article> allArticles = new ArrayList<>();

        // Sample data: add articles with titles and tags
        allArticles.add(new Article("https://repubblica.it", Arrays.asList("politics")));
        allArticles.add(new Article("https://internazionale.it", Arrays.asList("sports")));
        allArticles.add(new Article("https://multiplayer.it", Arrays.asList("politics", "sports")));
        // Add more articles as needed...

        return allArticles;
    }


    // Method to suggest an article based on user preferences
    public static List<Article> suggestArticles(boolean politicsPref, boolean sportsPref) {
        List<Article> suggestedArticles = new ArrayList<>();

        // Sample list of articles (replace with your actual list)
        List<Article> allArticles = getAllArticles();

        for (Article article : allArticles) {
            if ((politicsPref && article.getTags().contains("politics")) ||
                    (sportsPref && article.getTags().contains("sports"))) {
                suggestedArticles.add(article);
            }
        }


        return suggestedArticles;
    }

}