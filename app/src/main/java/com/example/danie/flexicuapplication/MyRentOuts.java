package com.example.danie.flexicuapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MyRentOuts extends AppCompatActivity implements View.OnClickListener {
        ConstraintLayout firstEmployee;
        TextView LejeperiodeStart;
        TextView LejeperiodeSlut;
        TextView Lejer;
        TextView Erhverv;
        TextView Lokation;
        ConstraintLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mine_udlejninger);

        mainLayout = findViewById(R.id.MineIndlejninger_mainLayout);
        LejeperiodeStart = findViewById(R.id.textViewLejeperiodeStart);
        LejeperiodeSlut = findViewById(R.id.textViewLejeperiodeSlut);
        Lejer = findViewById(R.id.textViewLejer);
        Erhverv = findViewById(R.id.textViewErhverv);
        Lokation = findViewById(R.id.textViewLokation);

        firstEmployee = findViewById(R.id.firstEmployee);
        firstEmployee.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        if ( v == firstEmployee )
        {
            LejeperiodeStart.setText("Tekst ændret");
            LejeperiodeSlut.setText("Tekst ændret");
            Lejer.setText("Tekst ændret");
            Erhverv.setText("Tekst ændret");
            Lokation.setText("Tekst ændret");
        }
    }
}
