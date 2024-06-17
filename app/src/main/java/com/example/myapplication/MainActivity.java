package com.example.myapplication;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import android.webkit.WebView;
import android.view.View;
import android.os.Bundle;
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
    private WebView webViewNews;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private WebsiteContentCheckerThread contentCheckerThread;
    private ArticleDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewNews = findViewById(R.id.webViewNews);

                if (webViewNews != null){
                    String url = webViewNews.getOriginalUrl();
                    databaseHelper.addArticle(url, url);
                    Toast.makeText(MainActivity.this, "Article added to favorites" + url, Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /* Content Checkers
           In this I start the threads that check for each website it there
           is new content. If there is something new on the website then a notification
           is thrown.
         */
        String websiteUrlRepubblica = "https://www.repubblica.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlRepubblica);
        contentCheckerThread.start();

        String websiteUrlGazzetta = "https://gazzetta.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlGazzetta);
        contentCheckerThread.start();

        String websiteUrlCorriere = "https://corriere.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlCorriere);
        contentCheckerThread.start();

        String websiteUrlSkyTg24 = "https://tg24.sky.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlSkyTg24);
        contentCheckerThread.start();

        String websiteUrlInternazionale = "https://internazionale.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlInternazionale);
        contentCheckerThread.start();

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.webView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.CorriereSeraFragment, R.id.foxNewsFragment, R.id.skyNewsFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        databaseHelper = new ArticleDatabaseHelper(this);

        displayFavoriteArticles();
    }

    /* This method obtain all the info about favourites articles from the database
       and then display them.
    */
    private void displayFavoriteArticles() {
        Cursor cursor = databaseHelper.getAllArticles();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ArticleDatabaseHelper.COLUMN_TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(ArticleDatabaseHelper.COLUMN_URL));
                Log.d("MainActivity", "Title: " + title + ", URL: " + url);
            }
            cursor.close();
        }
    }

    /*

     * This is the settings menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    } */

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
