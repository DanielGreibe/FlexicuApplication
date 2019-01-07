package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.R;

public class CreateEmployeeYear extends AppCompatActivity implements View.OnClickListener {
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

        Intent intent = getIntent();
        name = intent.getStringExtra("NameOfEmployee");
        textViewTitle.setText(" Hvilket årstal er " + name + " født?");

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if ( v == buttonNextPage)
        {
        year = editTextYear.getText().toString();
        Intent OpretAnsatErhverv = new Intent(this, CreateEmployeeProfession.class);
            OpretAnsatErhverv.putExtra("NameOfEmployee", name);
            OpretAnsatErhverv.putExtra("YearOfEmployee", year);
        startActivity(OpretAnsatErhverv);
        }
    }
}
