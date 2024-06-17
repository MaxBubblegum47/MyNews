package com.example.myapplication.ui.FavoriteArticles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ArticleDatabaseHelper;
import com.example.myapplication.R;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private WebView webViewFavoriteArticles;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        webViewFavoriteArticles = root.findViewById(R.id.webViewNews);
        loadFavoriteArticles();

        return root;
    }

    private void loadFavoriteArticles() {
        ArticleDatabaseHelper databaseHelper = new ArticleDatabaseHelper(requireContext());
        ArrayList<String> favoriteArticles = databaseHelper.getAllArticleTitles();

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body>");

        for (String articleTitle : favoriteArticles) {
            htmlContent.append("<a href=\"").append(articleTitle).append("\">").append(articleTitle).append("</a><br>");
        }

        htmlContent.append("</body></html>");

        webViewFavoriteArticles.loadData(htmlContent.toString(), "text/html", "UTF-8");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webViewFavoriteArticles = null;
    }
}
