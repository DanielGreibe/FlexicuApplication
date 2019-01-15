package com.example.danie.flexicuapplication.GUI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
    String ImageString = null;
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

        buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);

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

        //Tag billede button
        vaelgBilledeSelect.setOnClickListener((view) ->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_SELECT);
        });

        //Tag billede button
        crossPreview.setOnClickListener((view) ->{
            crossPreview.setVisibility(View.INVISIBLE);
            preview.setVisibility(View.INVISIBLE);
            ImageString = null;
        });

    }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            if (v == buttonNextPage) {
                Intent CreateEmployeeFinish = new Intent(this, CreateEmployeeFinish.class);
                ((GlobalVariables) this.getApplication()).setTempEmployeeImage(ImageString);
                startActivity(CreateEmployeeFinish);
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
    }
