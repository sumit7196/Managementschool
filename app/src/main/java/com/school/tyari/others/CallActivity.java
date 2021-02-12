package com.school.tyari.others;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.school.tyari.R;

public class CallActivity extends AppCompatActivity {

    private ImageButton call1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        call1 = findViewById(R.id.call1);

        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("tel:+918077198448");
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);

            }
        });
    }
}