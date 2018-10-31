package com.example.danie.flexicuapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MineIndlejninger extends AppCompatActivity {


    ConstraintLayout mainLayout;
    TextView mainText;


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_indlejninger);

        LinearLayout scroller = findViewById(R.id.linearlayout1);

        mainLayout = findViewById(R.id.MineIndlejninger_mainLayout);
        mainText = findViewById(R.id.textView2);
        ConstraintLayout firstEmployee = findViewById(R.id.firstEmployee);
        Button myButton = new Button(this);
        Button myButton2 = new Button(this);


        ConstraintLayout myConstraint = new ConstraintLayout(this);
        myConstraint.setBackgroundColor(R.color.FlexBlue);
        myConstraint.setMaxHeight(50);
        myConstraint.setMaxWidth(400);
        //Image add to constraint
        ImageView profilepic = new ImageView(this);
        myConstraint.setBackgroundResource(R.drawable.layout_background_round_corners);
        //profilepic.setBackgroundResource(R.drawable.download);
        //myConstraint.addView(profilepic);

        scroller.addView(myButton);
        mainLayout.addView(myConstraint);
        //scroller.addView(myConstraint);
        scroller.addView(myButton2);




        //mainLayout.addView(myButton);
        //mainLayout.addView(myConstraint);

       /* ConstraintLayout testperson = findViewById(R.id.firstEmployee);

        ScrollView personScroller = findViewById(R.id.scrollView);
        ConstraintLayout addTest = new ConstraintLayout(this);
        addTest.setId(View.generateViewId());
        setContentView(addTest);
        addTest = testperson;
       // addTest.
        //personScroller.addView(addTest);*/
        }
    }
