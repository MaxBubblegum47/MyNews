package com.example.myapplication;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.example.myapplication.ArticleDatabaseHelper;
import android.webkit.WebView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import android.database.Cursor;
import android.util.Log;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private WebsiteContentCheckerThread contentCheckerThread;

    private ArticleDatabaseHelper databaseHelper;

    // I need to copy this across all possible newspapers
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // this is the post icon, if I want to add some particular function
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView webView = findViewById(R.id.webViewBBC);

                String articleUrl = webView.getUrl();

                // Check if the URL is not null or empty
                if (articleUrl != null && !articleUrl.isEmpty()) {
                    // Extract the article title from the URL (you can implement your own logic)
                    String articleTitle = articleUrl.toString();

                    // Add the article to favorites
                    databaseHelper.addArticle(articleTitle, articleUrl);
                    Toast.makeText(MainActivity.this, "Article added to favorites" + articleUrl, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Unable to add article to favorites. No URL found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize and start the content checker thread
        String websiteUrl = "https://www.repubblica.it"; // Replace with your website URL for main page
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrl);
        contentCheckerThread.start();

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.foxNewsFragment, R.id.skyNewsFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        databaseHelper = new ArticleDatabaseHelper(this);


        // Example: Display favorite articles
        displayFavoriteArticles();
    }

    private void displayFavoriteArticles() {
        // Example: Retrieve and display favorite articles
        Cursor cursor = databaseHelper.getAllArticles();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ArticleDatabaseHelper.COLUMN_TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(ArticleDatabaseHelper.COLUMN_URL));
                Log.d("MainActivity", "Title: " + title + ", URL: " + url);
                // Display the favorite article in your UI (e.g., in a ListView or RecyclerView)
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        // Stop the content checker thread when the activity is destroyed
        if (contentCheckerThread != null) {
            contentCheckerThread.interrupt();
        }
        super.onDestroy();
    }
}
