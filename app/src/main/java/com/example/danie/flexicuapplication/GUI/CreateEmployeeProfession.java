package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.R;

public class CreateEmployeeProfession extends AppCompatActivity implements View.OnClickListener {
    Button buttonNextPage;
    TextView textViewTitle;
    EditText editTextErhverv;
    String name;
    String year;
    String erhverv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_erhverv);

        textViewTitle = findViewById(R.id.textViewTitle);
        editTextErhverv = findViewById(R.id.editTextErhverv);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("NameOfEmployee");
        year = intent.getStringExtra("YearOfEmployee");
        textViewTitle.setText(" Tilføj erhverv til " + name);

    }

    @Override
    public void onClick(View v) {
        if ( v == buttonNextPage)
        {
            erhverv = editTextErhverv.getText().toString();
            Intent OpretAnsatPostnummer = new Intent(this, CreateEmployeeZipcode.class);
            OpretAnsatPostnummer.putExtra("NameOfEmployee", name);
            OpretAnsatPostnummer.putExtra("YearOfEmployee", year);
            OpretAnsatPostnummer.putExtra("ErhvervOfEmployee", erhverv);
            startActivity(OpretAnsatPostnummer);

        }
    }
}
