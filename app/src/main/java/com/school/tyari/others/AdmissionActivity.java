package com.school.tyari.others;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.R;

import com.school.tyari.NotificationShowActivity;
import com.school.tyari.activities.MainActivity;
import com.school.tyari.models.ModelAdmission;

public class AdmissionActivity extends AppCompatActivity {

    private EditText nameEt,phoneEt,stateEt,cityEt,commentEt,addressEt,classEt,schoolNameEt;
    private Button btn_conf_order;
    private ImageView profileIv;



    FirebaseDatabase database;
    DatabaseReference ref;
    int maxid = 0;
    ModelAdmission modelAdmission;
    private FirebaseAuth firebaseAuth;

    //permission constants
    //  private static  final int LOCATION_REQUEST_CODE = 100;
    private static  final int CAMERA_REQUEST_CODE = 200;
    private static  final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static  final int IMAGE_PICK_GALLERY_CODE = 400;
    private static  final int IMAGE_PICK_CAMERA_CODE = 500;


    //permission arrays
    //   private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //
    private Uri image_uri;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);

        nameEt = findViewById(R.id.nameEt);
        stateEt = findViewById(R.id.stateEt);
        cityEt = findViewById(R.id.cityEt);
        phoneEt=findViewById(R.id.phoneEt);
        commentEt = findViewById(R.id.commentEt);
        addressEt=findViewById(R.id.addressEt);
        classEt = findViewById(R.id.classEt);
        btn_conf_order = findViewById(R.id.btn_conf_order);


        //init permission array
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        modelAdmission = new ModelAdmission();
        ref = FirebaseDatabase.getInstance().getReference().child("AdmissionForm");








       // animation_view.setOnClickListener(new View.OnClickListener() {
     //       @Override
       //     public void onClick(View v) {
         //       try {
          //          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.sk.helloween")));
          //      }
          //      catch (ActivityNotFoundException e){
          //          startActivity(new Intent(Intent.ACTION_VIEW,
           //                 Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
         //       }
         //   }
      //  });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxid = (int) snapshot.getChildrenCount();
                }else {
                    ///
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_conf_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //order complete
                inputData();
            }
        });

    }

    private String fullName,phoneNumber,state,city,address,className,schoolName;

    private void inputData() {

        fullName = nameEt.getText().toString().trim();
        phoneNumber = phoneEt.getText().toString().trim();
        state = stateEt.getText().toString().trim();
        city = cityEt.getText().toString().trim();
        address = addressEt.getText().toString().trim();
        className = classEt.getText().toString().trim();
        schoolName = schoolNameEt.getText().toString().trim();



        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter Name...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber )){
            phoneEt.setError("Enter a right mobile number");
            Toast.makeText(this, "Enter Phone Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(state)){
            Toast.makeText(this, "Enter State Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(city)){
            Toast.makeText(this, "Enter City Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter  Address...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(className)){
            Toast.makeText(this, "Enter Admission Class ...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(schoolName)){
            Toast.makeText(this, "Enter Admission School ...", Toast.LENGTH_SHORT).show();
            return;
        }


        else {

            buttonhide();
            notification();
            startActivity(new Intent(AdmissionActivity.this, MainActivity.class));
            finish();

            Toast.makeText(this, "Your Details Submit", Toast.LENGTH_LONG).show();
        }


        modelAdmission.setFullname(nameEt.getText().toString());
        modelAdmission.setPhoneNumber(phoneEt.getText().toString());
        modelAdmission.setState(stateEt.getText().toString());
        modelAdmission.setCity(cityEt.getText().toString());
        modelAdmission.setAddress(addressEt.getText().toString());
        modelAdmission.setComment(commentEt.getText().toString());
        modelAdmission.setClassName(classEt.getText().toString());
        modelAdmission.setSchoolName(schoolNameEt.getText().toString());



        ref.child(String.valueOf(maxid+1)).setValue(modelAdmission);
    }
    private void buttonhide() {

        btn_conf_order.setVisibility(View.GONE);

    }

    private void notification() {
        String message = "Your Order is Done...";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                AdmissionActivity.this
        )
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("New Notification")
                .setContentText(message).setAutoCancel(true);

        Intent intent = new Intent(AdmissionActivity.this, NotificationShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        PendingIntent pendingIntent = PendingIntent.getActivity(AdmissionActivity.this,
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE
        );
        notificationManager.notify(0,builder.build());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);

    }
}