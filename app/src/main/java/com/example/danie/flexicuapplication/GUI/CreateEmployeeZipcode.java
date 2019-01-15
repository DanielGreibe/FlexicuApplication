package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;

public class CreateEmployeeZipcode extends AppCompatActivity implements View.OnClickListener
    {
    Button nextPage;
    TextView titel;
    String name;
    int zipcode;
    EditText editTextZipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_zipcode);
        editTextZipcode = findViewById(R.id.editTextPostcode);
        titel = findViewById(R.id.textViewTitle);
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        titel.setText("Indtast postnummer til " + name);
        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);
        }

    @Override
    public void onClick(View v)
        {
        if (v == nextPage)
            {
            zipcode = Integer.parseInt(editTextZipcode.getText().toString());
            ((GlobalVariables) this.getApplication()).setTempEmployeeZipcode(zipcode);
            Intent createEmployeeDescription = new Intent(this, CreateEmployeeDescription.class);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                startActivity(createEmployeeDescription, bndlanimation);

            }
        }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            finish();
        }
    }
