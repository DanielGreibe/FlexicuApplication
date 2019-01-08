package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;

public class CreateEmployeeProfession extends AppCompatActivity implements View.OnClickListener
    {
    Button buttonNextPage;
    TextView textViewTitle;
    EditText editTextErhverv;
    String name;
    String year;
    String profession;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_erhverv);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextErhverv = findViewById(R.id.editTextErhverv);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);

        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText(" Tilføj erhverv til " + name);

        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            if (editTextErhverv.getText().toString().equals(""))
                {
                Toast.makeText(this, "Feltet må ikke være tomt", Toast.LENGTH_SHORT).show();
                }
            else
                {
                profession = editTextErhverv.getText().toString();
                ((GlobalVariables) this.getApplication()).setTempEmployeeProfession(profession);
                Intent createEmployeePay = new Intent(this, CreateEmployeePay.class);
                startActivity(createEmployeePay);
                }


            }
        }
    }
