package com.school.tyari.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.school.tyari.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivityQuiz extends AppCompatActivity {

    private TextView appName;

    public static List<CategoryModel> catList = new ArrayList<>();
    public static int selected_cat_index = 0;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_quiz);

          appName = findViewById(R.id.appName);

          Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
          appName.setTypeface(typeface);

          Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.myanim);
          appName.setAnimation(anim);

        firestore = FirebaseFirestore.getInstance();

        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadData();
            }
        }.start();

    }

    private void loadData()
    {
        catList.clear();

        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long count = (long)doc.get("COUNT");

                        for(int i=1; i <= count; i++)
                        {
                            String catName = doc.getString("CAT" + String.valueOf(i) + "_NAME");
                            String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            catList.add(new CategoryModel(catID,catName));
                        }


                        Intent intent = new Intent(SplashActivityQuiz.this, MainQuizActivity.class);
                        startActivity(intent);
                        SplashActivityQuiz.this.finish();

                    }
                    else
                    {
                        Toast.makeText(SplashActivityQuiz.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(SplashActivityQuiz.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}