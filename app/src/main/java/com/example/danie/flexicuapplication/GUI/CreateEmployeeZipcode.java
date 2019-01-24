package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;

import java.util.ArrayList;
import java.util.List;

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
        titel.setText("Indtast " + name + "'s" + " postnummer");
        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);


        //SETUP PROGRESSBAR
        HorizontalStepView stepView = findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Navn",1);
        StepBean stepBean1 = new StepBean("Alder",1);
        StepBean stepBean2 = new StepBean("Erhverv",1);
        StepBean stepBean3 = new StepBean("Løn",1);
        StepBean stepBean4 = new StepBean("Transport",1);
        StepBean stepBean5 = new StepBean("Lokation",0);
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

            HorizontalScrollView scroller = findViewById(R.id.horizontalScrollView2);
            scroller.post(new Runnable() {
                @Override
                public void run() {
                    scroller.scrollTo(500, 0);
                }
            });
        }

    @Override
    public void onClick(View v)
        {
        if (v == nextPage)
            {
            if (editTextZipcode.getText().toString().length() != 4)
                {
                Toast.makeText(this, "Postnummeret skal være 4 cifre", Toast.LENGTH_SHORT).show();
                }
            else
                {
                zipcode = Integer.parseInt(editTextZipcode.getText().toString());
                ((GlobalVariables) this.getApplication()).setTempEmployeeZipcode(zipcode);
                Intent createEmployeeDescription = new Intent(this, CreateEmployeeDescription.class);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
                startActivity(createEmployeeDescription, bndlanimation);
                }

            }
        }
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            finish();
        }
    }
