package com.example.myapplication.ui.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private SharedPreferences sharedPreferences;
    private ToggleButton toggleButton1, toggleButton2;
    private TextView suggestedArticlesTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.preferences, container, false);

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        toggleButton1 = rootView.findViewById(R.id.toggleButton1);
        toggleButton2 = rootView.findViewById(R.id.toggleButton2);

        suggestedArticlesTextView = rootView.findViewById(R.id.suggestedArticlesTextView);

        // Retrieve and set the saved state for toggle buttons
        toggleButton1.setChecked(sharedPreferences.getBoolean("toggle1", true));
        toggleButton2.setChecked(sharedPreferences.getBoolean("toggle2", true));

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

        boolean politicsPref = toggleButton1.isChecked();
        boolean sportsPref = toggleButton2.isChecked();


        // Suggest article based on user preferences
        //String suggestedArticle = ArticleSuggestion.suggestArticles(politicsPref, sportsPref);

        List<ArticleSuggestion.Article> suggestedArticles = ArticleSuggestion.suggestArticles(politicsPref, sportsPref);

        // Create a StringBuilder to build the suggested articles string
        StringBuilder suggestedArticlesStringBuilder = new StringBuilder();

        // Iterate over the list of suggested articles and append each article's title to the StringBuilder
        for (ArticleSuggestion.Article article : suggestedArticles) {
            suggestedArticlesStringBuilder.append(article.getTitle()).append("\n");
        }

        // Convert the StringBuilder to a String
        String suggestedArticlesString = suggestedArticlesStringBuilder.toString();
        suggestedArticlesTextView.setText(suggestedArticlesString);

        return rootView;
    }
}
