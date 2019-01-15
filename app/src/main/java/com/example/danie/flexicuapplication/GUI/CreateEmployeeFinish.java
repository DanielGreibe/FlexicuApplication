package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
        textViewDistance = findViewById(R.id.textViewDistance);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);


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
            Intent Udlej = new Intent(this, RentOut.class);
            //TODO Tilføj et skærmbillede hvor dist, altså hvor langt medarbejderen vil køre indtastes
            CrudEmployee employee = new CrudEmployee.EmployeBuilder(name).job(erhverv).pay(250).zipcode(zipcode).dist(distance).builder();

            //Upload image
            uploadImg(employee.getID(), employee);
            Udlej.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Udlej.putExtra("callingActivity", "createEmployeeFinish");
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            Intent intent = new Intent(this, RentOut.class);
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
