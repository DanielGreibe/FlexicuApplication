package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.R;

import java.util.ArrayList;
import java.util.List;

public class FiltersRentIn extends AppCompatActivity
    {
        private Button btn;
        private EditText lowerPay;
        private EditText upperPay;
        private SeekBar seekBarDist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_in_filters);
            List<CrudEmployee> people = new ArrayList<CrudEmployee>();
            people.add(new CrudEmployee.EmployeBuilder("john").pay(201).job("mur").dist(70).builder());
            //people.add(new CrudEmployee.EmployeBuilder("fut").pay(199).builder());
            people.add(new CrudEmployee.EmployeBuilder("bob").pay(202).job("elektro").dist(20).builder());
            people.add(new CrudEmployee.EmployeBuilder("julemanden").pay(150).job("væg").dist(10).builder());

        btn = findViewById(R.id.searchBtn);
        lowerPay = findViewById(R.id.payLower);
        upperPay = findViewById(R.id.payUpper);
        seekBarDist = findViewById(R.id.distSlider);
        seekBarDist.setProgress(0);
        seekBarDist.incrementProgressBy(10);
        seekBarDist.setMax(100);


         btn.setOnClickListener((view)->{
                Intent intent = new Intent(this, RentIn.class);
                intent.putExtra("dist", seekBarDist.getProgress());
                intent.putExtra("pay",  upperPay.getText().toString());
                finish();
                startActivity(intent);


            });
        }



    }
