package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Navigation extends AppCompatActivity implements View.OnClickListener
    {

    Button MineIndlejninger;
    Button MineUdlejninger;
    Button Indlej;
    Button Udlej;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        /* Hides the Title Bar
        SOURCE = https://www.javatpoint.com/android-hide-title-bar-example
         */
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_navigation);

        MineIndlejninger = findViewById(R.id.MineIndlejninger);
        MineUdlejninger = findViewById(R.id.MineUdlejninger);
        Indlej = findViewById(R.id.Indlej);
        Udlej = findViewById(R.id.Udlej);

        /*
        MineIndlejninger.setText("Mine Indlejninger");
        MineUdlejninger.setText("Mine Udlejninger");
        Indlej.setText("Indlej");
        Udlej.setText("Udlej");
        */


        MineIndlejninger.setOnClickListener(this);
        MineUdlejninger.setOnClickListener(this);
        Indlej.setOnClickListener(this);
        Udlej.setOnClickListener(this);
        }

    @Override
    public void onClick(View v)
        {
            if ( v == MineUdlejninger)
                {
                    //Opens the MineUdlejninger page
                    Intent MineUdlejninger = new Intent(this, MineUdlejninger.class);
                    startActivity(MineUdlejninger);
                }
            else if ( v == MineIndlejninger)
                {
                    //Opens the MineIndlejninger page
                    Intent MineIndlejninger = new Intent(this, MineIndlejninger.class);
                    startActivity(MineIndlejninger);
                }
            else if ( v == Indlej)
                {
                    //Opens the Indlej page
                    Intent Indlej = new Intent(this, Indlej.class);
                    startActivity(Indlej);
                }
            else if (v == Udlej)
                {
                    //Opens the Udlej page
                    Intent Udlej = new Intent(this, Udlej.class);
                    startActivity(Udlej);
                }
        }
    }
