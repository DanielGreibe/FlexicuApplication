package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeProfession extends AppCompatActivity implements View.OnClickListener
    {
    //Declare Global Variables
    Button buttonNextPage;
    TextView textViewTitle;
    Spinner editTextErhverv;
    String name;
    String year;
    String profession;
    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_erhverv);

        //Create views, set initial text and add items to spinner
        textViewTitle = findViewById(R.id.textViewTitle);
        editTextErhverv = findViewById(R.id.spinnerJob);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText(" Tilføj erhverv til " + name);
        buttonNextPage.setOnClickListener(this);
        items.add("Tømrer");
        items.add("VVS");
        items.add("Elektrikker");
        items.add("Murer");
        items.add("Anlægsgartner");
        items.add("Maler");
        items.add("Arbejdsmand");
        items.add("Smed");
        items.add("Chauffør - under 3,5T");
        items.add("Chauffør - over 3,5T");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            editTextErhverv.setAdapter(adapter);
            //SETUP PROGRESSBAR
            HorizontalStepView stepView = findViewById(R.id.step_view);
            List<StepBean> stepsBeanList = new ArrayList<>();
            StepBean stepBean0 = new StepBean("Navn",1);
            StepBean stepBean1 = new StepBean("Alder",1);
            StepBean stepBean2 = new StepBean("Erhverv",0);
            StepBean stepBean3 = new StepBean("Løn",-1);
            StepBean stepBean4 = new StepBean("Transport",-1);
            StepBean stepBean5 = new StepBean("Lokation",-1);
            StepBean stepBean6 = new StepBean("Beskrivelse",-1);
            StepBean stepBean7 = new StepBean("Billede",-1);
            StepBean stepBean8 = new StepBean("Bekræft",-1);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
            stepsBeanList.add(stepBean3);
            stepsBeanList.add(stepBean4);
            stepsBeanList.add(stepBean5);
            stepsBeanList.add(stepBean6);
            stepsBeanList.add(stepBean7);
            stepsBeanList.add(stepBean8);
            stepView
                    .setStepViewTexts(stepsBeanList)//总步骤
                    .setTextSize(8)//set textSize
                    .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.FlexBlue))
                    .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.white))
                    .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.FlexBlue))
                    .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))
                    .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.blue_check))
                    .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_custom))
                    .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.trans_focus));



        }

    @Override
    public void onClick(View v)
        {
        if (v == buttonNextPage)
            {
            //If nothing is chosen write an error message, otherwise put it in GlobalVariables and make a slide animation to the new activity createEmployeePay
            if (editTextErhverv.getSelectedItem().toString().equals(""))
                {
                Toast.makeText(this, "Feltet må ikke være tomt", Toast.LENGTH_SHORT).show();
                }
            else
                {
                profession = editTextErhverv.getSelectedItem().toString();
                ((GlobalVariables) this.getApplication()).setTempEmployeeProfession(profession);
                Intent createEmployeePay = new Intent(this, CreateEmployeePay.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                    startActivity(createEmployeePay, bndlanimation);
                }


            }
        }
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            finish();
        }
    }
