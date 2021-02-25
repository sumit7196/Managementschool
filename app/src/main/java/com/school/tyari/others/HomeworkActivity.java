package com.school.tyari.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.Constants;
import com.school.tyari.R;
import com.school.tyari.activities.OrderDetailsUsersActivity;
import com.school.tyari.activities.ShopDetailsActivity;
import com.school.tyari.adapter.AdapterCartitem;
import com.school.tyari.adapter.AdapterHomework;
import com.school.tyari.adapter.AdapterProductUser;
import com.school.tyari.models.ModelCartitem;
import com.school.tyari.models.ModelProduct;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class HomeworkActivity extends AppCompatActivity {

    private ImageView shopIv;
    private TextView filteredProductsTv;
    private ImageButton filterProductBtn;
    private EditText searchProductEt;
    private RecyclerView homeRv;


    private String shopUid;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelProduct> productsList;
    private AdapterHomework adapterHomework;
    //progress dialog
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        //init ui views
        shopIv = findViewById(R.id.shopIv);
        searchProductEt = findViewById(R.id.searchProductEt);
        filterProductBtn = findViewById(R.id.filterProductBtn);
        filteredProductsTv = findViewById(R.id.filteredProductsTv);
        homeRv = findViewById(R.id.homeRv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();

    //    loadMyInfo();
    //    loadShopDetails();
        loadShopProducts();




        //search
        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterHomework.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeworkActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Constants.productcategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.productcategories1[which];
                                filteredProductsTv.setText(selected);
                                if (selected.equals("All")){
                                    //load all
                                    loadShopProducts();

                                }
                                else {
                                    //load filtered
                                    adapterHomework.getFilter().filter(selected);
                                }
                            }
                        })
                        .show();
            }
        });


    }



    private void loadShopProducts() {
        //init list
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Homework")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list before addding items
                        productsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productsList.add(modelProduct);
                        }
                        //setup adapter
                        adapterHomework = new AdapterHomework(HomeworkActivity.this,productsList);
                        //set adapter
                        homeRv.setAdapter(adapterHomework);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}