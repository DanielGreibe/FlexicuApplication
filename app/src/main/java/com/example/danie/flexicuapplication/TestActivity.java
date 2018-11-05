package com.example.danie.flexicuapplication;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.graphics.Color.rgb;

public class TestActivity extends AppCompatActivity
    {
        ConstraintLayout mainLayout;
        TextView mainText;
        ScrollView scrollView;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mainLayout = findViewById(R.id.mainLayoutTestPage);
        scrollView = findViewById(R.id.ScrollView);
        mainText = findViewById(R.id.textView2);

        //Orange in RGB as int.
        int orange = rgb(255,165,0);


        ConstraintLayout generatedLayout = new ConstraintLayout(this);
        generatedLayout.setMaxWidth(400);
        generatedLayout.setMaxHeight(200);
        generatedLayout.setBackgroundColor(orange);

        Button myButton = new Button(this);
        myButton.setText("TestButton");


        mainLayout.addView(myButton);
        scrollView.addView(generatedLayout);



        }
    }
