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

import androidx.fragment.app.Fragment;

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


    private LinearLayout BookCv,AdmissionCv,tutorCv,conveyanceCv,rating;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        BookCv = view.findViewById(R.id.BookCv);
        AdmissionCv = view.findViewById(R.id.AdmissionCv);
        conveyanceCv = view.findViewById(R.id.conveyanceCv);
        tutorCv = view.findViewById(R.id.tutorCv);
        rating = view.findViewById(R.id.rating);




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
        AdmissionCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),AdmissionActivity.class );
                startActivity(intent);
            }
        });

        tutorCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TutorActivity.class );
                startActivity(intent);
            }
        });



        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.school.tyari")));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

             */
            }
        });




        return view;
    }


    private void setSliderViews() {
        for (int i = 0; i< 5; i++){
            DefaultSliderView sliderView = new DefaultSliderView(getContext());
            switch (i){
                case 0:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/gp-adampur.appspot.com/o/slider%2Fcollege.jpg?alt=media&token=1763b1ad-6bf7-47a5-be9e-db531b3ce1cf");
                    break;
                case 1:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/gp-adampur.appspot.com/o/slider%2F001.JPG?alt=media&token=db445573-0344-4811-ab02-a1917b2ab007");
                    break;
                case 2:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/gp-adampur.appspot.com/o/slider%2F002.JPG?alt=media&token=620d5166-784e-4317-a9ba-48532c5c671f");
                    break;
                case 3:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/gp-adampur.appspot.com/o/slider%2F003.JPG?alt=media&token=c70595d5-5969-4191-be2f-583eb171ae37");
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