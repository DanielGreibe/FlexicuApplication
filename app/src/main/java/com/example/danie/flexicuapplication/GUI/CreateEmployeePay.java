package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;

public class CreateEmployeePay extends AppCompatActivity implements View.OnClickListener
    {

    Button buttonNextPage;
    TextView textViewTitle;
    EditText editTextPay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_pay);

        buttonNextPage = findViewById(R.id.buttonNextPage);
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextPay = findViewById(R.id.editTextDistance);

        buttonNextPage.setOnClickListener(this);
        String name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Indtast timeløn til " + name);
        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            String pay = editTextPay.getText().toString();
            ((GlobalVariables) this.getApplication()).setTempEmployeePay(pay);
            Intent createEmployeeDistance = new Intent(this, CreateEmployeeDistance.class);
            startActivity(createEmployeeDistance);
            }
        }
    }
