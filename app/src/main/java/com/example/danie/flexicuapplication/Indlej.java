package com.example.danie.flexicuapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Indlej extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indlej);

        LinearLayout scroller = findViewById(R.id.linearLayout);
        ImageView filterMenu = findViewById(R.id.filterMenu);

        filterMenu.setOnClickListener(this);


        }
    public void createNew(){
        LinearLayout LL = new LinearLayout(this);
      

    }

    @Override
    public void onClick(View v) {

    }
}
