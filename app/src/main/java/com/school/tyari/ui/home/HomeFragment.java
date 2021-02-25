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
import com.school.tyari.others.HomeworkActivity;
import com.school.tyari.others.TutorActivity;
import com.school.tyari.quiz.SplashActivityQuiz;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;


public class HomeFragment extends Fragment {
    private SliderLayout sliderLayout;


    private LinearLayout BookCv,tutorCv,conveyanceCv,quizCv,homwrkCv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        BookCv = view.findViewById(R.id.BookCv);
   //     AdmissionCv = view.findViewById(R.id.AdmissionCv);
        conveyanceCv = view.findViewById(R.id.conveyanceCv);
        tutorCv = view.findViewById(R.id.tutorCv);
        quizCv = view.findViewById(R.id.quizCv);
        homwrkCv = view.findViewById(R.id.homwrkCv);




  //      sliderLayout = view.findViewById(R.id.slider);
  //      sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
 //       sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
 //       sliderLayout.setScrollTimeInSec(1);


 //       setSliderViews();


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


        quizCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SplashActivityQuiz.class );
                startActivity(intent);
            }
        });
        homwrkCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeworkActivity.class );
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
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/slide1.jpg?alt=media&token=3976910b-a1fb-4bd1-8007-83a45a83a594");
                    break;
                case 1:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/slide2.jpg?alt=media&token=31593853-56e6-4e3f-a727-f7ce7b43382f");
                    break;
                case 2:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/slide3.jpg?alt=media&token=b1a5a638-deb3-405b-8039-bb0548f9fb3b");
                    break;
                case 3:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/slide4.jpg?alt=media&token=118aa2fb-7b5d-4288-92fe-16a3720d1de0");
                    break;
                case 4:
                    sliderView.setImageUrl("https://firebasestorage.googleapis.com/v0/b/schooltyari-2057a.appspot.com/o/slide5.jpg?alt=media&token=8b0c125e-2b8b-4f93-b657-777c4999b869");
                    break;
            }
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

            sliderLayout.addSliderView(sliderView);
        }
    }



}