package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danie.flexicuapplication.LogicLayer.AndCriteria;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaDistance;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaInterface;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPayLower;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPayUpper;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaProfession;
import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.LogicLayer.CrudRentIns;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
import com.example.danie.flexicuapplication.LogicLayer.RoundedImageView;
import com.example.danie.flexicuapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RentInFragment extends Fragment {
    ImageView filter;
    LinearLayout mContainer;
    List<CrudEmployee> employees = new ArrayList<>();
    int counter = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextView freeSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rent_in, container, false);
        onActivityCreated(savedInstanceState);
        filter = v.findViewById(R.id.filterMenu);
        mContainer = v.findViewById(R.id.linearLayoutRentin);
        freeSearch = v.findViewById(R.id.freeSearch);

                filter.setOnClickListener((View) -> {
                    Intent filtermenu = new Intent(getContext(), FiltersRentIn.class);
                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getContext(), R.anim.anim_slide_in_left, R.anim.anim_slide_out_left).toBundle();
                    startActivity(filtermenu, bndlanimation);

                });

        Bundle bundle = getActivity().getIntent().getExtras();
        DatabaseReference myRef2 = database.getReference("/Udlejninger");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()) {
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entry.getValue().toString());
                    JsonObject Employee = element.getAsJsonObject();
                    if(!Employee.get("id").toString().contains(GlobalVariables.getFirebaseUser().getUid())) {
                        employees.add(JsonToPersonConverter(entry, entry.getKey()));
                    }
                }
                if (bundle != null) {
                    ArrayList<String> filterValues = bundle.getStringArrayList("filterValues");
                    CriteriaInterface payLower = new CriteriaPayLower(Double.parseDouble(filterValues.get(0)));
                    CriteriaInterface payUpper = new CriteriaPayUpper(Double.parseDouble(filterValues.get(1)));
                    CriteriaInterface dist = new CriteriaDistance(Double.parseDouble(filterValues.get(2)));
                    CriteriaInterface profession = new CriteriaProfession(filterValues.get(3));
                    CriteriaInterface payBounds = new AndCriteria(payLower, profession, payUpper, dist );
                    payBounds.meetCriteria(employees).forEach((a) -> {
                        createNewEmployee(payBounds.meetCriteria(employees).get(counter++), mContainer);

                    });

                } else{
                    employees.forEach((a) -> createNewEmployee(employees.get(counter++), mContainer));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


    @SuppressLint("StaticFieldLeak")
    public void createNewEmployee(CrudEmployee Employee, LinearLayout myContainer) {
        //Inflater to XML filer ind, et Cardview og en Spacer som bruges til at skabe afstand fordi det ikke er muligt med Padding eller Layout Margin.
        View ExpandableCardview = getLayoutInflater().inflate(R.layout.employee_cardview, null, false);
        View Spacer = getLayoutInflater().inflate(R.layout.spacer, null, false);

        //Lav FindViewById på Viewsne som er blevet inflated
        TextView textViewPay = ExpandableCardview.findViewById(R.id.textViewLøn);
        TextView textViewZipcode = ExpandableCardview.findViewById(R.id.textViewZipcode);
        TextView textViewDistance = ExpandableCardview.findViewById(R.id.textViewDistance);
        TextView textViewStatus = ExpandableCardview.findViewById(R.id.textViewHeaderStatus);
        LinearLayout linearLayoutCollapsed = ExpandableCardview.findViewById(R.id.linearLayoutCollapsed);
        LinearLayout linearLayoutExpanded = ExpandableCardview.findViewById(R.id.linearLayoutExpanded);
        ImageView imageButtonArrow = ExpandableCardview.findViewById(R.id.imageButtonExpand);
        TextView textViewName = ExpandableCardview.findViewById(R.id.textViewName);
        TextView textViewProfession = ExpandableCardview.findViewById(R.id.textViewProfession);
        ImageView profilePic = ExpandableCardview.findViewById(R.id.imageViewImage);
        Button indlejButton = ExpandableCardview.findViewById(R.id.buttonUdlej);
        indlejButton.setText("Indlej");

        //Hent data og gør det til et JsonObject

        //Træk data ud af Json Objektet og put det på textviews i Cardviewet.
        textViewPay.setText(String.valueOf(Employee.getPay()) + " kr/t");
        textViewZipcode.setText(String.valueOf(Employee.getZipcode()));
        textViewDistance.setText(String.valueOf(Employee.getDist()) + " km");
        textViewName.setText(Employee.getName().replace("\"", ""));
        textViewProfession.setText(Employee.getJob().replace("\"", ""));
        String startdate = Employee.getStartDate();
        String enddate = Employee.getEndDate();
        String rank = String.valueOf(Employee.getRank());
        String dr = Employee.getKey();
        String urlString = Employee.getPic().replaceAll("\"", "");

        //Set temporary picture while real pictures are downloading
        profilePic.setImageResource(R.drawable.download);
        //We want to download images for the list of workers


        //System.out.println(src);
        URL url = null;
        try {
            url = new URL(Employee.getPic());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println("HERE3 " + Employee.getPic());



        if(Employee.getPic().equals("flexicu")){
            int minPixels = 0;
            Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.flexiculogocube);
            if(photo.getWidth() < photo.getHeight()){
                minPixels = photo.getWidth();
            }
            else {
                minPixels = photo.getHeight();
                Bitmap squareImg = Bitmap.createBitmap(photo, ((photo.getWidth() - minPixels) / 2), ((photo.getHeight() - minPixels) / 2), minPixels, minPixels);
                squareImg = RoundedImageView.getCroppedBitmap(squareImg, 400);
                profilePic.setImageBitmap(squareImg);
            }
        } else {
            //We want to download images for the list of workers
            URL finalUrl = url;
            new AsyncTask<Void, Void, Bitmap>() {
                //Get pictures in background
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        //Use glide for faster load and to save images in cache! (glide.asBitmap does not create its own asynctask)
                        Bitmap myBitmap = Glide
                                .with(profilePic)
                                .asBitmap()
                                .load(finalUrl)
                                .submit()
                                .get();
                        return myBitmap;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                //On return update images in list
                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
                @Override
                protected void onPostExecute(Bitmap s) {
                    super.onPostExecute(s);
                    s = RoundedImageView.getCroppedBitmap(s, 200);
                    profilePic.setImageBitmap(s);
                }
            }.execute();
        }


        //Lav OnClickListener som håndterer at viewet bliver expanded og collapsed.
        linearLayoutCollapsed.setOnClickListener((test) ->
        {

            expand(linearLayoutExpanded, imageButtonArrow);
        });
        imageButtonArrow.setOnClickListener((test) ->
        {
            expand(linearLayoutExpanded, imageButtonArrow);
        });

        //Tilføjer Cardviewet og Spaceren til det Linære Layout myContainer.
        myContainer.addView(ExpandableCardview);
        myContainer.addView(Spacer);

        indlejButton.setOnClickListener(view -> {
            // Tilføj pågældende udlejning til ejen indlejning
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users/"+GlobalVariables.getFirebaseUser().getUid()+"/Indlejninger");
            CrudRentIns temp = new CrudRentIns(Employee.getName(),
                    Double.toString(Employee.getPay()), Employee.getJob(),
                    Employee.getStartDate(), Employee.getEndDate(), Employee.getOwner(),
                    Integer.toString(Employee.getZipcode()), Double.toString(Employee.getRank()), Employee.getPic(), "udlejet");
            Gson gson = new Gson();
            String employeeJSON = gson.toJson(temp);
            myRef.child(Integer.toString(temp.getID())).setValue(employeeJSON);

            String employeeID = Employee.getID().substring(Employee.getID().length()-4);
            String ownerID = Employee.getID().substring(0, Employee.getID().length()-4);




            DatabaseReference updateStatus = database.getReference("Users/"+ownerID+"/Medarbejdere/");
            CrudEmployee employee = JsonToPersonConverter2(gson.toJson(Employee), "udlejet", employeeID);

            updateStatus.child(employeeID).setValue(gson.toJson(employee));

            //Fjern udlejninger fra databasen
            String key = dr;
            String owner = Employee.getOwner();
            DatabaseReference toDelete = FirebaseDatabase.getInstance().getReference("Udlejninger").child(key);
            toDelete.removeValue();

            ExpandableCardview.setVisibility(View.GONE);

        });
    }

    private void expand(LinearLayout linearLayoutExpanded, ImageView imageButtonArrow) {
        if (linearLayoutExpanded.getVisibility() == View.GONE) {
            linearLayoutExpanded.setVisibility(View.VISIBLE);
            imageButtonArrow.setRotation(90);
        } else if (linearLayoutExpanded.getVisibility() == View.VISIBLE) {
            linearLayoutExpanded.setVisibility(View.GONE);
            imageButtonArrow.setRotation(0);
        }
    }

    public CrudEmployee JsonToPersonConverter(DataSnapshot entry, String dr){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry.getValue().toString());
        JsonObject obj = element.getAsJsonObject();

        CrudEmployee people = new CrudEmployee.EmployeBuilder(
                obj.get("name").toString().replace("\"", ""))
                .job(obj.get("job").toString().replace("\"", ""))
                .ID(obj.get("id").toString().replaceAll("\"",""))
                .pic(obj.get("pic").toString().replace("\"", ""))
                .pay(Double.parseDouble(obj.get("pay").toString().replaceAll("\"","")))
                .startDate(obj.get("rentStart").toString())
                .endDate(obj.get("rentEnd").toString())
                .key(dr)
                .status(obj.get("status").toString().replaceAll("\"", ""))
                .owner(obj.get("owner").toString().replaceAll("\"",""))
                .dist(Integer.parseInt(obj.get("dist").toString().replaceAll("\"","")))
                .zipcode(Integer.parseInt(obj.get("zipcode").toString().replaceAll("\"","")))
                .builder();
        return people;
    }

    public CrudEmployee JsonToPersonConverter2(String entry, String status, String ownerID) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(entry);
        JsonObject obj = element.getAsJsonObject();

        CrudEmployee people = new CrudEmployee.EmployeBuilder(
                obj.get("name").toString().replace("\"", ""))
                .job(obj.get("job").toString().replace("\"", ""))
                .ID(ownerID)
                .pic(obj.get("pic").toString().replaceAll("\"", ""))
                .owner(obj.get("owner").toString().replaceAll("\"", ""))
                .pay(Double.parseDouble(obj.get("pay").toString().replaceAll("\"", "")))
                .status(status)
                .owner(obj.get("owner").toString().replaceAll("\"", ""))
                .dist(Integer.parseInt(obj.get("dist").toString().replaceAll("\"", "")))
                .zipcode(Integer.parseInt(obj.get("zipcode").toString().replaceAll("\"", "")))
                .builder();
        return people;
    }
}


