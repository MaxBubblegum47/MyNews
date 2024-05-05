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
        allArticles.add(new Article("https://www.nytimes.com/", Arrays.asList("politics")));
        allArticles.add(new Article("https://charliehebdo.fr/", Arrays.asList("politics")));
        allArticles.add(new Article("https://www.aljazeera.com/", Arrays.asList("politics")));

        allArticles.add(new Article("https://www.hwupgrade.it/", Arrays.asList("tech")));
        allArticles.add(new Article("https://www.notebookcheck.net/", Arrays.asList("tech")));
        allArticles.add(new Article("https://www.tomshardware.com/", Arrays.asList("tech")));
        allArticles.add(new Article("https://gamersnexus.net/", Arrays.asList("tech")));

        allArticles.add(new Article("https://www.automoto.it/", Arrays.asList("automoto")));
        allArticles.add(new Article("https://www.auto.it/", Arrays.asList("automoto")));
        allArticles.add(new Article("https://www.alvolante.it/", Arrays.asList("automoto")));
        allArticles.add(new Article("https://www.inmoto.it/", Arrays.asList("automoto")));
        allArticles.add(new Article("https://www.insella.it/", Arrays.asList("automoto")));

        allArticles.add(new Article("https://rollingstones.com/", Arrays.asList("music")));
        allArticles.add(new Article("https://www.giornaledellamusica.it/", Arrays.asList("music")));
        allArticles.add(new Article("https://www.essemagazine.it/", Arrays.asList("music")));
        allArticles.add(new Article("https://pitchfork.com/news/", Arrays.asList("music")));

        return allArticles;
    }


    // Method to suggest an article based on user preferences
    public static List<Article> suggestArticles(boolean politicsPref, boolean techPref, boolean autoPref, boolean musicPref) {
        List<Article> suggestedArticles = new ArrayList<>();

        // Sample list of articles (replace with your actual list)
        List<Article> allArticles = getAllArticles();

        for (Article article : allArticles) {
            if ((politicsPref && article.getTags().contains("politics"))) {
                suggestedArticles.add(article);
            }

            if ((techPref && article.getTags().contains("tech"))) {
                suggestedArticles.add(article);
            }

            if ((musicPref && article.getTags().contains("music"))) {
                suggestedArticles.add(article);
            }

            if ((autoPref && article.getTags().contains("automoto"))) {
                suggestedArticles.add(article);
            }
        }

        return suggestedArticles;
    }

}