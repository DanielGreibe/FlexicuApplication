package com.example.danie.flexicuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpretAnsatBillede extends AppCompatActivity implements View.OnClickListener {
    Button nextPage;
    TextView titel;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_image);

        titel = findViewById(R.id.textViewDescription);
        Intent intent = getIntent();
        name = intent.getStringExtra("NameOfEmployee");
        titel.setText("Tilføj et billede af " + name + " eller firmaets logo");

        nextPage = findViewById(R.id.buttonNextPage);
        nextPage.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if ( v == nextPage )
        {

            Intent Navigation = new Intent(this, Navigation.class);
            startActivity(Navigation);

        }
    }
}
