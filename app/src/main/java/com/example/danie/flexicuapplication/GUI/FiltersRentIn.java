package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.R;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class FiltersRentIn extends AppCompatActivity
    {
        private Button btn;
        private EditText lowerPay;
        private EditText upperPay;
        private SeekBar seekBarDist;
        private TextView seekbarValue;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_in_filters);
           ArrayList<String> filterList = new ArrayList<String>();


        btn = findViewById(R.id.searchBtn);
        seekbarValue=findViewById(R.id.progressValue);
        lowerPay = findViewById(R.id.payLower);
        upperPay = findViewById(R.id.payUpper);
        lowerPay.setText("0");
        upperPay.setText("999");
        seekBarDist = findViewById(R.id.distSlider);
        seekBarDist.setProgress(100);
        seekbarValue.setText(seekBarDist.getProgress() + " km");
        seekBarDist.setMax(100);

        seekBarDist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarValue.setText(String.valueOf(progress)+" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

         btn.setOnClickListener((view)->{
                Intent intent = new Intent(this, TabbedRentIn.class);
                //intent.putExtra("dist", seekBarDist.getProgress());
                filterList.add(lowerPay.getText().toString());
                filterList.add(upperPay.getText().toString());
                filterList.add(String.valueOf(seekBarDist.getProgress()));
                Bundle bundle  = new Bundle();
                bundle.putStringArrayList("filterValues", filterList);
                 intent.putStringArrayListExtra("filterValues", filterList);
                if (lowerPay.getText().toString().equals("") && upperPay.getText().toString().equals(""))
                {
                    Toast.makeText(this, "Feltet må ikke være tomt", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(intent);
                    finish();
                }

            });
        }


        @Override
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            finish();
        }
    }
