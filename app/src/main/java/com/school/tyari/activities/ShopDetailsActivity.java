package com.school.tyari.activities;

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
import android.widget.RatingBar;
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
import com.squareup.picasso.Picasso;
import com.school.tyari.Constants;
import com.school.tyari.R;
import com.school.tyari.adapter.AdapterCartitem;
import com.school.tyari.adapter.AdapterProductUser;
import com.school.tyari.models.ModelCartitem;
import com.school.tyari.models.ModelProduct;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class ShopDetailsActivity extends AppCompatActivity {


    private ImageView shopIv;
    private TextView shopNameTv,openCloseTv,filteredProductsTv,cartCountTv;
    private ImageButton cartBtn,backBtn,filterProductBtn;
    private EditText searchProductEt;
    private RecyclerView productsRv;


    private String shopUid;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelProduct> productsList;
    private AdapterProductUser adapterProductUser;
    //progress dialog
    private ProgressDialog progressDialog;

    //cart
    private ArrayList<ModelCartitem> cartItemList;
    private AdapterCartitem adapterCartitem;

    private String  myPhone;
    private String shopName;
    public String deliveryFee;

    private EasyDB easyDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        //init ui views
        shopIv = findViewById(R.id.shopIv);
        shopNameTv = findViewById(R.id.shopNameTv);
        openCloseTv = findViewById(R.id.openCloseTv);
        cartBtn = findViewById(R.id.cartBtn);
        backBtn = findViewById(R.id.backBtn);
        searchProductEt = findViewById(R.id.searchProductEt);
        filterProductBtn = findViewById(R.id.filterProductBtn);
        filteredProductsTv = findViewById(R.id.filteredProductsTv);
        productsRv = findViewById(R.id.productsRv);
        cartCountTv= findViewById(R.id.cartCountTv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();
        checkmyInfo();
        checkloadShopProducts();
       // loadMyInfo();
        loadShopDetails();
      //  loadShopProducts();



        //declare it to class level and init in onCreate
        easyDB = EasyDB.init(this,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id",new String[]{"text","unique"}))
                .addColumn(new Column("Item_PId",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity",new String[]{"text","not null"}))
                .doneTableColumn();

        //each shop have its own products and orders so if user add items to cart and go back and open cart in difference shop then cart should be differen
        //so delete cart data whenever user open this activity
        deleteCartData();
        cartCount();

        //search
        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterProductUser.getFilter().filter(s);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go previous activity
                onBackPressed();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show cart dialog
                showCartDialog();
            }
        });




        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Constants.productcategories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Constants.productcategories1[which];
                                filteredProductsTv.setText(selected);
                                if (selected.equals("All")){
                                    //load all
                                    checkloadShopProducts();

                                }
                                else {
                                    //load filtered
                                    adapterProductUser.getFilter().filter(selected);
                                }
                            }
                        })
                        .show();
            }
        });


    }



    private void deleteCartData() {
        easyDB.deleteAllDataFromTable(); //delete all records from table

    }
    public void cartCount(){
        //keep it public so we can access in adapter
        //get cart count
        int count = easyDB.getAllData().getCount();
        if (count<=0){
            //no item in cart,hide cart count view
            cartCountTv.setVisibility(View.GONE);
        }
        else {
            // have items in cart,show cart count textview and set count
            cartCountTv.setVisibility(View.VISIBLE);
            cartCountTv.setText(""+count); // concatenate with string, because we cant set integer in textview
        }
    }

    public   double allTotalPrice = 0.00;
    //need to access this views in adapter so making public
    public TextView sTotalTv;
    public TextView dFeeTv;
    public TextView allTotalPriceTv;







    private void checkmyInfo() {

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
                                loadMyInfo();

                            }else if (account.equals("schoolsvm")) {
                                progressDialog.dismiss();
                                //user Other
                                loadMyInfosecond();

                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(ShopDetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void checkloadShopProducts() {

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
                                loadShopProducts();

                            }else if (account.equals("schoolsvm")) {
                                progressDialog.dismiss();
                                //user Other
                                loadShopProductssecond();


                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(ShopDetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }



    private void showCartDialog() {
        //init list
        cartItemList = new ArrayList<>();
        //inflate cart layout
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_cart,null);
        //init views
        TextView shopNameTv = view.findViewById(R.id.shopNameTv);
        RecyclerView cartItemsRv = view.findViewById(R.id.cartItemsRv);
        sTotalTv = view.findViewById(R.id.sTotalTv);
        dFeeTv = view.findViewById(R.id.dFeeTv);
        allTotalPriceTv = view.findViewById(R.id.totalTv);
        Button checkoutBtn = view.findViewById(R.id.checkoutBtn);

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set view to dialog
        builder.setView(view);

        shopNameTv.setText(shopName);

        EasyDB easyDB = EasyDB.init(this,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id",new String[]{"text","unique"}))
                .addColumn(new Column("Item_PId",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity",new String[]{"text","not null"}))
                .doneTableColumn();

        //get all records from db
        Cursor res = easyDB.getAllData();
        while (res.moveToNext()){
            String id = res.getString(1);
            String pId = res.getString(2);
            String name = res.getString(3);
            String price = res.getString(4);
            String cost = res.getString(5);
            String quantity = res.getString(6);

            allTotalPrice = allTotalPrice + Double.parseDouble(cost);

            ModelCartitem modelCartitem = new ModelCartitem(
                    ""+id,
                    ""+pId,
                    ""+name,
                    ""+price,
                    ""+cost,
                    ""+quantity
            );

            cartItemList.add(modelCartitem);
        }
        //setup adapter
        adapterCartitem = new AdapterCartitem(this,cartItemList);
        //set to recyclerview
        cartItemsRv.setAdapter(adapterCartitem);

        dFeeTv.setText("$"+deliveryFee);
        sTotalTv.setText("$"+String.format("%.2f",allTotalPrice));
        allTotalPriceTv.setText("$"+(allTotalPrice));

        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        //reset total price on dialog dismiss
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                allTotalPrice = 0.00;
            }
        });

        //place order
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first validate delivery address


                if (myPhone.equals("") || myPhone.equals("null")){
                    //user didn't enter your phone number in your profile

                    Toast.makeText(ShopDetailsActivity.this, "Please enter your phone number in your profile before placing order...", Toast.LENGTH_SHORT).show();
                    return; //don't Proceed Further
                }
                if (cartItemList.size() == 0){
                    //cart list is empty
                    Toast.makeText(ShopDetailsActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
                    return; //dont proceed further

                }
                submitOrder();
            }
        });


    }

    private void submitOrder() {
        // show progress dialog
        progressDialog.setMessage("Placing Order...");
        progressDialog.show();

        //for order id and order time
        final String timestamp =  ""+System.currentTimeMillis();

        String cost = allTotalPriceTv.getText().toString().trim().replace("$",""); //remove $ if contains

        //add latitude,longitude of user to each order | delete previous order from firebase or add manually to them


        //setup order data
        final HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("orderId",""+timestamp);
        hashMap.put("orderTime",""+timestamp);
        hashMap.put("orderStatus","In progress");
        hashMap.put("orderCost",""+cost);
        hashMap.put("orderBy",""+firebaseAuth.getUid());
        hashMap.put("orderTo",""+shopUid);



        //add to db
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(shopUid).child("Orders");
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //order info added now add order items
                        for (int i=0; i<cartItemList.size(); i++) {
                            String pId = cartItemList.get(i).getpId();
                            String id = cartItemList.get(i).getId();
                            String cost = cartItemList.get(i).getCost();
                            String name = cartItemList.get(i).getName();
                            String price = cartItemList.get(i).getPrice();
                            String quantity = cartItemList.get(i).getQuantity();

                            HashMap<String, String> hashMap1 = new HashMap<>();
                            hashMap1.put("pId", pId);
                            hashMap1.put("name", name);
                            hashMap1.put("cost", cost);
                            hashMap1.put("price", price);
                            hashMap1.put("quantity", quantity);


                            ref.child(timestamp).child("Items").child(pId).setValue(hashMap1);

                        }
                        progressDialog.dismiss();
                        Toast.makeText(ShopDetailsActivity.this, "Order Placed Succesfull... ", Toast.LENGTH_SHORT).show();

                        prepareNotificationMessage(timestamp);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failed Placing order
                        progressDialog.dismiss();
                        Toast.makeText(ShopDetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            myPhone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadMyInfosecond() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            myPhone = ""+ds.child("phone").getValue();
                            String profileImage = ""+ds.child("profileImage").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String city = ""+ds.child("city").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(shopUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get shop data
                 String name = ""+snapshot.child("name").getValue();
                shopName = ""+snapshot.child("shopname").getValue();
              //  shopEmail = ""+snapshot.child("email").getValue();
             //   shopPhone = ""+snapshot.child("phone").getValue();
             //   shopLatitude = ""+snapshot.child("latitude").getValue();
             //   shopAddress = ""+snapshot.child("address").getValue();
            //    shopLongitude = ""+snapshot.child("Longitude").getValue();
           //     deliveryFee = ""+snapshot.child("deliveryFee").getValue();
                String profileImage = ""+snapshot.child("profileImage").getValue();
                String shopOpen = ""+snapshot.child("shopOpen").getValue();

                //set data
                shopNameTv.setText(shopName);
             //   emailTv.setText(shopEmail);
            //    deliveryFeeTv.setText("DeliveryFee: $"+deliveryFee);
            //    addressTv.setText(shopAddress);
            //    phoneTv.setText(shopPhone);

                if (shopOpen.equals("true")){

                    openCloseTv.setText("Open");
                }
                else {
                    openCloseTv.setText("Closed");
                }
                try {
                    Picasso.get().load(profileImage).into(shopIv);
                }
                catch (Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadShopProducts() {
        //init list
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        reference.child(shopUid).child("Products")
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
                        adapterProductUser = new AdapterProductUser(ShopDetailsActivity.this,productsList);
                        //set adapter
                        productsRv.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadShopProductssecond() {
        //init list
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond");
        reference.child(shopUid).child("Products")
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
                        adapterProductUser = new AdapterProductUser(ShopDetailsActivity.this,productsList);
                        //set adapter
                        productsRv.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }













    //notification to seller

    private void  prepareNotificationMessage(String orderId){
        //when user places order, send notification to seller

        //preapre data for notification
        String NOTIFICATION_TOPIC = "/topics/" +Constants.FCM_TOPIC; //must be same as subscribed by user
        String NOTIFICATION_TITLE = "New Order "+ orderId;
        String NOTIFICATION_MESSAGE = "Congratulations...! You have new order.";
        String NOTIFICATION_TYPE = "NewOrder";

        //prepare json(what to send and where to send)
        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();
        try {
            //what to send
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid",firebaseAuth.getUid()); //since we are logged in as buyer to place order so that current user uid is buyer uid
            notificationBodyJo.put("sellerUid",shopUid);
            notificationBodyJo.put("orderId",orderId);
            notificationBodyJo.put("notificationTitle",NOTIFICATION_TITLE);
            notificationBodyJo.put("notificationMessage",NOTIFICATION_MESSAGE);
            //where to send
            notificationJo.put("to",NOTIFICATION_TOPIC); //to all who subscribed this topic
            notificationJo.put("data",notificationBodyJo);

        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        sendFcmNotification(notificationJo,orderId);
    }

    private void sendFcmNotification(JSONObject notificationJo, final String orderId) {
        //send volley request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //after sending fcm start order details activity
                //after placing order open  order details page
                //open order details ,we need to keys there ,oderId,orderTo
                Intent intent = new Intent(ShopDetailsActivity.this, OrderDetailsUsersActivity.class);
                intent.putExtra("orderTo",shopUid);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //if failed sending fcm,still start order details activity
                Intent intent = new Intent(ShopDetailsActivity.this, OrderDetailsUsersActivity.class);
                intent.putExtra("orderTo",shopUid);
                intent.putExtra("orderId",orderId);
                startActivity(intent);
            }

    }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                //put required headers
                Map<String,String> headers = new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","key=" + Constants.FCM_KEY);

                return headers;
            }
        };

        //enque the volley request
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}