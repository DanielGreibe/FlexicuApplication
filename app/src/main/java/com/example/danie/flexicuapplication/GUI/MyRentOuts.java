package com.example.danie.flexicuapplication.GUI;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.danie.flexicuapplication.R;

public class MyRentOuts extends AppCompatActivity implements View.OnClickListener {
        ConstraintLayout firstEmployee;
        TextView rentPeriodStart;
        TextView rentPeriodEnd;
        TextView tenant;
        TextView profession;
        TextView location;
        ConstraintLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_rent_outs);

        mainLayout = findViewById(R.id.MineIndlejninger_mainLayout);
        /*rentPeriodStart = findViewById(R.id.textViewRentPeriodStart);
        rentPeriodEnd = findViewById(R.id.textViewRentPeriodEnd);
        tenant = findViewById(R.id.textViewTenant);
        profession = findViewById(R.id.textViewProfession);
        location = findViewById(R.id.textViewLocation);

        firstEmployee = findViewById(R.id.firstEmployee);*/
//        firstEmployee.setOnClickListener(this);
//        mainLayout.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
       /* if ( v == firstEmployee )
        {
            rentPeriodStart.setText("Tekst ændret");
            rentPeriodEnd.setText("Tekst ændret");
            tenant.setText("Tekst ændret");
            profession.setText("Tekst ændret");
            location.setText("Tekst ændret");
        }*/
    }
}
