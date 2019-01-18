package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.danie.flexicuapplication.LogicLayer.GlobalVariables.*;

public class CreateEmployeeFinish extends AppCompatActivity implements View.OnClickListener {

    //Storage ref
    private StorageReference mStorageRef;

    TextView textViewTitle;
    TextView textViewName;
    TextView textViewYear;
    TextView textViewErhverv;
    TextView textViewPostcode;
    TextView textViewDescription;
    TextView textViewPay;
    TextView textViewDistance;
    Button buttonNextPage;

    String name;
    String year;
    String erhverv;
    int zipcode;
    String beskrivelse;
    String pay;
    int distance;
    Uri imageUri;
    Bitmap imgData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_finish);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewName = findViewById(R.id.textViewName);
        textViewYear = findViewById(R.id.textViewYear);
        textViewErhverv = findViewById(R.id.textViewErhverv);
        textViewPostcode = findViewById(R.id.textViewPostcode);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewPay = findViewById(R.id.textViewPay);
        textViewDistance = findViewById(R.id.textViewHeaderDistance);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);

        //SETUP PROGRESSBAR
        HorizontalStepView stepView = findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Navn",1);
        StepBean stepBean1 = new StepBean("Alder",1);
        StepBean stepBean2 = new StepBean("Erhverv",1);
        StepBean stepBean3 = new StepBean("Løn",1);
        StepBean stepBean4 = new StepBean("Transport",1);
        StepBean stepBean5 = new StepBean("Lokation",1);
        StepBean stepBean6 = new StepBean("Beskrivelse",1);
        StepBean stepBean7 = new StepBean("Billede",1);
        StepBean stepBean8 = new StepBean("Bekræft",0);
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


        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        year = ((GlobalVariables) this.getApplication()).getTempEmployeeYear();
        erhverv = ((GlobalVariables) this.getApplication()).getTempEmployeeProfession();
        zipcode = ((GlobalVariables) this.getApplication()).getTempEmployeeZipcode();
        beskrivelse = ((GlobalVariables) this.getApplication()).getTempEmployeeDescription();
        pay = ((GlobalVariables) this.getApplication()).getTempEmployeePay();
        distance = ((GlobalVariables) this.getApplication()).getTempEmployeeDistance();
        //imageUri = Uri.parse(((GlobalVariables) this.getApplication()).getTempEmployeeImage());
        imgData = StringToBitMap(((GlobalVariables) this.getApplication()).getTempEmployeeImage());


        textViewName.setText("Navn: " + name);
        textViewYear.setText("Fødselsår: " + year);
        textViewErhverv.setText("Erhverv: " + erhverv);
        textViewPostcode.setText("Postnummer: " + zipcode);
        textViewDescription.setText("Beskrivelse: " + beskrivelse);
        textViewPay.setText("Timeløn: " + pay + " kr/t");
        textViewDistance.setText("Afstand: " + distance + " km");
    }

    @Override
    public void onClick(View v)
    {
        if ( v == buttonNextPage )
        {
            Intent Udlej = new Intent(this, TabbedRentOut.class);
            //TODO Tilføj et skærmbillede hvor dist, altså hvor langt medarbejderen vil køre indtastes
            CrudEmployee employee = new CrudEmployee.EmployeBuilder(name).job(erhverv).pay(Double.parseDouble(pay)).zipcode(zipcode).dist(distance).available(true).builder();

            //Upload image if standard image is selected or if custom image is selected!
            if(((GlobalVariables) this.getApplication()).getTempEmployeeImage().equals("flexicu")){
                employee.setPic("flexicu"); //TODO ADD STANDARD IMG LINK
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(( getFirebaseUser().getUid()+"/Medarbejdere"));
                Gson gson = new Gson();
                String employeeJSON = gson.toJson(employee);
                System.out.println(employeeJSON);
                myRef.child(Integer.toString(employee.getID())).setValue(employeeJSON);
            }else {
                uploadImg(employee.getID(), employee);
            }

            Udlej.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Udlej.putExtra("callingActivity", "createEmployeeFinish");
            Bundle extra = new Bundle();
            extra.putString("callingActivity", "createEmployeeFinish");
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            startActivity(Udlej);
            finish();
        }
    }

    public void uploadImg(int ID, CrudEmployee employee) {

        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference databaseRef = mStorageRef.child("Users/"+getFirebaseUser().getUid()+"/Medarbejdere/"+ID+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgData.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        databaseRef.putBytes(data).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    System.out.println("Upload Error!");
                    throw task.getException();
                }
                return databaseRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downUri = task.getResult();
                    System.out.println("onComplete: Url: "+ downUri.toString());
                    employee.setPic(task.getResult().toString());
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(( getFirebaseUser().getUid()+"/Medarbejdere"));
                    Gson gson = new Gson();
                    String employeeJSON = gson.toJson(employee);
                    System.out.println(employeeJSON);
                    myRef.child(Integer.toString(employee.getID())).setValue(employeeJSON);
                }
            }
        });
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        finish();
    }
}
