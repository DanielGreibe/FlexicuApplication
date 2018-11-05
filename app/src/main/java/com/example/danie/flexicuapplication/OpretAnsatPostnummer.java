package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpretAnsatPostnummer extends AppCompatActivity implements View.OnClickListener {
    Button nextPage;
    TextView titel;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_postcode);

    titel = findViewById(R.id.textViewDescription);
    Intent intent = getIntent();
    name = intent.getStringExtra("NameOfEmployee");
    titel.setText("Indtast postnummer til " + name);

        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if ( v == nextPage )
        {

            Intent OpretAnsatBeskrivelse = new Intent(this, OpretAnsatBeskrivelse.class)
                    .putExtra("NameOfEmployee", name);
            startActivity(OpretAnsatBeskrivelse);

        }
    }
}
