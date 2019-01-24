package com.example.danie.flexicuapplication.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    //Declare Global Variables and Views
    TextView textViewTitle;
    Button buttonNextPage;
    LinearLayout container;

    String name;
    String year;
    String profession;
    int zipcode;
    String description;
    String pay;
    int distance;
    Bitmap imgData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_finish);

        textViewTitle = findViewById(R.id.textViewTitle);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        container = findViewById(R.id.linearLayoutFinish);

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

        HorizontalScrollView scroller = findViewById(R.id.horizontalScrollView2);
        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.scrollTo(500, 0);
            }
        });

        //Get all the data entered in the previous createEmployee activites into their corresponding variables
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        year = ((GlobalVariables) this.getApplication()).getTempEmployeeYear();
        profession = ((GlobalVariables) this.getApplication()).getTempEmployeeProfession();
        zipcode = ((GlobalVariables) this.getApplication()).getTempEmployeeZipcode();
        description = ((GlobalVariables) this.getApplication()).getTempEmployeeDescription();
        pay = ((GlobalVariables) this.getApplication()).getTempEmployeePay();
        distance = ((GlobalVariables) this.getApplication()).getTempEmployeeDistance();
        imgData = ((GlobalVariables) this.getApplication()).getTempEmployeeImage();

        createEmployeeCardview();
    }

    @Override
    public void onClick(View v){
        if ( v == buttonNextPage ){
            //Make a CrudEmployee object using a Builder pattern

            CrudEmployee employee = new CrudEmployee.EmployeBuilder(name).job(profession).pay(Double.parseDouble(pay)).zipcode(zipcode).dist(distance).status("ikke udlejet").available("Home").owner(GlobalVariables.getFirebaseUser().getUid()).description(description).builder();

            //Upload image if standard image is selected or if custom image is selected!
            if(imgData == null){
                employee.setPic("flexicu");
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(("Users/"+ getFirebaseUser().getUid()+"/Medarbejdere"));
                Gson gson = new Gson();
                String employeeJSON = gson.toJson(employee);
                myRef.child(employee.getID()).setValue(employeeJSON);
            }else {
                uploadImg(employee.getID(), employee);
            }
        //Create a new intent to start the activity TabbedRentOut.
        //ClearTop will remove all activites on top of TabbedRentOut in the activity stack removing all CreateEmployee activites.
        //PutExtra is used to change behaviour when you enter the activity from CreateEmployeeFinish
        Intent Udlej = new Intent(this, TabbedRentOut.class);
            Udlej.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            String temp = "createEmployeeFinish";
            Udlej.putExtra("createEmployeeFinish",temp);
            startActivity(Udlej);
            finish();
        }
    }

    public void uploadImg(String ID, CrudEmployee employee) {
        //Uploads a compressed version of the employeeImage to Firebase
        //If it fails throw an exception, otherwise upload the image of the user and upload the user to the database.
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference databaseRef = mStorageRef.child("Users/"+getFirebaseUser().getUid()+"/Medarbejdere/"+ID+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgData.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        databaseRef.putBytes(data).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return databaseRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    employee.setPic(task.getResult().toString());
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(( "Users/"+getFirebaseUser().getUid()+"/Medarbejdere"));
                    Gson gson = new Gson();
                    String employeeJSON = gson.toJson(employee);
                    myRef.child(employee.getID()).setValue(employeeJSON);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        finish();
    }

    public void createEmployeeCardview(){
        View ExpandableCardview = getLayoutInflater().inflate(R.layout.employee_cardview, null, false);
        //Inflate an ExpandableCardview to the layout and make findViewById on them
        TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
        TextView textViewZipcode = ExpandableCardview.findViewById(R.id.textViewZipcode);
        TextView textViewDistance = ExpandableCardview.findViewById(R.id.textViewDistance);
        TextView textViewStatus = ExpandableCardview.findViewById(R.id.textViewStatus);
        TextView headderStatus = ExpandableCardview.findViewById(R.id.textViewHeaderStatus);
        LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
        ImageView imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);
        TextView textViewName = ExpandableCardview.findViewById(R.id.textViewName);
        TextView textViewProfession = ExpandableCardview.findViewById(R.id.textViewProfession);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);
        Button udlejBtn = ExpandableCardview.findViewById(R.id.buttonUdlej);
        TextView textViewLejeperiodeStart = ExpandableCardview.findViewById(R.id.textViewLejeperiodeStart);
        TextView textViewLejeperiodeSlut = ExpandableCardview.findViewById(R.id.textViewLejeperiodeSlut);
        TextView headerLejStart = ExpandableCardview.findViewById(R.id.textViewHeaderLejeperiodeStart);
        TextView headerLejEnd = ExpandableCardview.findViewById(R.id.textViewHeaderLejeperiodeSlut);
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);
        TextView rate = ExpandableCardview.findViewById(R.id.textViewRating);
        ImageView rateImage = ExpandableCardview.findViewById(R.id.imageButton2);
        TextView headerDescription = ExpandableCardview.findViewById(R.id.textViewHeaderDescription);

        //Apply data from the CrudEmployee to the TextViews in the cardview
        //If no image is selected, or the flexiculogo is selected add that
        //Otherwise load the image with Glide
        textViewPay.setText(pay + " kr/t");
        textViewZipcode.setText((zipcode + ""));
        textViewDistance.setText((distance + " km"));
        textViewName.setText(name);
        textViewProfession.setText(profession);
        textViewStatus.setText("ikke udlejet");
        textViewDescription.setText(description);
        if (imgData == null){
            //Get round image
            Glide.with(this).load(R.drawable.flexiculogocube).
                    apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
        } else{
                //Get round image
                Glide.with(this).load(imgData).
                        apply(RequestOptions.circleCropTransform())
                        .into(profilePic);
        }

        headderStatus.setText("Fødselsår");
        textViewStatus.setText(year);

        if(description.equals("")){
            headerDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
        }
        //Remove unused Views from the cardview and add the cardview to the page
        rate.setVisibility(View.GONE);
        rateImage.setVisibility(View.GONE);
        headerLejStart.setVisibility(View.GONE);
        headerLejEnd.setVisibility(View.GONE);
        textViewLejeperiodeSlut.setVisibility(View.GONE);
        textViewLejeperiodeStart.setVisibility(View.GONE);
        udlejBtn.setVisibility(View.GONE);
        imageButtonArrow.setVisibility(View.INVISIBLE);
        linearLayoutExpanded.setVisibility(View.VISIBLE);
        container.addView(ExpandableCardview);
    }
}
