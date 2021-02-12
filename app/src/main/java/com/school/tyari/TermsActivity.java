package com.school.tyari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TermsActivity extends AppCompatActivity {

    WebView mwebview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        mwebview = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mwebview.loadUrl("file:///android_asset/terms.html");
    }
}