package com.school.tyari.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.PrivacyPolicyActivity;
import com.school.tyari.R;

import java.util.HashMap;

import com.school.tyari.TermsActivity;
import com.school.tyari.others.AdmissionActivity;
import com.school.tyari.others.BlogActivity;
import com.school.tyari.others.CallActivity;
import com.school.tyari.others.ConveyanceActivity;
import com.school.tyari.others.SchoolDetailsActivity;
import com.school.tyari.others.TutorActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private LinearLayout BookCv,AdmissionCv,tutorCv,conveyanceCv,BlogCv,rating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //activity

        BookCv = findViewById(R.id.BookCv);
        AdmissionCv = findViewById(R.id.AdmissionCv);
        conveyanceCv = findViewById(R.id.conveyanceCv);
        tutorCv = findViewById(R.id.tutorCv);
        BlogCv = findViewById(R.id.BlogCv);
        rating = findViewById(R.id.rating);




        //drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbars);


        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();


        //------------Click Listener-----------------//

        BookCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainUserActivity.class));
                finish();
            }
        });

        conveyanceCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConveyanceActivity.class));
                finish();
            }
        });
        AdmissionCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdmissionActivity.class));
                finish();
            }
        });

        tutorCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TutorActivity.class));
                finish();
            }
        });
        BlogCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BlogActivity.class));
                finish();
            }
        });


        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.school.tyari")));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                break;

            case R.id.nav_profile:
                Intent intent2 = new Intent(MainActivity.this,ProfileEditUserActivity.class);
                startActivity(intent2);
                break;


            case R.id.nav_contactus:

                Uri uri = Uri.parse("tel:+918077198448");
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);

                break;


            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String sharebody = "https://play.google.com/store/apps/details?id=com.school.tyari";
                String shareSubject = "Share is care";

                sharingIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

                startActivity(Intent.createChooser(sharingIntent,"Share "));
                break;


            case R.id.nav_rate:
                rateMe();
                break;


            case R.id.nav_update:

                checkforupdate();
                break;

            case R.id.nav_privacy:

                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                finish();

                break;

            case R.id.nav_terms:

                startActivity(new Intent(MainActivity.this, TermsActivity.class));
                finish();

                break;


            case R.id.nav_logout:

                checkingUser();



                break;



        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    private void checkingUser() {

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
                                progressDialog.dismiss();
                                //user Other
                                logoutfirebasefirst();

                            }else if (account.equals("schoolsvm")) {
                                progressDialog.dismiss();
                                //user Other
                                logoutfirebasesecond();
                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }



    private void logoutfirebasefirst() {
        //if user is seller ,start seller main screen
        //if user is buyer,start user main screen

        progressDialog.setMessage("Logging Out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            String accountType = "" + ds.child("accountType").getValue();
                            if (accountType.equals("seller")) {
                                progressDialog.dismiss();
                                //user is seller
                                firebaseAuth.signOut();
                                checkUser();
                            }
                            else {
                                progressDialog.dismiss();
                                //user is buyer
                                firebaseAuth.signOut();
                                checkUser();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void logoutfirebasesecond() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");

        ref
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            String accountType = "" + ds.child("accountType").getValue();
                            if (accountType.equals("seller")) {
                                progressDialog.dismiss();
                                //user is seller
                                firebaseAuth.signOut();
                                checkUser();
                            }
                            else {
                                progressDialog.dismiss();
                                //user is buyer
                                firebaseAuth.signOut();
                                checkUser();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void makeMeOffline() {

        //after logging in , make user online
        progressDialog.setMessage("Logging Out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update succesfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void makeMeOfflinesecond() {
        //after logging in , make user online
        progressDialog.setMessage("Logging Out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update succesfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        else {

        }
    }

    private void checkforupdate() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.school.tyari")));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.school.tyari")));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
}