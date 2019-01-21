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

public class CreateEmployeeImage extends AppCompatActivity implements View.OnClickListener{

    Button buttonNextPage;
    ImageView preview, crossPreview;
    TextView textViewTitle;
    ConstraintLayout firmaBilledeSelect, tagBilledeSelect, vaelgBilledeSelect;
    String name;
    String erhverv;

    //Camera variables
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    //Gallery select variables
    public static final int GALLERY_SELECT = 1887;

    //Upload variables
    String ImageString = "flexicu";
    Uri imageUri = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee_image);

        crossPreview = findViewById(R.id.crossPreview);
        preview = findViewById(R.id.imageViewPreview);
        firmaBilledeSelect = findViewById(R.id.vaelgFirmaBillede);
        vaelgBilledeSelect = findViewById(R.id.vaelgBillede);
        tagBilledeSelect = findViewById(R.id.tagBillede);
        textViewTitle = findViewById(R.id.textViewTitle);
        name = ((GlobalVariables) this.getApplication()).getTempEmployeeName();
        textViewTitle.setText("Tilføj et billede af " + name + " eller firmaets logo");

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

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);

        HorizontalScrollView scroller = findViewById(R.id.horizontalScrollView2);
        scroller.post(new Runnable() {
            @Override
            public void run() {
                scroller.scrollTo(500, 0);
            }
        });

        preview.setVisibility(View.INVISIBLE);
        crossPreview.setVisibility(View.INVISIBLE);


        //Tag billede button
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

        //Vælg billede button
        vaelgBilledeSelect.setOnClickListener((view) ->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_SELECT);
        });

        //Brug firmabillede
        firmaBilledeSelect.setOnClickListener((view) ->{
            ImageString = "flexicu";
            crossPreview.setVisibility(View.VISIBLE);
            preview.setVisibility(View.VISIBLE);

            //Get round image
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flexiculogocube);
            bitmap = RoundedImageView.getCroppedBitmap(bitmap, 200);
            preview.setImageBitmap(bitmap);

        });

        //Slet preview button
        crossPreview.setOnClickListener((view) ->{
            crossPreview.setVisibility(View.INVISIBLE);
            preview.setVisibility(View.INVISIBLE);
            ImageString = "flexicu";
        });

    }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            if (v == buttonNextPage) {
                Intent CreateEmployeeFinish = new Intent(this, CreateEmployeeFinish.class);
                ((GlobalVariables) this.getApplication()).setTempEmployeeImage(ImageString);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                startActivity(CreateEmployeeFinish, bndlanimation);
            }
        }


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
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //imageUri = Uri.parse(data.toURI());
                //System.out.println("URI IS: " + imageUri.toString());

                //Make image square
                int minPixels = 0;
                if(photo.getWidth() < photo.getHeight()) minPixels = photo.getWidth();
                else minPixels = photo.getHeight();
                Bitmap squareImg = Bitmap.createBitmap(photo, ((photo.getWidth()-minPixels)/2), ((photo.getHeight()-minPixels)/2), minPixels, minPixels);
                //Get round image
                squareImg = RoundedImageView.getCroppedBitmap(squareImg, 200);

                ImageString = BitMapToString(squareImg);
                preview.setVisibility(View.VISIBLE);
                preview.setImageBitmap(squareImg);
                crossPreview.setVisibility(View.VISIBLE);
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
                int minPixels = 0;
                if(photo.getWidth() < photo.getHeight()) minPixels = photo.getWidth();
                else minPixels = photo.getHeight();
                Bitmap squareImg = Bitmap.createBitmap(photo, ((photo.getWidth()-minPixels)/2), ((photo.getHeight()-minPixels)/2), minPixels, minPixels);
                //Get round image
                squareImg = RoundedImageView.getCroppedBitmap(squareImg, 200);
                ImageString = BitMapToString(squareImg);
                preview.setVisibility(View.VISIBLE);
                preview.setImageBitmap(squareImg);
                crossPreview.setVisibility(View.VISIBLE);
            }
        }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        finish();
    }
    }
