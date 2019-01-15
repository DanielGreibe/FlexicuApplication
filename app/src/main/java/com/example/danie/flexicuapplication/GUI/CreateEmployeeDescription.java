package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;

public class CreateEmployeeDescription extends AppCompatActivity implements View.OnClickListener
    {
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
        editTextDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        textViewTitle = findViewById(R.id.textViewTitle);
        Intent intent = getIntent();
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Tilføj en beskrivelse til " + name);

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);


        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            beskrivelse = editTextDescription.getText().toString();
            ((GlobalVariables) this.getApplication()).setTempEmployeeDescription(beskrivelse);
            Intent createEmployeeImage = new Intent(this, CreateEmployeeImage.class);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                startActivity(createEmployeeImage, bndlanimation);

            }
        }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            finish();
        }
    }
