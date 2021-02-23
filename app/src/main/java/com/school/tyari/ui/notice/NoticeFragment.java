package com.school.tyari.ui.notice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.R;
import com.school.tyari.activities.MainUserActivity;

import java.util.ArrayList;


public class NoticeFragment extends Fragment {

    private RecyclerView deleteNoticeRecycler;
    private ProgressBar progressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    private DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        deleteNoticeRecycler = view.findViewById(R.id.deleteNoticeRecycler);
        progressBar = view.findViewById(R.id.progressBar);


        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        deleteNoticeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        deleteNoticeRecycler.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();


        getNotice();

        return view;
    }



    private void getNotice() {


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NoticeData data = snapshot.getValue(NoticeData.class);
                    list.add(0,data);
                }

                adapter = new NoticeAdapter(getContext(), list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteNoticeRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(),databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });


    }



}