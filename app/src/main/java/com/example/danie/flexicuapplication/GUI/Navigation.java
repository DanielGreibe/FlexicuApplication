package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
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
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                    startActivity(MineUdlejninger, bndlanimation);
                }
            else if ( v == buttonMineIndlejninger)
                {
                    //Opens the MineIndlejninger page
                    Intent MineIndlejninger = new Intent(this, MyRentIns.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                    startActivity(MineIndlejninger, bndlanimation);
                }
            else if ( v == buttonIndlej)
                {
                    //Opens the Indlej page
                    Intent Indlej = new Intent(this, RentIn.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                    startActivity(Indlej, bndlanimation);
                }
            else if (v == buttonUdlej)
                {
                    //Opens the Udlej page
                    Intent Udlej = new Intent(this, RentOut.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                    startActivity(Udlej, bndlanimation);
                    Udlej.putExtra("callingActivity", "navigation");
                    startActivity(Udlej);
                }
        }
    }
