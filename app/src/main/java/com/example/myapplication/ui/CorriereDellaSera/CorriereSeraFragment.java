package com.example.myapplication.ui.CorriereDellaSera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ArticleDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.WebViewController;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.databinding.FragmentSlideshowBinding;

public class CorriereSeraFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = binding.webViewNews;
        webView.loadUrl("https://corriere.it");
        webView.setWebViewClient(new WebViewController());

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}