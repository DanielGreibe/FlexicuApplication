package com.example.danie.flexicuapplication.GUI;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeDescription extends AppCompatActivity implements View.OnClickListener{
    Button buttonNextPage;
    TextView textViewTitle, springOver;
    EditText editTextDescription;
    String name;
    String beskrivelse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_description);

        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTitle = findViewById(R.id.textViewTitle);
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Tilføj en beskrivelse til " + name);
        springOver = findViewById(R.id.springOverTV);
        springOver.setOnClickListener(this);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        HorizontalScrollView scroller = findViewById(R.id.horizontalScrollView2);

        //SETUP PROGRESSBAR
        HorizontalStepView stepView = findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Navn",1);
        StepBean stepBean1 = new StepBean("Alder",1);
        StepBean stepBean2 = new StepBean("Erhverv",1);
        StepBean stepBean3 = new StepBean("Løn",1);
        StepBean stepBean4 = new StepBean("Transport",1);
        StepBean stepBean5 = new StepBean("Lokation",1);
        StepBean stepBean6 = new StepBean("Beskrivelse",0);
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

            scroller.post(new Runnable() {
                @Override
                public void run() {
                    scroller.scrollTo(500, 0);
                }
            });
        }

    @Override
    public void onClick(View v) {
        if (v == buttonNextPage){
            //If the description isn't empty save the description in GlobalVariables and open the next activity createEmployeeImage
            //otherwise write an error message in a toast
            if(editTextDescription.length() > 0){
                beskrivelse = editTextDescription.getText().toString();
                ((GlobalVariables) this.getApplication()).setTempEmployeeDescription(beskrivelse);
                Intent createEmployeeImage = new Intent(this, CreateEmployeeImage.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
                startActivity(createEmployeeImage, bndlanimation);
            } else {
                Toast.makeText(this, "Tilføj en beskrivelse, eller spring over", Toast.LENGTH_SHORT).show();
            }
            //If you clicked "Spring over" and the description is empty set the description in Global Variables
            //and open the next activity createEmployeeImage
        } else if(v == springOver && (editTextDescription.getText().toString().length() == 0)){
            beskrivelse = "";
            ((GlobalVariables) this.getApplication()).setTempEmployeeDescription(beskrivelse);
            Intent createEmployeeImage = new Intent(this, CreateEmployeeImage.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
            startActivity(createEmployeeImage, bndlanimation);
        }
    }   //Display a slide animation and close the current activity.
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            finish();
        }
    }
