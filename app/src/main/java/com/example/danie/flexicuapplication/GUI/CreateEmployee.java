package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;

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
            ((GlobalVariables) this.getApplication()).setTempEmployeeName(name);
            Intent createEmployeeYear = new Intent(this, CreateEmployeeYear.class);
            startActivity(createEmployeeYear);

        }
    }
}
