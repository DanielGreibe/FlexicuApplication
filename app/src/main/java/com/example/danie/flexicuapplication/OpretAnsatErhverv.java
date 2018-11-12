package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OpretAnsatErhverv extends AppCompatActivity implements View.OnClickListener {
    Button nextPage;
    TextView titel;
    String name;
    String year;
    String erhverv;

    EditText editTextErhverv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_erhverv);

        titel = findViewById(R.id.textViewDescription);
        Intent intent = getIntent();
        name = intent.getStringExtra("NameOfEmployee");
        year = intent.getStringExtra("YearOfEmployee");
        titel.setText(" Tilføj erhverv til " + name);


        editTextErhverv = findViewById(R.id.editTextDescription);

        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if ( v == nextPage )
        {
            erhverv = editTextErhverv.getText().toString();
            Intent OpretAnsatPostnummer = new Intent(this, OpretAnsatPostnummer.class);
            OpretAnsatPostnummer.putExtra("NameOfEmployee", name);
            OpretAnsatPostnummer.putExtra("YearOfEmployee", year);
            OpretAnsatPostnummer.putExtra("ErhvervOfEmployee", erhverv);

            startActivity(OpretAnsatPostnummer);

        }
    }
}
