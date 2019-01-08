package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEmployeeYear extends AppCompatActivity implements View.OnClickListener
    {
    Button buttonNextPage;
    TextView textViewTitle;
    Spinner spinnerYear;
    String name;
    String year;


    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_year);

        textViewTitle = findViewById(R.id.textViewTitle);
        spinnerYear = findViewById(R.id.spinnerYear);

        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText(" Hvilket årstal er " + name + " født?");

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);


        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--)
            {
            years.add(Integer.toString(i));
            }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinnerYear.setAdapter(adapter);

        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            year = spinnerYear.getSelectedItem().toString();
            ((GlobalVariables) this.getApplication()).setTempEmployeeYear(year);
            Intent createEmployeeProfession = new Intent(this, CreateEmployeeProfession.class);
            createEmployeeProfession.putExtra("NameOfEmployee", name);
            createEmployeeProfession.putExtra("YearOfEmployee", year);
            startActivity(createEmployeeProfession);

            }
        }
    }
