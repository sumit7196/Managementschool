package com.school.tyari.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make full screen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        //start login after 2 second
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //user not logged in start login activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    //user is logger in,check user type
                    checkinguser();

                  //  checkUserType();
                }

            }
        }, 1000);

    }

    private void checkinguser() {

        //if user is seller ,start seller main screen
        //if user is buyer,start user main screen


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String account = "" + ds.child("account").getValue();
                            if (account.equals("schoolcshp")) {
                                //user Other
                                checkUserType();

                            } else if (account.equals("schoolsvm")) {
                                //user Other
                                checkUserType2();

                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(SplashActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkUserType() {
        //if user is seller ,start seller main screen
        //if user is buyer,start user main screen

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       // for (DataSnapshot ds: snapshot.getChildren()){
                            String accountType = ""+snapshot.child("accountType").getValue();
                            if (accountType.equals("User")){
                                //user is seller
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                            else {
                                //user is buyer
                                startActivity(new Intent(SplashActivity.this,MainSellerActivity.class));
                                finish();
                            }
                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkUserType2() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");

        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String accountType = ""+ds.child("accountType").getValue();
                            if (accountType.equals("User")) {

                                //user is seller
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                            else {
                                //user is buyer
                                startActivity(new Intent(SplashActivity.this,MainSellerActivity.class));
                                finish();
                            }
                        }
                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

}