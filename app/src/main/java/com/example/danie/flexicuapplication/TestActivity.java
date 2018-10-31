package com.example.danie.flexicuapplication;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import static android.graphics.Color.GREEN;

public class TestActivity extends AppCompatActivity
    {
        ConstraintLayout mainLayout;
        TextView mainText;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mainLayout = findViewById(R.id.mainLayoutTestPage);

        mainText = findViewById(R.id.textView2);


        ConstraintLayout generatedLayout = new ConstraintLayout(this);
        generatedLayout.setMaxWidth(400);
        generatedLayout.setMaxHeight(200);
        generatedLayout.setBackgroundColor(GREEN);

        Button myButton = new Button(this);
        myButton.setText("TestButton");


        mainLayout.addView(generatedLayout);

        mainLayout.addView(myButton);




        }
    }
