package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.danie.flexicuapplication.LogicLayer.CriteriaInterface;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPay;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaProfession;
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


        btn = findViewById(R.id.searchBtn);
        lowerPay = findViewById(R.id.payLower);
        upperPay = findViewById(R.id.payUpper);
        seekBarDist = findViewById(R.id.distSlider);
        seekBarDist.setProgress(0);
        seekBarDist.incrementProgressBy(10);
        seekBarDist.setMax(100);


         btn.setOnClickListener((view)->{
                Intent intent = new Intent(this, RentIn.class);
                //intent.putExtra("dist", seekBarDist.getProgress());
                intent.putExtra("pay", lowerPay.getText().toString());
                if (lowerPay.getText().toString().equals("") || upperPay.getText().toString().equals(""))
                {
                    Toast.makeText(this, "Feltet må ikke være tomt", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(intent);
                    finish();
                }


            });
        }



    }
