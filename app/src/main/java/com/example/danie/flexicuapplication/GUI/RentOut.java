package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.CriteriaDemo;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.makeramen.roundedimageview.RoundedImageView;

public class RentOut extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ConstraintLayout constLayout;
    private ConstraintLayout constCardLayout;
    TextView textViewPay, textViewProfession, textViewZipcode, textViewDistance;
    private ConstraintLayout udlejBtn;
    int id = 1;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out);
        textViewProfession = findViewById(R.id.erhvevtxt);
        textViewPay = findViewById(R.id.løntxt);
        textViewZipcode = findViewById(R.id.postnummertxt);
        textViewDistance = findViewById(R.id.radiustxt);
        LinearLayout myContainer = findViewById(R.id.scrollLayoutUdlej);
        constLayout = findViewById(R.id.opretMedarbejder);
        udlejBtn = findViewById(R.id.UdlejBtn);
        mContext = getApplicationContext();
        CriteriaDemo demo = new CriteriaDemo();
        demo.start();
        constLayout.setOnClickListener((view) ->{
            Intent opretAnsat = new Intent(this, CreateEmployee.class);
            startActivity(opretAnsat);

        });

        //Load workers from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere");

        //Check for existing ID.
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()){

                    //Parse JSON
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entry.getValue().toString());
                    JsonObject obj = element.getAsJsonObject();


                    System.out.println("ID IS "+obj.get("ID"));

                    // 2. JSON to Java object, read it from a Json String.
                    CardView cv = new CardView(getApplicationContext());
                    cv.setId(Integer.parseInt(obj.get("ID").toString()));
                    LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
                    size.setMargins(0, 5, 0, 5);
                    cv.setLayoutParams(size);
                    cv.setRadius(15);
                    ConstraintLayout cl = new ConstraintLayout(getApplicationContext());
                    cv.addView(cl);
                    cv.setOnClickListener((view) ->
                    {
                        //Opdaterer TextViews med information fra brugeren
                        UpdateTextviews(obj);

                        for(int i = 0; i <  myContainer.getChildCount(); i++){
                            myContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
                        }

                        view.setBackgroundColor(Color.rgb(0,153,203));
                        udlejBtn.setBackgroundResource(R.drawable.layout_background_round_corners_blue);

                    });
                    //Add pic
                    RoundedImageView IVProfilePic = new RoundedImageView(getApplicationContext());
                    //@SuppressLint("ResourceType") LinearLayout IVProfilePic = (LinearLayout) findViewById(R.drawable.circle);

                    IVProfilePic.setId(id++);
                    //IVProfilePic.setImageResource(R.drawable.img);
                //IVProfilePic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                IVProfilePic.setScaleType(ImageView.ScaleType.FIT_XY);
                IVProfilePic.setCornerRadius((float) 10);
                IVProfilePic.setBorderWidth((float) 2);
                IVProfilePic.setBorderColor(Color.DKGRAY);
                IVProfilePic.mutateBackground(true);
                IVProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.oliver, null));
                IVProfilePic.setBackground(getResources().getDrawable(R.drawable.oliver, null));
                IVProfilePic.setOval(true);
                IVProfilePic.setTileModeX(Shader.TileMode.REPEAT);
                IVProfilePic.setTileModeY(Shader.TileMode.REPEAT);

                    IVProfilePic.setAdjustViewBounds(true);
                    cl.addView(IVProfilePic);
                    //Add Name and Job
                    TextView TVName = new TextView(getApplicationContext());
                    TVName.setId(id++);
                    TVName.setText(obj.get("name").toString().replaceAll("\"", "")+"\n"+obj.get("job").toString().replaceAll("\"", ""));
                    TVName.setTextSize(15);

                    cl.addView(TVName);
                    //Add distance
                    //cv.setPadding(0,100,0,100);
                    //ReadMore

                    ConstraintSet CS = new ConstraintSet();
                    CS.clone(cl);
                    //Pic
                    CS.connect(IVProfilePic.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
                    CS.connect(IVProfilePic.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
                    //Name and Job
                    CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.RIGHT,8);
                    CS.connect(TVName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
                    CS.connect(TVName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);



                    CS.applyTo(cl);

                    myContainer.addView(cv);
                    //CrudEmployee staff = gson.fromJson(entry, );
                    //myContainer.addView(createNew(obj.get("name").toString(), obj.get("job").toString(), Double.parseDouble(obj.get("rank").toString()), Double.parseDouble(obj.get("pay").toString()), Integer.parseInt(obj.get("pic").toString())));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });

    }

private void UpdateTextviews(JsonObject obj)
    {
    textViewDistance.setText(obj.get("dist").toString() + " km");
    textViewPay.setText(obj.get("pay").toString() + " kr/t");
    textViewProfession.setText(obj.get("job").toString());
    textViewZipcode.setText(obj.get("zipcode").toString());
    textViewProfession.setText(obj.get("job").toString().replaceAll("\"", ""));
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

    @Override
    public void onClick(View v)
    {

    }
}
