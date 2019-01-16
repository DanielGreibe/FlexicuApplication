package com.example.danie.flexicuapplication.GUI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
import com.example.danie.flexicuapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateUser_photo extends AppCompatActivity {

    //Gallery select variables
    public static final int GALLERY_SELECT = 1887;
    Uri imageUri = null;
    //Upload variables
    String ImageString = "flexicu";

    Button buttonNextPage;
    ImageView preview, crossPreview;
    TextView textViewTitle;
    ConstraintLayout vaelgBilledeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_photo);

        //Get intent
        Intent intent = getIntent();


        crossPreview = findViewById(R.id.crossPreview);
        preview = findViewById(R.id.imageViewPreview);
        vaelgBilledeSelect = findViewById(R.id.vaelgBillede);
        textViewTitle = findViewById(R.id.textViewTitle);

        buttonNextPage = findViewById(R.id.buttonNextPage);

        preview.setVisibility(View.INVISIBLE);
        crossPreview.setVisibility(View.INVISIBLE);

        //Tag billede button
        vaelgBilledeSelect.setOnClickListener((view) ->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_SELECT);
        });

        //Slet preview button
        crossPreview.setOnClickListener((view) ->{
            crossPreview.setVisibility(View.INVISIBLE);
            preview.setVisibility(View.INVISIBLE);
            vaelgBilledeSelect.setVisibility(View.VISIBLE);
            ImageString = "flexicu";
            buttonNextPage.setText("FORTSÆT UDEN BILLEDE");
        });

        //Next button
        buttonNextPage.setOnClickListener((view) ->{
            Intent user_password = new Intent(this, CreateUser_password.class);
            user_password.putExtra("CVR", intent.getStringExtra("CVR"));
            user_password.putExtra("FIRMANAVN", intent.getStringExtra("FIRMANAVN"));
            user_password.putExtra("EMAIL", intent.getStringExtra("EMAIL"));
            user_password.putExtra("TELEFON", intent.getStringExtra("TELEFON"));
            user_password.putExtra("POSTNUMMER", intent.getStringExtra("POSTNUMMER"));
            user_password.putExtra("BILLEDE", ImageString);
            startActivity(user_password);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GALLERY_SELECT){
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
            squareImg = RoundedImageView.getCroppedBitmap(squareImg, 400);
            ImageString = BitMapToString(squareImg);
            preview.setVisibility(View.VISIBLE);
            preview.setImageBitmap(squareImg);
            crossPreview.setVisibility(View.VISIBLE);
            vaelgBilledeSelect.setVisibility(View.INVISIBLE);
            buttonNextPage.setText("BRUG BILLEDE");
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
