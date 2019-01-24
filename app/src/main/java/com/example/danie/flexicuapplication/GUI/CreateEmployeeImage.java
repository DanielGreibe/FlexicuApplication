package com.example.danie.flexicuapplication.GUI;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anton46.stepsview.StepsView;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
import com.example.danie.flexicuapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreateEmployeeImage extends AppCompatActivity implements View.OnClickListener{

    //Declare global variables
    Button buttonNextPage;
    ImageView preview, crossPreview;
    TextView textViewTitle, springOver;
    ConstraintLayout firmaBilledeSelect, tagBilledeSelect, vaelgBilledeSelect;
    String name;

    //Camera variables
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    //Gallery select variables
    public static final int GALLERY_SELECT = 1887;

    //Upload variables
    String ImageString = "flexicu";
    Bitmap ImageData = null;
    Uri imageUri = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_image);

        //Create views, add text to them and hide the previews
        crossPreview = findViewById(R.id.crossPreview);
        preview = findViewById(R.id.imageViewPreview);
        firmaBilledeSelect = findViewById(R.id.vaelgFirmaBillede);
        vaelgBilledeSelect = findViewById(R.id.vaelgBillede);
        tagBilledeSelect = findViewById(R.id.tagBillede);
        textViewTitle = findViewById(R.id.textViewTitle);
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Tilføj et billede af " + name + " eller firmaets logo");
        springOver = findViewById(R.id.springOverTV);
        springOver.setOnClickListener(this);
        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        preview.setVisibility(View.INVISIBLE);
        crossPreview.setVisibility(View.INVISIBLE);
        HorizontalScrollView scroller = findViewById(R.id.horizontalScrollView2);
        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.scrollTo(500, 0);
            }
        });

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
        StepBean stepBean7 = new StepBean("Billede",0);
        StepBean stepBean8 = new StepBean("Bekræft",-1);
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


        //If you don't have the permission to open the camera ask for them, otherwise open the camera.
        tagBilledeSelect.setOnClickListener((view) ->{
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        //If you don't have the permission to open the Gallery ask for them, otherwise open the gallery
        vaelgBilledeSelect.setOnClickListener((view) ->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_SELECT);
        });

        //Sets the preview as Flexicu
        firmaBilledeSelect.setOnClickListener((view) ->{
            ImageString = "flexicu";
            crossPreview.setVisibility(View.VISIBLE);
            preview.setVisibility(View.VISIBLE);

            //Get round image
            Glide.with(this).load(R.drawable.flexiculogocube).
                    apply(RequestOptions.circleCropTransform())
                    .into(preview);
        });

        //If you clicked on the cross, delete the preview and set the imageString to default flexicu
        crossPreview.setOnClickListener((view) ->{
            crossPreview.setVisibility(View.INVISIBLE);
            preview.setVisibility(View.INVISIBLE);
            ImageString = "flexicu";
        });

    }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            //Clicked to go to next page and you have chosen one of the three options to use an image go to createEmployeeFinish
            if(v == buttonNextPage && preview.getVisibility() != View.INVISIBLE){
                Intent CreateEmployeeFinish = new Intent(this, CreateEmployeeFinish.class);
                ((GlobalVariables) this.getApplication()).setTempEmployeeImage(ImageData);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                startActivity(CreateEmployeeFinish, bndlanimation);
                //Clicked to go to next page and you haven't chosen a picture, write an error message to the user
            } else if(v == buttonNextPage && preview.getVisibility() == View.INVISIBLE){
                Toast.makeText(this, "Vælg et billede, eller firmalogo", Toast.LENGTH_SHORT).show();
                //Clicked to skip and you haven't chosen an image go to createEmployeeFinish (Flexicu is used as default image)
            } else if(v == springOver && preview.getVisibility() == View.INVISIBLE){
                Intent CreateEmployeeFinish = new Intent(this, CreateEmployeeFinish.class);
                ((GlobalVariables) this.getApplication()).setTempEmployeeImage(null);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                startActivity(CreateEmployeeFinish, bndlanimation);
            }
        }

        //If you requested permission and it was accepted open the camera otherwise write a toast saying acces was denied.
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new
                            Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }

        //If you opened the camera and took a picture set imageData, imageString and previewImage accordingly
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");


                //Get round image
                Glide.with(this).load(photo).
                        apply(RequestOptions.circleCropTransform())
                        .into(preview);
                ImageData = photo;
                ImageString = "SpecialImage";


                preview.setVisibility(View.VISIBLE);
                crossPreview.setVisibility(View.VISIBLE);
                //If you chose an image from the Camera set photo variable to the image chosen.
            }else if(requestCode == GALLERY_SELECT){
                Bitmap photo = null;
                if (resultCode == RESULT_OK) {
                    try {
                        imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        photo = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{return;} //Return if no image selected

                //Get round image chosen with either the camera or the gallery
                Glide.with(this).load(photo).
                        apply(RequestOptions.circleCropTransform())
                        .into(preview);
                ImageString = "SpecialImage";
                ImageData = photo;
                preview.setVisibility(View.VISIBLE);
                crossPreview.setVisibility(View.VISIBLE);
            }
        }
    @Override
    public void onBackPressed() {
    //Makes a slide animation when backpressed
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        finish();
    }
    }
