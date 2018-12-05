package com.example.danie.flexicuapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RentOut extends AppCompatActivity
    {
        private Context mContext;
        private ConstraintLayout constLayout;
        private ConstraintLayout constCardLayout;
        int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out);
        mContext = getApplicationContext();
        LinearLayout myContainer = findViewById(R.id.scrollLayoutUdlej);
        constLayout = findViewById(R.id.opretMedarbejder);

        constLayout.setOnClickListener((view) ->{
        Intent opretAnsat = new Intent(this, CreateEmployee.class);
        startActivity(opretAnsat);
            CardView card = new CardView(mContext);
            card.setId(id);
            id++;
            if(card.getId() == 1){
                card.setOnClickListener((info) -> {
                    TextView txtLøn = findViewById(R.id.løntxt);
                    txtLøn.setText("200 kr");
                });
            }else if (card.getId() == 2){
                card.setOnClickListener((info) -> {
                            TextView txtLøn = findViewById(R.id.løntxt);
                            txtLøn.setText("0 kr");
            });

            }

            /*
            card.setLayoutParams(getLinearLayout());
            card.setRadius(15);
            card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));

            ImageView imgView = new ImageView(this);
            imgView.setImageResource(R.drawable.download);
            imgView.setAdjustViewBounds(true);


            card.addView(addTextView(new TextView(mContext)));
            card.addView(imgView);
            myContainer.addView(card);
            */

        });

        }

        public void setinfo(CardView CV){

        }

        public TextView addTextView(TextView TV){
            TV.setLayoutParams(getLinearLayout());
            TV.setText("Thomas - snedker");
            TV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            TV.setTextColor(Color.BLACK);
            TV.setPadding(125,0,0,0);

            return TV;
        }

        public ViewGroup.LayoutParams getLinearLayout(){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    100
            );
            params.setMargins(0,15,75,0);
            return  params;
        }

    }
