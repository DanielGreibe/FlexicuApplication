package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OpretAnsatBeskrivelse extends AppCompatActivity implements View.OnClickListener {
    Button buttonNextPage;
    TextView textViewTitle;
    EditText editTextDescription;

    String name;
    String year;
    String erhverv;
    String postcode;
    String beskrivelse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_description);

        editTextDescription = findViewById(R.id.editTextDescription);

        textViewTitle = findViewById(R.id.textViewTitle);
        Intent intent = getIntent();
        name = intent.getStringExtra("NameOfEmployee");
        year = intent.getStringExtra("YearOfEmployee");
        erhverv = intent.getStringExtra("ErhvervOfEmployee");
        postcode = intent.getStringExtra("PostnummerOfEmployee");
        textViewTitle.setText("Tilføj en beskrivelse til " + name);

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if ( v == buttonNextPage)
        {
            beskrivelse = editTextDescription.getText().toString();
            Intent OpretAnsatBillede = new Intent(this, OpretAnsatBillede.class);
            OpretAnsatBillede.putExtra("NameOfEmployee", name);
            OpretAnsatBillede.putExtra("YearOfEmployee", year);
            OpretAnsatBillede.putExtra("ErhvervOfEmployee", erhverv);
            OpretAnsatBillede.putExtra("PostnummerOfEmployee", postcode);
            OpretAnsatBillede.putExtra("DescriptionOfEmployee", beskrivelse);
            startActivity(OpretAnsatBillede);

        }
    }
}
