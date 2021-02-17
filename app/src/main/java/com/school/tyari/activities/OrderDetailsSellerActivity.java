package com.school.tyari.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
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
import com.school.tyari.adapter.AdapterOrderedItem;
import com.school.tyari.models.ModelOrdereditem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDetailsSellerActivity extends AppCompatActivity {

    //ui views
    private ImageButton backBtn,editBtn,mapBtn;
    private TextView orderIdTv,dateTv,orderStatusTv,emailTv,phoneTv,totalItemsTv,amountTv,addressTv;
    private RecyclerView itemsRv;

    String orderId,orderBy;
    //to open destination in map
    String sourceLatitude,sourceLongitude,destinationLatitude,destinationLongitude;

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelOrdereditem> ordereditemArrayList;
    private AdapterOrderedItem adapterOrderedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_seller);

        //init ui views
        backBtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editBtn);
        mapBtn = findViewById(R.id.mapBtn);
        orderIdTv = findViewById(R.id.orderIdTv);
        dateTv = findViewById(R.id.dateTv);
        orderStatusTv = findViewById(R.id.orderStatusTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        totalItemsTv = findViewById(R.id.totalItemsTv);
        amountTv = findViewById(R.id.amountTv);
        addressTv = findViewById(R.id.addressTv);
        itemsRv = findViewById(R.id.itemsRv);



        //get data from intent
        orderId= getIntent().getStringExtra("orderId");
        orderBy = getIntent().getStringExtra("orderBy");

        firebaseAuth = FirebaseAuth.getInstance();
        loadMyInfo();

       // checkingUserinfo();
      //  checkingOrderDetails();
      //  checkingOrderItems();


          loadBuyerInfo();
        loadOrderDetails();
        loadOrderedItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit order status : In progress,Completed, Cnacelled
                editOrderStatusDialog();
            }
        });


    }


    private void checkingUserinfo() {

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
                                loadBuyerInfo();

                            }else if (account.equals("schoolsvm")) {

                                //user Other
                                loadBuyerInfosecond();

                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(OrderDetailsSellerActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void checkingOrderDetails() {

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
                                loadOrderDetails();

                            }else if (account.equals("schoolsvm")) {

                                //user Other
                                loadOrderDetailssecond();

                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(OrderDetailsSellerActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void checkingOrderItems() {

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
                                loadOrderedItems();

                            }else if (account.equals("schoolsvm")) {

                                //user Other
                                loadOrderedItemssecond();

                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(OrderDetailsSellerActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }







    private void editOrderStatusDialog() {
        //option to display dialog
        final String[] options = {"In Progress","Completed","Cancelled"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Order Status")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks
                        String selectedOption = options[which];
                        editOrderStatus(selectedOption);
                    }
                })
                .show();
    }

    private void editOrderStatus(final String selectedOption) {
        //setup data to put in firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus",""+selectedOption);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String message = "Order is now"+selectedOption;
                        //status updated
                        Toast.makeText(OrderDetailsSellerActivity.this,message , Toast.LENGTH_SHORT).show();

                        prepareNotificationMessage(orderId,message);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failed updating status ,show reason
                        Toast.makeText(OrderDetailsSellerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sourceLatitude = ""+snapshot.child("latitude").getValue();
                        sourceLongitude = ""+snapshot.child("longitude").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadBuyerInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(orderBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        destinationLatitude = ""+snapshot.child("latitude").getValue();
                        destinationLongitude = ""+snapshot.child("longitude").getValue();
                        String email = ""+snapshot.child("email").getValue();
                        String phone = ""+snapshot.child("phone").getValue();

                        //set info
                        emailTv.setText(email);
                        phoneTv.setText(phone);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadBuyerInfosecond() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
        ref.child(orderBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        destinationLatitude = ""+snapshot.child("latitude").getValue();
                        destinationLongitude = ""+snapshot.child("longitude").getValue();
                        String email = ""+snapshot.child("email").getValue();
                        String phone = ""+snapshot.child("phone").getValue();

                        //set info
                        emailTv.setText(email);
                        phoneTv.setText(phone);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void loadOrderDetails() {
        //load detailed info of this order , based on order id
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get order info
                        String orderBy = ""+snapshot.child("orderBy").getValue();
                        String orderCost = ""+snapshot.child("orderCost").getValue();
                        String orderId = ""+snapshot.child("orderId").getValue();
                        String orderStatus = ""+snapshot.child("orderStatus").getValue();
                        String orderTime = ""+snapshot.child("orderTime").getValue();
                        String orderTo = ""+snapshot.child("orderTo").getValue();
                        String deliveryFee = ""+snapshot.child("deliveryFee").getValue();
                        String latitude = ""+snapshot.child("latitude").getValue();
                        String longitude = ""+snapshot.child("longitude").getValue();

                        //convert time stamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(orderTime));
                        String dateFormated = DateFormat.format("dd/MM/yyyy",calendar).toString();

                        //order status
                        if (orderStatus.equals("In Progress")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                        else if (orderStatus.equals("Completed")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                        }
                        else if (orderStatus.equals("Cancelled")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed));
                        }

                        //set data
                        orderIdTv.setText(orderId);
                        orderStatusTv.setText(orderStatus);
                        amountTv.setText("$"+orderCost + "[Including deliver fee $"+deliveryFee+"]");
                        dateTv.setText(dateFormated);

                        findAddress(latitude,longitude); //to find delivery address

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadOrderDetailssecond() {
        //load detailed info of this order , based on order id
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get order info
                        String orderBy = ""+snapshot.child("orderBy").getValue();
                        String orderCost = ""+snapshot.child("orderCost").getValue();
                        String orderId = ""+snapshot.child("orderId").getValue();
                        String orderStatus = ""+snapshot.child("orderStatus").getValue();
                        String orderTime = ""+snapshot.child("orderTime").getValue();
                        String orderTo = ""+snapshot.child("orderTo").getValue();
                        String deliveryFee = ""+snapshot.child("deliveryFee").getValue();
                        String latitude = ""+snapshot.child("latitude").getValue();
                        String longitude = ""+snapshot.child("longitude").getValue();

                        //convert time stamp
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(Long.parseLong(orderTime));
                        String dateFormated = DateFormat.format("dd/MM/yyyy",calendar).toString();

                        //order status
                        if (orderStatus.equals("In Progress")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                        else if (orderStatus.equals("Completed")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                        }
                        else if (orderStatus.equals("Cancelled")){
                            orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed));
                        }

                        //set data
                        orderIdTv.setText(orderId);
                        orderStatusTv.setText(orderStatus);
                        amountTv.setText("$"+orderCost + "[Including deliver fee $"+deliveryFee+"]");
                        dateTv.setText(dateFormated);

                        findAddress(latitude,longitude); //to find delivery address

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadOrderedItems(){
        //load the products/items of order

        //init list
        ordereditemArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ordereditemArrayList.clear(); //before adding data clear list
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelOrdereditem modelOrdereditem = ds.getValue(ModelOrdereditem.class);
                            //add to list
                            ordereditemArrayList.add(modelOrdereditem);
                        }
                        //setup adapter
                        adapterOrderedItem = new AdapterOrderedItem(OrderDetailsSellerActivity.this,ordereditemArrayList);
                        //set adapter to our recyclerview
                        itemsRv.setAdapter(adapterOrderedItem);

                        //set total number of items/products in order
                        totalItemsTv.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadOrderedItemssecond(){
        //load the products/items of order

        //init list
        ordereditemArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId).child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ordereditemArrayList.clear(); //before adding data clear list
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelOrdereditem modelOrdereditem = ds.getValue(ModelOrdereditem.class);
                            //add to list
                            ordereditemArrayList.add(modelOrdereditem);
                        }
                        //setup adapter
                        adapterOrderedItem = new AdapterOrderedItem(OrderDetailsSellerActivity.this,ordereditemArrayList);
                        //set adapter to our recyclerview
                        itemsRv.setAdapter(adapterOrderedItem);

                        //set total number of items/products in order
                        totalItemsTv.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }










    private void openMap() {
        //saddr means source address
        //daddr means destination address
        String address = "https://maps.google.com/maps?saddr=" + sourceLatitude +","+ sourceLongitude+"&daddr=" + destinationLatitude + "," + destinationLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);
    }

    private void findAddress(String latitude, String longitude) {
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat,lon,1);

            //complete address
            String address = addresses.get(0).getAddressLine(0);
            addressTv.setText(address);
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //notification to orders status changed

    private void  prepareNotificationMessage(String orderId,String message){
        //when user seller changes order status Inprogress/Cancelled/Completed, send notification to buyer

        //preapre data for notification
        String NOTIFICATION_TOPIC = "/topics/"+ Constants.FCM_TOPIC; //must be same as subscribed by user
        String NOTIFICATION_TITLE = "Your Order "+ orderId;
        String NOTIFICATION_MESSAGE = "" + message;
        String NOTIFICATION_TYPE = "OrderStatusChanged";

        //prepare json(what to send and where to send)
        JSONObject notificationJo = new JSONObject();
        JSONObject notificationBodyJo = new JSONObject();
        try {
            //what to send
            notificationBodyJo.put("notificationType",NOTIFICATION_TYPE);
            notificationBodyJo.put("buyerUid",orderBy);
            notificationBodyJo.put("sellerUid",firebaseAuth.getUid()); //since we are logged in as seller to change order status so that current user uid is seller uid
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
        sendFcmNotification(notificationJo);
    }

    private void sendFcmNotification(JSONObject notificationJo) {
        //send volley request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //notification sent

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               //notification failed
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