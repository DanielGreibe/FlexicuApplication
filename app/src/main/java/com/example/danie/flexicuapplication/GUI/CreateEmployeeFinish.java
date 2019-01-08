package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class CreateEmployeeFinish extends AppCompatActivity implements View.OnClickListener {

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
    String postcode;
    String beskrivelse;
    String pay;
    String distance;

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
        postcode = ((GlobalVariables) this.getApplication()).getTempEmployeeZipcode();
        beskrivelse = ((GlobalVariables) this.getApplication()).getTempEmployeeDescription();
        pay = ((GlobalVariables) this.getApplication()).getTempEmployeePay();
        distance = ((GlobalVariables) this.getApplication()).getTempEmployeeDistance();


        textViewName.setText("Navn: " + name);
        textViewYear.setText("Fødselsår: " + year);
        textViewErhverv.setText("Erhverv: " + erhverv);
        textViewPostcode.setText("Postnummer: " + postcode);
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
            CrudEmployee employee = new CrudEmployee.EmployeBuilder(name).job(erhverv).pic(R.drawable.download).pay(250).builder();

            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(((GlobalVariables) this.getApplication()).getFirebaseUser().getUid()+"/Medarbejdere");
            Gson gson = new Gson();
            String employeeJSON = gson.toJson(employee);
            System.out.println(employeeJSON);
            myRef.child(Integer.toString(employee.getID())).setValue(employeeJSON);
            Udlej.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Udlej);
        }
    }
}
