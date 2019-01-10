package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.danie.flexicuapplication.R;

public class Navigation extends AppCompatActivity implements View.OnClickListener
    {

    Button buttonMineIndlejninger;
    Button buttonMineUdlejninger;
    Button buttonIndlej;
    Button buttonUdlej;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        buttonMineIndlejninger = findViewById(R.id.buttonMineIndlejninger);
        buttonMineUdlejninger = findViewById(R.id.buttonMineUdlejninger);
        buttonIndlej = findViewById(R.id.buttonIndlej);
        buttonUdlej = findViewById(R.id.buttonUdlej);


        buttonMineIndlejninger.setOnClickListener(this);
        buttonMineUdlejninger.setOnClickListener(this);
        buttonIndlej.setOnClickListener(this);
        buttonUdlej.setOnClickListener(this);
        }

    @Override
    public void onClick(View v)
        {
            if ( v == buttonMineUdlejninger)
                {
                    //Opens the MineUdlejninger page
                    Intent MineUdlejninger = new Intent(this, MyRentOuts.class);
                    startActivity(MineUdlejninger);
                }
            else if ( v == buttonMineIndlejninger)
                {
                    //Opens the MineIndlejninger page
                    Intent MineIndlejninger = new Intent(this, MyRentIns.class);
                    startActivity(MineIndlejninger);
                }
            else if ( v == buttonIndlej)
                {
                    //Opens the Indlej page
                    Intent Indlej = new Intent(this, RentIn.class);
                    startActivity(Indlej);
                }
            else if (v == buttonUdlej)
                {
                    //Opens the Udlej page
                    Intent Udlej = new Intent(this, RentOut.class);
                    startActivity(Udlej);
                }
        }
    }
