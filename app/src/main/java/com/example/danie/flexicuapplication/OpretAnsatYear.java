package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpretAnsatYear extends AppCompatActivity implements View.OnClickListener {
    Button nextPage;
    TextView titel;
    TextView YearTextview;
    String name;
    String year;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_year);

        titel = findViewById(R.id.textViewTitle);
        YearTextview = findViewById(R.id.editTextDescription);

        Intent intent = getIntent();
        name = intent.getStringExtra("NameOfEmployee");
        titel.setText(" Hvilket årstal er " + name + " født?");

        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if ( v == nextPage )
        {
        year = YearTextview.getText().toString();
        Intent OpretAnsatErhverv = new Intent(this, OpretAnsatErhverv.class);
            OpretAnsatErhverv.putExtra("NameOfEmployee", name);
            OpretAnsatErhverv.putExtra("YearOfEmployee", year);
        startActivity(OpretAnsatErhverv);
        }
    }
}
