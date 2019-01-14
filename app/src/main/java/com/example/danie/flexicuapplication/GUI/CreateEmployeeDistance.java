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

public class CreateEmployeeDistance extends AppCompatActivity implements View.OnClickListener
    {

    Button buttonNextPage;
    TextView textViewTitle;
    EditText editTextDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_distance);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextDistance = findViewById(R.id.editTextDistance);

        buttonNextPage.setOnClickListener(this);
        String name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Indtast afstand som " + name + " er villig at transportere");
        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            if (editTextDistance.getText().toString().equals(""))
                {
                Toast.makeText(this, "Feltet må ikke være tomt", Toast.LENGTH_SHORT).show();
                }
            else
                {
                int distance = Integer.parseInt(editTextDistance.getText().toString());
                ((GlobalVariables) this.getApplication()).setTempEmployeeDistance(distance);
                Intent createEmployeeZipcode = new Intent(this, CreateEmployeeZipcode.class);
                startActivity(createEmployeeZipcode);
                }
            }
        }
    }
