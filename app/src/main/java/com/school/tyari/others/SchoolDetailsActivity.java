package com.school.tyari.others;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.school.tyari.R;

import com.school.tyari.activities.MainActivity;

public class SchoolDetailsActivity extends AppCompatActivity {

    WebView mwebview,mwebviewsecond;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        //start here

        mwebviewsecond = (WebView) findViewById(R.id.schoolabout);

        backBtn = findViewById(R.id.backBtn);




        WebSettings webSettings = mwebviewsecond.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mwebviewsecond.loadUrl("file:///android_asset/schoolabout.html");



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}