package com.school.tyari.ui.faculty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.R;

import java.util.ArrayList;
import java.util.List;


public class FacultyFragment extends Fragment {

    private RecyclerView englishDepart, mathDepart, physicsDepartment, chemistryDepartment,biologyDepartment,
            computerDepartment,physicalDepartment,hindiDepartment;
    private LinearLayout engNoData, mathNoData, physicsNoData, chemistryNoData,bioNoData,
            computrNoData,physicalNoData,hindiNoData;
    private List<TeacherData> list1, list2, list3 , list4;
    private TeacherAdapter adapter;

    private DatabaseReference reference, dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);


        englishDepart = view.findViewById(R.id.csDepartment);
        mathDepart = view.findViewById(R.id.mechanicalDepartment);
        physicsDepartment = view.findViewById(R.id.physicsDepartment);
        chemistryDepartment = view.findViewById(R.id.chemistryDepartment);
        biologyDepartment = view.findViewById(R.id.biologyDepartment);
        computerDepartment = view.findViewById(R.id.computerDepartment);
        physicalDepartment = view.findViewById(R.id.physicalDepartment);
        hindiDepartment = view.findViewById(R.id.hindiDepartment);


        engNoData = view.findViewById(R.id.csNoData);
        mathNoData = view.findViewById(R.id.mechNoData);
        physicsNoData = view.findViewById(R.id.physicsNoData);
        chemistryNoData = view.findViewById(R.id.chemistryNoData);
        bioNoData = view.findViewById(R.id.biologyNoData);
        computrNoData = view.findViewById(R.id.computerNoData);
        physicalNoData = view.findViewById(R.id.physicalNoData);
        hindiNoData = view.findViewById(R.id.hindiNoData);

     //   reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");

      //  checkingUserinfo();
        englishDepartment();
        mathDepartment();
        physicsDepartment();
        chemistryDepartment();
        biologyDepartment();
        computerDepartment();
        physicalDepartment();
        hindiDepartment();


        return view;
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
                                englishDepartment();
                                mathDepartment();
                                physicsDepartment();
                                chemistryDepartment();
                                biologyDepartment();
                                computerDepartment();
                                physicalDepartment();
                                hindiDepartment();

                            }else if (account.equals("schoolsvm")) {

                                //user Other
                          //      englishDepartmentsecond();
                          //      mathDepartmentsecond();
                          //      physicsDepartmentsecond();
                          //      chemistryDepartmentsecond();
                          //      biologyDepartmentsecond();
                          //      computerDepartmentsecond();
                          //      physicalDepartmentsecond();
                          //      hindiDepartmentsecond();


                            }


                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });

    }


    private void englishDepartment() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");

        dbRef = reference.child("English");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    engNoData.setVisibility(View.VISIBLE);
                    englishDepart.setVisibility(View.GONE);
                }else {
                    engNoData.setVisibility(View.GONE);
                    englishDepart.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    englishDepart.setHasFixedSize(true);
                    englishDepart.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list1, getContext());
                    englishDepart.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mathDepartment() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");


        dbRef = reference.child("Math");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    mathNoData.setVisibility(View.VISIBLE);
                    mathDepart.setVisibility(View.GONE);
                }else {
                    mathNoData.setVisibility(View.GONE);
                    mathDepart.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    mathDepart.setHasFixedSize(true);
                    mathDepart.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list2, getContext());
                    mathDepart.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void physicsDepartment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");


        dbRef = reference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else {
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list3, getContext());
                    physicsDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chemistryDepartment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");


        dbRef = reference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    chemistryNoData.setVisibility(View.VISIBLE);
                    chemistryDepartment.setVisibility(View.GONE);
                }else {
                    chemistryNoData.setVisibility(View.GONE);
                    chemistryDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    chemistryDepartment.setHasFixedSize(true);
                    chemistryDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    chemistryDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void biologyDepartment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");


        dbRef = reference.child("Biology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    bioNoData.setVisibility(View.VISIBLE);
                    biologyDepartment.setVisibility(View.GONE);
                }else {
                    bioNoData.setVisibility(View.GONE);
                    biologyDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    biologyDepartment.setHasFixedSize(true);
                    biologyDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    biologyDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void computerDepartment() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");

        dbRef = reference.child("Computer");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    computrNoData.setVisibility(View.VISIBLE);
                    computerDepartment.setVisibility(View.GONE);
                }else {
                    computrNoData.setVisibility(View.GONE);
                    computerDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    computerDepartment.setHasFixedSize(true);
                    computerDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    computerDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void physicalDepartment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");


        dbRef = reference.child("Physical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    physicalNoData.setVisibility(View.VISIBLE);
                    physicalDepartment.setVisibility(View.GONE);
                }else {
                    physicalNoData.setVisibility(View.GONE);
                    physicalDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    physicalDepartment.setHasFixedSize(true);
                    physicalDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    physicalDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hindiDepartment() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolFirst").child("teacher");

        dbRef = reference.child("Hindi");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    hindiNoData.setVisibility(View.VISIBLE);
                    hindiDepartment.setVisibility(View.GONE);
                }else {
                    hindiNoData.setVisibility(View.GONE);
                    hindiDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    hindiDepartment.setHasFixedSize(true);
                    hindiDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    hindiDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Second School Department

    private void englishDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("English");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    engNoData.setVisibility(View.VISIBLE);
                    englishDepart.setVisibility(View.GONE);
                }else {
                    engNoData.setVisibility(View.GONE);
                    englishDepart.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    englishDepart.setHasFixedSize(true);
                    englishDepart.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list1, getContext());
                    englishDepart.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mathDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Math");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    mathNoData.setVisibility(View.VISIBLE);
                    mathDepart.setVisibility(View.GONE);
                }else {
                    mathNoData.setVisibility(View.GONE);
                    mathDepart.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    mathDepart.setHasFixedSize(true);
                    mathDepart.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list2, getContext());
                    mathDepart.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void physicsDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else {
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list3, getContext());
                    physicsDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chemistryDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    chemistryNoData.setVisibility(View.VISIBLE);
                    chemistryDepartment.setVisibility(View.GONE);
                }else {
                    chemistryNoData.setVisibility(View.GONE);
                    chemistryDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    chemistryDepartment.setHasFixedSize(true);
                    chemistryDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    chemistryDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void biologyDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Biology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    bioNoData.setVisibility(View.VISIBLE);
                    biologyDepartment.setVisibility(View.GONE);
                }else {
                    bioNoData.setVisibility(View.GONE);
                    biologyDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    biologyDepartment.setHasFixedSize(true);
                    biologyDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    biologyDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void computerDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Computer");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    computrNoData.setVisibility(View.VISIBLE);
                    computerDepartment.setVisibility(View.GONE);
                }else {
                    computrNoData.setVisibility(View.GONE);
                    computerDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    computerDepartment.setHasFixedSize(true);
                    computerDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    computerDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void physicalDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Physical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    physicalNoData.setVisibility(View.VISIBLE);
                    physicalDepartment.setVisibility(View.GONE);
                }else {
                    physicalNoData.setVisibility(View.GONE);
                    physicalDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    physicalDepartment.setHasFixedSize(true);
                    physicalDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    physicalDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hindiDepartmentsecond() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SchoolSecond").child("teacher");

        dbRef = reference.child("Hindi");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    hindiNoData.setVisibility(View.VISIBLE);
                    hindiDepartment.setVisibility(View.GONE);
                }else {
                    hindiNoData.setVisibility(View.GONE);
                    hindiDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    hindiDepartment.setHasFixedSize(true);
                    hindiDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new TeacherAdapter(list4, getContext());
                    hindiDepartment.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}