package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.danie.flexicuapplication.R;
import com.example.danie.flexicuapplication.profileSettings;

public class Navigation extends AppCompatActivity implements View.OnClickListener{

    Button buttonIndlej, buttonUdlej;
    ConstraintLayout settingsButton, aboutButton;
    ViewFlipper slideShowVF;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        buttonIndlej = findViewById(R.id.buttonIndlej);
        buttonUdlej = findViewById(R.id.buttonUdlej);
        settingsButton = findViewById(R.id.settingsButton);
        aboutButton = findViewById(R.id.aboutButton);
        slideShowVF = findViewById(R.id.slideShow);
        int images[] = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3,R.drawable.slide4};

        buttonIndlej.setElevation(8);
        buttonUdlej.setElevation(8);

        buttonIndlej.setOnClickListener(this);
        buttonUdlej.setOnClickListener(this);

            //Set settings button onClick
            settingsButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, profileSettings.class);
                startActivity(intent);
            });
            aboutButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
             });

        for(int image: images){
            SlideShow(image);

        }

    }

    @Override
    public void onClick(View v)
        {
            if ( v == buttonIndlej) {
                    //Opens the Indlej page
                    Intent Indlej = new Intent(this, TabbedRentIn.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_out_right,R.anim.anim_slide_in_right).toBundle();
                    startActivity(Indlej, bndlanimation);
                }
            else if (v == buttonUdlej) {
                    //Opens the Udlej page
                    Intent Udlej = new Intent(this, TabbedRentOut.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                    Udlej.putExtra("callingActivity", "navigation");
                    startActivity(Udlej, bndlanimation);
                }
        }
        public void SlideShow(int image){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(image);
            slideShowVF.addView(imageView);
            slideShowVF.setFlipInterval(4000);
            slideShowVF.setAutoStart(true);
            slideShowVF.setInAnimation(this,  android.R.anim.slide_in_left);
            slideShowVF.setOutAnimation(this, android.R.anim.slide_out_right);

        }
    }
