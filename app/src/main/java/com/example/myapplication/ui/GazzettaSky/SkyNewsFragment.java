package com.example.myapplication.ui.GazzettaSky;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.myapplication.WebViewController;
import com.example.myapplication.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkyNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkyNewsFragment extends Fragment {


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = binding.webViewNews;
        webView.loadUrl("https://tg24.sky.it");
        webView.setWebViewClient(new WebViewController());

        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}