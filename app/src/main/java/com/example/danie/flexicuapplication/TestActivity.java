package com.example.danie.flexicuapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity
    {
        ConstraintLayout mainLayout;
        TextView mainText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mainLayout = findViewById(R.id.mainLayoutTestPage);
        mainText = findViewById(R.id.textView2);

        Button myButton = new Button(this);


        mainLayout.addView(myButton);




        }
    }
