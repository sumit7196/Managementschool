package com.school.tyari.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.school.tyari.R;
import com.school.tyari.activities.MainActivity;
import com.school.tyari.activities.MainUserActivity;
import com.school.tyari.others.AdmissionActivity;
import com.school.tyari.others.BlogActivity;
import com.school.tyari.others.ConveyanceActivity;
import com.school.tyari.others.TutorActivity;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;


public class HomeFragment extends Fragment {
    private SliderLayout sliderLayout;


    private LinearLayout BookCv,tutorCv,conveyanceCv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        BookCv = view.findViewById(R.id.BookCv);
   //     AdmissionCv = view.findViewById(R.id.AdmissionCv);
        conveyanceCv = view.findViewById(R.id.conveyanceCv);
        tutorCv = view.findViewById(R.id.tutorCv);




        sliderLayout = view.findViewById(R.id.slider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(1);


        setSliderViews();


        BookCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),MainUserActivity.class );
                startActivity(intent);
            }
        });

        conveyanceCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),ConveyanceActivity.class );
                startActivity(intent);
            }
        });



        /*
        AdmissionCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),AdmissionActivity.class );
                startActivity(intent);
            }
        });

         */

        tutorCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TutorActivity.class );
                startActivity(intent);
            }
        });





        return view;
    }






    private void setSliderViews() {
        for (int i = 0; i< 5; i++){
            DefaultSliderView sliderView = new DefaultSliderView(getContext());
            switch (i){
                case 0:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/s1.jpg?alt=media&token=bbb9c7f3-6e35-4505-b743-61bc966030ed");
                    break;
                case 1:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/s2.jpeg?alt=media&token=ec73f3f4-7ed0-4d68-8254-a25a3fca5f68");
                    break;
                case 2:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/s3.jpg?alt=media&token=9768450d-4434-4ad5-a585-73fc073066cc");
                    break;
                case 3:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/s4.jpg?alt=media&token=b1ab4fca-ce03-4e93-ad0c-64fe494ae537");
                    break;
                case 4:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/gp-adampur.appspot.com/o/slider%2F005.JPG?alt=media&token=bf0b5676-3c75-4616-a690-242093cccce0");
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            sliderLayout.addSliderView(sliderView);
        }
    }



}