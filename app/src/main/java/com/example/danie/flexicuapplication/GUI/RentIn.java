package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.location.Criteria;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.CriteriaInterface;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPay;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaProfession;
import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
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

import java.io.Console;
import java.util.ArrayList;
import java.util.List;


public class RentIn extends AppCompatActivity implements View.OnClickListener{

    int id = 0;
    int counter = 0;
    LinearLayout scroller;
    ImageView filterMenu;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_in);
        scroller = findViewById(R.id.linearLayout);
        filterMenu = findViewById(R.id.filterMenu);
        filterMenu.setOnClickListener(this);
            Bundle bundle = getIntent().getExtras();

        List<CrudEmployee> employees = new ArrayList<>();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference(GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere");


            myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot entry : snapshot.getChildren()) {
                        //Parse JSON
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(entry.getValue().toString());
                        JsonObject obj = element.getAsJsonObject();
                        CrudEmployee people = new CrudEmployee.EmployeBuilder(
                                obj.get("name").toString().replace("\"",""))
                                .job(obj.get("job").toString().replace("\"",""))
                                .ID(Integer.parseInt(obj.get("ID").toString()))
                                .pic(obj.get("pic").toString())
                                .pay(Double.parseDouble(obj.get("pay").toString()))
                                .builder();
                        employees.add(people);



                    }
                    if(bundle != null ){
                            String payVal = bundle.getString("pay");
                            Log.e("pay", payVal);
                            CriteriaInterface salaray = new CriteriaPay(Double.parseDouble(bundle.getString("pay")));
                            salaray.meetCriteria(employees).forEach((a) -> createNew(salaray.meetCriteria(employees).get(counter++)));

                    }else {
                        employees.forEach((a)->createNew(employees.get(counter++)));
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }





    public void createNew(CrudEmployee card){

        CardView cv = new CardView(getApplicationContext());
        cv.setOnClickListener(this);
        cv.setId(id++);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
        size.setMargins(0,20,0,0);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        ConstraintLayout cl = new ConstraintLayout(this);
        cv.addView(cl);
        //Add pic
        ImageView IVProfilePic = new ImageView(this);
        IVProfilePic.setId(id++);
        //IVProfilePic.setImageResource(card.getPic());
        IVProfilePic.setAdjustViewBounds(true);
        IVProfilePic.setScaleX((float) 0.75);
        IVProfilePic.setScaleY((float) 0.75);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(card.getName()+"\n"+card.getJob());
        TVName.setTextSize(18);
        cl.addView(TVName);
        //Add Rank
        ImageView IVRank = new ImageView(this);
        IVRank.setId(id++);
        IVRank.setImageResource(R.drawable.blue_star_icon);
        IVRank.setAdjustViewBounds(true);
        IVRank.setMaxHeight(130);
        IVRank.setMaxWidth(130);
        IVRank.setPadding(0,30,0,0 );
        TextView TVRank = new TextView(this);
        TVRank.setId(id++);
        TVRank.setText(Double.toString(card.getRank()));
        TVRank.setTextSize(18);
        TVRank.setPadding(0,0,0,30);
        cl.addView(IVRank);
        cl.addView(TVRank);
        //Add pay
        TextView TVPay = new TextView(this);
        TVPay.setPadding(20,0,0,0);
        TVPay.setId(id++);
        TVPay.setTextSize(22);
        TVPay.setText(Double.toString(card.getPay()));
        TextView TVPayConst = new TextView(this);
        TVPayConst.setPadding(20,0,0,0);
        TVPayConst.setId(id++);
        TVPayConst.setTextSize(14);
        TVPayConst.setText("Timeløn");
        cl.addView(TVPay);
        cl.addView(TVPayConst);
        //ReadMore
        ImageView IVMore = new ImageView(this);
        IVMore.setId(id++);
        IVMore.setImageResource(R.drawable.arrow);
        IVMore.setScaleX((float)0.3);
        IVMore.setScaleY((float)0.3);
        cl.addView(IVMore);
        ConstraintSet CS = new ConstraintSet();
        CS.clone(cl);


        //Pic
        CS.connect(IVProfilePic.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
        CS.connect(IVProfilePic.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        //Name and Job
        CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.RIGHT,8);
        CS.connect(TVName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        CS.connect(TVName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
        //Rank
        CS.connect(IVRank.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        CS.connect(IVRank.getId(), ConstraintSet.LEFT, TVName.getId(), ConstraintSet.RIGHT,0);
        CS.connect(TVRank.getId(), ConstraintSet.TOP, IVRank.getId(), ConstraintSet.BOTTOM,0);
        CS.connect(TVRank.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,10);
        CS.connect(TVRank.getId(), ConstraintSet.LEFT, IVRank.getId(), ConstraintSet.LEFT,0);
        CS.connect(TVRank.getId(), ConstraintSet.RIGHT, IVRank.getId(), ConstraintSet.RIGHT,0);
        //Pay
        CS.connect(TVPayConst.getId(), ConstraintSet.LEFT, TVPay.getId(), ConstraintSet.LEFT,0);
        CS.connect(TVPayConst.getId(), ConstraintSet.TOP, TVRank.getId(), ConstraintSet.TOP,0);
        CS.connect(TVPay.getId(), ConstraintSet.BOTTOM, TVPayConst.getId(), ConstraintSet.TOP,0);
        CS.connect(TVPay.getId(), ConstraintSet.LEFT, IVRank.getId(), ConstraintSet.RIGHT,8);
        //Readmore
        CS.connect(IVMore.getId(), ConstraintSet.LEFT, TVPayConst.getId(), ConstraintSet.RIGHT);
        CS.connect(IVMore.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

        CS.applyTo(cl);

        scroller.addView(cv);
    }

    @Override
    public void onClick(View v) {
        if(v == filterMenu){
            Intent intent = new Intent(this, FiltersRentIn.class);
            startActivity(intent);
            finish();
        }
        else{
            int temp = v.getId();
            if(v.findViewById(temp+7).getRotation() == 0) {
                v.findViewById(temp + 7).setRotation(90);
            }else{
                v.findViewById(temp + 7).setRotation(0);
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
