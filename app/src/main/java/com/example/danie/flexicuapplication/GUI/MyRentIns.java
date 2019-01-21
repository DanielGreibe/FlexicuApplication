package com.example.danie.flexicuapplication.GUI;

//TODO tilføj timeløn til mineindlejninger og mineudlejninger
//TODO ret til uniforme betegnelser overalt i app (f.eks. 'km væk' ELLER 'radius')


import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class MyRentIns extends AppCompatActivity {

    int id = 1;
    LinearLayout scroller;
    ConstraintLayout mainLayout;
    TextView mainText_textView;
    TextView textViewLejeperiodeStart;
    TextView textViewLejeperiodeSlut;
    TextView textViewLejer;
    TextView textViewErhverv;
    TextView textViewLokation;
    JsonObject obj;


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rent_ins);

        scroller = findViewById(R.id.linearlayout1);
        textViewLejeperiodeStart = findViewById(R.id.textViewLejeperiodeStart);
        textViewLejeperiodeSlut = findViewById(R.id.textViewLejeperiodeSlut);
        textViewLejer = findViewById(R.id.textViewLejer);
        textViewErhverv = findViewById(R.id.textViewErhverv);
        textViewLokation = findViewById(R.id.textViewLokation);

        mainLayout = findViewById(R.id.MineIndlejninger_mainLayout);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Indlejninger");

        /*CrudRentIns en = new CrudRentIns("Ternet Ninja", "Kriger", "1/2 - 2019", "1/4 - 2019", "Aske", "Ika", 4.9);
        CrudRentIns to = new CrudRentIns("Daniel Gribe", "Java", "1/4 - 2019", "15/7 - 2019", "Mathias", "Lyngby", 4.0);
        Gson gson = new Gson();
        String employeeJSON = gson.toJson(en);
        myRef.child(Integer.toString(en.getID())).setValue(employeeJSON);
        employeeJSON = gson.toJson(to);
        myRef.child(Integer.toString(to.getID())).setValue(employeeJSON);*/

        //Check for existing ID.
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()){

                    //Parse JSON
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entry.getValue().toString());
                    obj = element.getAsJsonObject();


                    System.out.println("ID IS "+obj.get("ID"));
                    createNew(obj);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });


//Listview og __view
        //Over i XML-fil


        //mainLayout.addView(myButton);
        //mainLayout.addView(myConstraint);

        //Hide keyboard on load
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    public void createNew(JsonObject obj){
        CardView cv = new CardView(getApplicationContext());
        cv.setId(id++);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
        size.setMargins(0, 5, 0, 5);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        ConstraintLayout cl = new ConstraintLayout(this);
        cv.addView(cl);
        //Add pic
        ImageView IVProfilePic = new ImageView(this);
        //@SuppressLint("ResourceType") LinearLayout IVProfilePic = (LinearLayout) findViewById(R.drawable.circle);


        IVProfilePic.setId(id++);
        //IVProfilePic.setImageResource(R.drawable.circle);
        //IVProfilePic.setBackground(R.drawable.roundimg);
        //
        IVProfilePic.setImageResource(R.drawable.download);
        IVProfilePic.setAdjustViewBounds(true);
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(obj.get("name").toString().replaceAll("\"","")+"\n"+obj.get("job").toString().replaceAll("\"",""));
        TVName.setTextSize(18);

        cl.addView(TVName);
        //Add distance
        //cv.setPadding(0,100,0,100);
        //ReadMore

        String rank = obj.get("rank").toString().replace("\"","");
        String rentStart = obj.get("rentStart").toString().replace("\"", "");
        String rentEnd = obj.get("rentEnd").toString().replaceAll("\"","");
        String job = obj.get("job").toString().replace("\"","");
        String owner = obj.get("owner").toString().replaceAll("\"","");
        String location = obj.get("location").toString().replaceAll("\"","");

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

        //Add onclick to card view
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewLejeperiodeStart.setText(rentStart);
                textViewLejeperiodeSlut.setText(rentEnd);
                textViewLejer.setText(owner);
                textViewErhverv.setText(job);
                textViewLokation.setText(location);
            }
        });


        scroller.addView(cv);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        finish();
    }
    }
