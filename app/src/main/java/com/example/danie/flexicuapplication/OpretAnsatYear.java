package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OpretAnsatYear extends AppCompatActivity implements View.OnClickListener {
    Button nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_year);

        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if ( v == nextPage )
        {
            Intent OpretAnsatErhverv = new Intent(this, OpretAnsatErhverv.class);
            startActivity(OpretAnsatErhverv);
        }
    }
}
