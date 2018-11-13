package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpretAnsat extends AppCompatActivity implements View.OnClickListener {
        Button nextPage;
        TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);


        name = findViewById(R.id.editTextDescription);
        nextPage = findViewById(R.id.buttonNextPage);

        nextPage.setOnClickListener(this);


        }

    @Override
    public void onClick(View v) {
        if ( v == nextPage )
        {

            String navn = name.getText().toString();
            Intent OpretAnsatYear = new Intent(this, OpretAnsatYear.class)
                    .putExtra("NameOfEmployee", navn);
            startActivity(OpretAnsatYear);
        }
    }
}
