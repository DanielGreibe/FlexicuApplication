package com.example.danie.flexicuapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

public class Udlej extends AppCompatActivity
    {

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udlej);
        LinearLayout myContainer = findViewById(R.id.scrollLayoutUdlej);
        LinearLayout myTestContainer = findViewById(R.id.scrollLayoutUdlej);

        Button myButton = new Button(this);
       // myContainer.addView(myTestContainer);
        myTestContainer.addView(myButton);
        }
    }
