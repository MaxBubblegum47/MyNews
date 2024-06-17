package com.example.myapplication;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class ArticleSuggestion {
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

        /*

        * The following 2 methods were made for testing purposes.
        *  Check if the article has a tag related to politics
        public boolean hasPoliticsTag() {
            return tags.contains("politics");
        }

        public boolean hasSportsTag() {
            return tags.contains("sports");
        } */
    }

        /* List of all articles suggestions */
        public static List<Article> getAllArticles() {
            List<Article> allArticles = new ArrayList<>();

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


        /*

         * This method check the preferences that the user has set and displays
         * the best matching news paper that could be interesting for the user.
         */
        public static List<Article> suggestArticles(boolean politicsPref, boolean techPref, boolean autoPref, boolean musicPref) {
            List<Article> suggestedArticles = new ArrayList<>();
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

