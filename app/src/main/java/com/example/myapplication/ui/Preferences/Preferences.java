package com.example.myapplication.ui.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RadioGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import android.widget.TextView;
import com.example.myapplication.ArticleSuggestion;
import java.util.List;
public class Preferences extends Fragment {
    private WebView webViewFavoriteArticles;

    private SharedPreferences sharedPreferences;
    private ToggleButton toggleButton1, toggleButton2, toggleButton3, toggleButton4;
    private TextView suggestedArticlesTextView;

    private RadioGroup radioGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.preferences, container, false);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        toggleButton1 = rootView.findViewById(R.id.toggleButton1);
        toggleButton2 = rootView.findViewById(R.id.toggleButton2);
        toggleButton3 = rootView.findViewById(R.id.toggleButton3);
        toggleButton4 = rootView.findViewById(R.id.toggleButton4);

        webViewFavoriteArticles = rootView.findViewById(R.id.suggestedArticlesTextView);

        // suggestedArticlesTextView = rootView.findViewById(R.id.suggestedArticlesTextView);

        // Retrieve and set the saved state for toggle buttons
        toggleButton1.setChecked(sharedPreferences.getBoolean("toggle1", false));
        toggleButton2.setChecked(sharedPreferences.getBoolean("toggle2", false));
        toggleButton3.setChecked(sharedPreferences.getBoolean("toggle3", false));
        toggleButton4.setChecked(sharedPreferences.getBoolean("toggle4", false));


        // Add listeners to toggle buttons to save their state when changed
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle1", isChecked);
                editor.apply();
            }
        });

        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle2", isChecked);
                editor.apply();
            }
        });

        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle3", isChecked);
                editor.apply();
            }
        });

        toggleButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("toggle4", isChecked);
                editor.apply();
            }
        });

        boolean politicsPref = toggleButton1.isChecked();
        boolean techPref = toggleButton2.isChecked();
        boolean autoPref = toggleButton3.isChecked();
        boolean musicPref = toggleButton4.isChecked();

        // Suggest article based on user preferences
        //String suggestedArticle = ArticleSuggestion.suggestArticles(politicsPref, sportsPref);

        List<ArticleSuggestion.Article> suggestedArticles = ArticleSuggestion.suggestArticles(politicsPref, techPref, autoPref, musicPref);

        // Create a StringBuilder to build the suggested articles string
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body>");

        for (ArticleSuggestion.Article article : suggestedArticles) {
            htmlContent.append("<a href=\"").append(article.getTitle()).append("\">").append(article.getTitle()).append("</a><br>");
        }

        htmlContent.append("</body></html>");

        webViewFavoriteArticles.loadData(htmlContent.toString(), "text/html", "UTF-8");


        return rootView;
    }
}
