package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateEmployeeYear extends AppCompatActivity implements View.OnClickListener
    {
    Button buttonNextPage;
    TextView textViewTitle;
    EditText editTextYear;
    String name;
    String year;


    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_year);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextYear = findViewById(R.id.editTextYear);

        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText(" Hvilket årstal er " + name + " født?");

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);


        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            year = editTextYear.getText().toString();
            ((GlobalVariables) this.getApplication()).setTempEmployeeYear(year);
            Intent createEmployeeProfession = new Intent(this, CreateEmployeeProfession.class);
            createEmployeeProfession.putExtra("NameOfEmployee", name);
            createEmployeeProfession.putExtra("YearOfEmployee", year);
            startActivity(createEmployeeProfession);

            }
        }
    }
