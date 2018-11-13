package com.example.danie.flexicuapplication;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Indlej extends AppCompatActivity implements View.OnClickListener {

    int id = 1;
    LinearLayout scroller;
    ImageView filterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indlej);

        scroller = findViewById(R.id.linearLayout);
        filterMenu = findViewById(R.id.filterMenu);

        filterMenu.setOnClickListener(this);

        createNew("Mathias", "Java",4.2, 250., 45);

        }
    public void createNew(String name, String job, double rank, Double pay, int dist){
        CardView cv = new CardView(getApplicationContext());
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
        IVProfilePic.setImageResource(R.drawable.download);
        IVProfilePic.setAdjustViewBounds(true);
        IVProfilePic.setScaleX((float) 0.75);
        IVProfilePic.setScaleY((float) 0.75);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(name+"\n"+job);
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
        TVRank.setText(Double.toString(rank));
        TVRank.setTextSize(18);
        TVRank.setPadding(0,0,0,30);
        cl.addView(IVRank);
        cl.addView(TVRank);
        //Add pay
        TextView TVPay = new TextView(this);
        TVPay.setPadding(20,0,0,0);
        TVPay.setId(id++);
        TVPay.setTextSize(22);
        TVPay.setText(Double.toString(pay));
        TextView TVPayConst = new TextView(this);
        TVPayConst.setPadding(20,0,0,0);
        TVPayConst.setId(id++);
        TVPayConst.setTextSize(14);
        TVPayConst.setText("Timeløn");
        cl.addView(TVPay);
        cl.addView(TVPayConst);
        //Add distance
        TextView TVDist = new TextView(this);
        TVDist.setId(id++);
        TVDist.setTextSize(22);
        TVDist.setText(Integer.toString(dist));
        TVDist.setPadding(60,0,0,0);
        TextView TVDistConst = new TextView(this);
        TVDistConst.setId(id++);
        TVDistConst.setTextSize(14);
        TVDistConst.setText("km væk");
        TVDistConst.setPadding(30,0,0,0);
        cl.addView(TVDist);
        cl.addView(TVDistConst);
        //ReadMore
        ImageView IVMore = new ImageView(this);
        IVMore.setId(id++);
        IVMore.setImageResource(R.drawable.arrow);
        IVMore.setScaleX((float)0.5);
        IVMore.setScaleY((float)0.5);
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
        //Dist
        CS.connect(TVDist.getId(), ConstraintSet.LEFT, TVPay.getId(), ConstraintSet.RIGHT, 0);
        CS.connect(TVDistConst.getId(), ConstraintSet.LEFT, TVDist.getId(), ConstraintSet.LEFT,0);
        CS.connect(TVDist.getId(), ConstraintSet.TOP, TVPay.getId(), ConstraintSet.TOP,0);
        CS.connect(TVDistConst.getId(), ConstraintSet.BOTTOM, TVPayConst.getId(), ConstraintSet.BOTTOM,0);
        //Readmore
        //CS.connect(IVMore.getId(), ConstraintSet.LEFT, TVDist.getId(), ConstraintSet.RIGHT,0);




        CS.applyTo(cl);


    scroller.addView(cv);
    }

    @Override
    public void onClick(View v) {
        createNew("Mathias", "Java",4.2, 250., 45);
    }
}
