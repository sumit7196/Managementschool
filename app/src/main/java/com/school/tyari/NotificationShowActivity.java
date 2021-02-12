package com.school.tyari;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NotificationShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_show);

        TextView textView = findViewById(R.id.tvNotification);

        String message = getIntent().getStringExtra("message");
        textView.setText(message);
    }
}