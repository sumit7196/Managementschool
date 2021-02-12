package com.school.tyari.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.school.tyari.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RegisterUserActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView profileIv;
    private EditText nameEt,phoneEt,addressEt,emailEt,
               passwordEt,cPasswordEt,codeEt;
    private Button registerBtn;
    private TextView registerSellerTv;


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


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        changeStatusBarColor();

        //init ui views
    //    backBtn = findViewById(R.id.backBtn);
        profileIv = findViewById(R.id.profileIv);
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        addressEt = findViewById(R.id.addressEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        cPasswordEt = findViewById(R.id.cpasswordEt);
        registerBtn = findViewById(R.id.registerBtn);
        codeEt = findViewById(R.id.codeEt);
        registerSellerTv = findViewById(R.id.registerSellerTv);

        //init permission array
     //   locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


   //     backBtn.setOnClickListener(new View.OnClickListener() {
   //         @Override
   //         public void onClick(View v) {
   //             onBackPressed();
   //         }
  //      });

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
                showImagePickDialog();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register user
                inputData();
            }
        });
        registerSellerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open register seller activity
                startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
            }
        });
    }

    ///register attractive layout
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }






    private String fullName,phoneNumber,city,address,email,password,confirmPassword,code;
    private void inputData() {
        //input data
        fullName = nameEt.getText().toString().trim();
        phoneNumber = phoneEt.getText().toString().trim();
        address = addressEt.getText().toString().trim();
        code = codeEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = cPasswordEt.getText().toString().trim();
        //validate data
        if (TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Enter Phone Name...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)){
            Toast.makeText(this, "Enter Code...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter Address...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Pattern...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<6){
            Toast.makeText(this, "Password must be atleast 6 character long...", Toast.LENGTH_SHORT).show();
            return;
        } if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Password doesn't match...", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();


    }

    private void createAccount() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //account created

                        checkUserType();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed creating account
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void checkUserType() {
        //if user is seller ,start seller main screen
        //if user is buyer,start user main screen


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        if (code.equals("cshp")){

            ref
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String account = ""+ds.child("account").getValue();
                                if (account.equals("schoolcshp")) {
                                    progressDialog.dismiss();
                                    //user is seller
                                    saverFirebaseData();
                                }

                                else {
                                    progressDialog.dismiss();
                                    //user is buyer
                                    Toast.makeText(RegisterUserActivity.this, "No path", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        }

        else if (code.equals("svm")){
            ref
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                String account = ""+ds.child("account").getValue();
                                if (account.equals("schoolsvm")) {
                                    progressDialog.dismiss();
                                    //user is seller
                                    saveSecondschooldata();
                                }



                            }
                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toast.makeText(RegisterUserActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
        }

    }

    private void saverFirebaseData() {
        progressDialog.setMessage("");

        final String timestamp = ""+System.currentTimeMillis();

        if (image_uri==null){
            //save info without image

            //setup data to save
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+firebaseAuth.getUid());
            hashMap.put("email",""+email);
            hashMap.put("name",""+fullName);
            hashMap.put("phone",""+phoneNumber);
            hashMap.put("address",""+address);
            hashMap.put("timestamp",""+timestamp);
            hashMap.put("accountType","User");
         //   hashMap.put("online","true");
            hashMap.put("code",""+code);
            hashMap.put("profileImage","");

            //save to db
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //db updated
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed updating db
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                            finish();
                        }
                    });
        }
        else {
            //save info with image

            //name and path of image
            String filePathAndName = "profile_images/" + ""+firebaseAuth.getUid();
            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //get url of uploaded images
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()){


                                //setup data to save
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("uid",""+firebaseAuth.getUid());
                                hashMap.put("email",""+email);
                                hashMap.put("name",""+fullName);
                                hashMap.put("phone",""+phoneNumber);
                                hashMap.put("address",""+address);
                                hashMap.put("timestamp",""+timestamp);
                                hashMap.put("code",""+code);
                                hashMap.put("accountType","User");
                     //           hashMap.put("online","true");
                                hashMap.put("profileImage",""+downloadImageUri); //url of uploaded image

                                //save to db
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolFirst");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //db updated
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed updating db
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                                                finish();
                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveSecondschooldata() {
        progressDialog.setMessage("");

        final String timestamp = ""+System.currentTimeMillis();

        if (image_uri==null){
            //save info without image

            //setup data to save
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+firebaseAuth.getUid());
            hashMap.put("email",""+email);
            hashMap.put("name",""+fullName);
            hashMap.put("phone",""+phoneNumber);
            hashMap.put("address",""+address);
            hashMap.put("timestamp",""+timestamp);
            hashMap.put("accountType","User");
        //    hashMap.put("online","true");
            hashMap.put("code",""+code);
            hashMap.put("profileImage","");

            //save to db
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
            ref.child(firebaseAuth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //db updated
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed updating db
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                            finish();
                        }
                    });
        }
        else {
            //save info with image

            //name and path of image
            String filePathAndName = "profile_images/" + ""+firebaseAuth.getUid();
            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //get url of uploaded images
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if (uriTask.isSuccessful()){


                                //setup data to save
                                HashMap<String,Object> hashMap = new HashMap<>();
                                hashMap.put("uid",""+firebaseAuth.getUid());
                                hashMap.put("email",""+email);
                                hashMap.put("name",""+fullName);
                                hashMap.put("phone",""+phoneNumber);
                                hashMap.put("address",""+address);
                                hashMap.put("timestamp",""+timestamp);
                                hashMap.put("code",""+code);
                                hashMap.put("accountType","User");
                     //           hashMap.put("online","true");
                                hashMap.put("profileImage",""+downloadImageUri); //url of uploaded image

                                //save to db
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SchoolSecond");
                                ref.child(firebaseAuth.getUid()).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //db updated
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed updating db
                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegisterUserActivity.this,MainActivity.class));
                                                finish();
                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

























    private void showImagePickDialog() {
            //option to display in dialog
            String[] options = {"Camera","Gallery"};
            //dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick Image")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                //camera clicked
                                if (checkCameraPermission()){
                                    //camera permission allowed
                                    pickFromCamera();
                                }
                                else {
                                    //not allowed ,request
                                    requestCameraPermission();

                                }
                            }
                            else {
                                //gallery clicked
                                if (checkStoragePermission()){
                                    //storage permission allowed
                                    pickFromGallery();

                                }
                                else {
                                    //not allowed,request
                                    requestStoragePermission();

                                }
                            }
                        }
                    })
                    .show();
        }

        private void  pickFromGallery(){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
        }

        private void pickFromCamera(){
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image Title");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image Description");

            image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
            startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
        }







        private boolean checkStoragePermission(){
            boolean result = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    (PackageManager.PERMISSION_GRANTED);

            return result;
        }

        private void requestStoragePermission(){
            ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
        }

        private boolean checkCameraPermission(){
            boolean result = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) ==
                    (PackageManager.PERMISSION_GRANTED);

            boolean result1 = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    (PackageManager.PERMISSION_GRANTED);


            return result && result1;
        }

        private void requestCameraPermission(){
            ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
        }



        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode){


                case CAMERA_REQUEST_CODE:{
                    if (grantResults.length>0){
                        boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (cameraAccepted && storageAccepted){
                            //permission allowed
                            pickFromCamera();
                        }
                        else {
                            //permission denied
                            Toast.makeText(this, "Camera Permissions are Necessary ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
                case STORAGE_REQUEST_CODE:{
                    if (grantResults.length>0){
                        boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                        if (storageAccepted){
                            //permission allowed
                            pickFromGallery();
                        }
                        else {
                            //permission denied
                            Toast.makeText(this, "Storage Permissions is Necessary ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            }




            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){

                //get Picked image
                image_uri = data.getData();
                //set to imageview
                profileIv.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){

                //set to imageview
                profileIv.setImageURI(image_uri);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
