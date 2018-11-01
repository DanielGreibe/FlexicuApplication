package com.example.danie.flexicuapplication;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MineUdlejninger extends AppCompatActivity implements View.OnClickListener {
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
        LejeperiodeStart = findViewById(R.id.textView5);
        LejeperiodeSlut = findViewById(R.id.textView6);
        Lejer = findViewById(R.id.textView7);
        Erhverv = findViewById(R.id.textView8);
        Lokation = findViewById(R.id.textView9);

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
