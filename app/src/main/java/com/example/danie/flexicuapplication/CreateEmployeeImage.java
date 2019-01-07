package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateEmployeeImage extends AppCompatActivity implements View.OnClickListener
    {
    Button buttonNextPage;
    TextView textViewTitle;
    TextView textViewImage;
    String name;
    String erhverv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_image);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewImage = findViewById(R.id.textViewImage);
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Tilføj et billede af " + name + " eller firmaets logo");

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            Intent CreateEmployeeFinish = new Intent(this, CreateEmployeeFinish.class);
            ((GlobalVariables) this.getApplication()).setTempEmployeeImage("URL");
            startActivity(CreateEmployeeFinish);


            }
        }
    }
