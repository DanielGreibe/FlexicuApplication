package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateEmployee extends AppCompatActivity implements View.OnClickListener {
        Button buttonNextPage;
        TextView editTextName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);
        editTextName = findViewById(R.id.editTextName);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        if ( v == buttonNextPage )
        {

            String name = editTextName.getText().toString();
            Intent OpretAnsatYear = new Intent(this, CreateEmployeeYear.class);
            OpretAnsatYear.putExtra("NameOfEmployee", name);
            startActivity(OpretAnsatYear);
        }
    }
}
