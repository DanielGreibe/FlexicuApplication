package com.example.danie.flexicuapplication.GUI;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.danie.flexicuapplication.LogicLayer.AndCriteria;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaDistance;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaInterface;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPayLower;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaPayUpper;
import com.example.danie.flexicuapplication.LogicLayer.CriteriaProfession;
import com.example.danie.flexicuapplication.LogicLayer.CrudEmployee;
import com.example.danie.flexicuapplication.LogicLayer.CrudRentIns;
import com.example.danie.flexicuapplication.LogicLayer.GlobalVariables;
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

public class RentInFragment extends Fragment {
    ImageView filter;
    LinearLayout mContainer;
    List<CrudEmployee> employees = new ArrayList<>();
    int counter = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    TextView freeSearch;
    Bundle dataBundle;
    TextView textViewNoElements;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rent_in, container, false);
        onActivityCreated(savedInstanceState);
        filter = v.findViewById(R.id.filterMenu);
        mContainer = v.findViewById(R.id.linearLayoutRentin);
        freeSearch = v.findViewById(R.id.freeSearch);
        textViewNoElements = v.findViewById(R.id.textViewNoElements);


        filter.setOnClickListener((View) -> {
        //Create filter popup and dispaly
        View popupView = getLayoutInflater().inflate(R.layout.activity_rent_in_filters, null);


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.showAtLocation(mContainer, Gravity.CENTER, 0, 0);
        View containerOfPopup = (View) popupWindow.getContentView().getParent();
        WindowManager windowManager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParameters = (WindowManager.LayoutParams) containerOfPopup.getLayoutParams();
        // add flag
        layoutParameters.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParameters.dimAmount = 0.8f;
        windowManager.updateViewLayout(containerOfPopup, layoutParameters);

        ArrayList<String> filterList = new ArrayList<String>();
        Button btn = popupView.findViewById(R.id.searchBtn);
        TextView seekbarValue = popupView.findViewById(R.id.progressValue);
        EditText lowerPay = popupView.findViewById(R.id.payLower);
        Spinner editTextprof = popupView.findViewById(R.id.spinnerJobFilter);
        EditText upperPay = popupView.findViewById(R.id.payUpper);
        ArrayList<String> items = new ArrayList<>();

        SeekBar seekBarDist = popupView.findViewById(R.id.distSlider);
        seekBarDist.setProgress(75);
        seekbarValue.setText("Fra 0 til "+ seekBarDist.getProgress() + " km");

        seekBarDist.setMax(150);
        items.add("Tømrer");
        items.add("VVS");
        items.add("Elektrikker");
        items.add("Murer");
        items.add("Anlægsgartner");
        items.add("Maler");
        items.add("Arbejdsmand");
        items.add("Smed");
        items.add("Chauffør - under 3,5T");
        items.add("Chauffør - over 3,5T");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        editTextprof.setAdapter(adapter);

        seekBarDist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            seekbarValue.setText("Fra 0 til " + String.valueOf(progress)+" km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn.setOnClickListener((view)->{
                filterList.add(lowerPay.getText().toString());
                filterList.add(upperPay.getText().toString());
                filterList.add(String.valueOf(seekBarDist.getProgress()));
                filterList.add(editTextprof.getSelectedItem().toString());
                dataBundle  = new Bundle();
                dataBundle.putStringArrayList("filterValues", filterList);
                if (lowerPay.getText().toString().equals("") || upperPay.getText().toString().equals(""))
                    {
                    Toast.makeText(getContext(), "Feltet må ikke være tomt", Toast.LENGTH_SHORT).show();
                }else {
                    popupWindow.dismiss();
                    mContainer.removeAllViews();
                    createEmployeeWithFilter();
                }
            });
        });

        //Set database reference and get rented out employees
        DatabaseReference myRef2 = database.getReference("/Udlejninger");
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot entry : snapshot.getChildren()) {
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(entry.getValue().toString());
                    JsonObject Employee = element.getAsJsonObject();

                    //If employee ID does not contain own UID, add to list
                    if(!Employee.get("id").toString().contains(GlobalVariables.getFirebaseUser().getUid())) {
                        employees.add(JsonToPersonConverter(entry, entry.getKey()));
                    }
                }
                if (dataBundle != null) {
                    ArrayList<String> filterValues = dataBundle.getStringArrayList("filterValues");
                    CriteriaInterface payLower = new CriteriaPayLower(Double.parseDouble(filterValues.get(0)));
                    CriteriaInterface payUpper = new CriteriaPayUpper(Double.parseDouble(filterValues.get(1)));
                    CriteriaInterface dist = new CriteriaDistance(Double.parseDouble(filterValues.get(2)));
                    CriteriaInterface profession = new CriteriaProfession(filterValues.get(3));
                    CriteriaInterface payBounds = new AndCriteria(payLower, profession, payUpper, dist );
                    payBounds.meetCriteria(employees).forEach((a) -> {
                    if ( employees.size() == 0)
                        {
                        textViewNoElements.setVisibility(View.VISIBLE);
                        }
                    else
                        {
                        textViewNoElements.setVisibility(View.GONE);
                        createNewEmployee(payBounds.meetCriteria(employees).get(counter++), mContainer);
                        }

                    });

                } else{
                    employees.forEach((a) -> createNewEmployee(employees.get(counter++), mContainer));
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Probably we should handle an error
                //TODO handle error
            }
        });


        return v;
    }


    @SuppressLint("StaticFieldLeak")
    public void createNewEmployee(CrudEmployee Employee, LinearLayout myContainer) {
        textViewNoElements.setVisibility(View.GONE);
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
        TextView headerDescription = ExpandableCardview.findViewById(R.id.textViewHeaderDescription);
        TextView textViewDescription = ExpandableCardview.findViewById(R.id.textViewDescription);
        TextView textViewLejeperiodeStart = ExpandableCardview.findViewById(R.id.textViewLejeperiodeStart);
        TextView textViewLejeperiodeSlut = ExpandableCardview.findViewById(R.id.textViewLejeperiodeSlut);

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
        textViewLejeperiodeStart.setText(Employee.getStartDate().toString().replaceAll("\"",""));
        textViewLejeperiodeSlut.setText(Employee.getEndDate().toString().replaceAll("\"",""));


        if(Employee.getdescription().equals("")){
            headerDescription.setVisibility(View.GONE);
            textViewDescription.setVisibility(View.GONE);
        } else {
            textViewDescription.setText(Employee.getdescription());
        }

        //Set temporary picture while real pictures are downloading
        profilePic.setImageResource(R.drawable.download);


        //We want to download images for the list of workers
        if(Employee.getPic().equals("flexicu")){
            Glide.with(this)
                    .load(R.drawable.flexiculogocube)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
        } else {
            //System.out.println(src);
            URL finalUrl = null;
            try {
                finalUrl = new URL(Employee.getPic());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //We want to download images for the list of workers
            Glide.with(this)
                    .load(finalUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic);
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
                    Integer.toString(Employee.getZipcode()), Double.toString(Employee.getRank()), Employee.getPic(), "udlejet", Employee.getdescription());
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
            Spacer.setVisibility(View.GONE);
            Log.e("Childs", mContainer.getChildCount()+"");

            int antalLedigeMedarbejdere = 0;
            for (int i = 0; i < mContainer.getChildCount(); i++)
                {
                if (mContainer.getChildAt(i).getVisibility() == View.VISIBLE)
                    {
                    antalLedigeMedarbejdere++;
                    }
                }
            if (antalLedigeMedarbejdere == 0)
                {
                mContainer.removeAllViews();
                mContainer.addView(textViewNoElements);
                textViewNoElements.setVisibility(View.VISIBLE);
                textViewNoElements.setText("Der er ikke nogle medarbejdere ledige :(");
                }

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
                .description(obj.get("description").toString().replaceAll("\"", ""))
                .endDate(obj.get("rentEnd").toString().replaceAll("\"",""))
                .startDate(obj.get("rentStart").toString().replaceAll("\"",""))
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
                .description(obj.get("description").toString().replaceAll("\"", ""))
                .endDate(obj.get("endDate").toString().replaceAll("\"",""))
                .startDate(obj.get("startDate").toString().replaceAll("\"",""))
                .builder();
        return people;
    }

@TargetApi(Build.VERSION_CODES.N)
public void createEmployeeWithFilter()
    {
    counter = 0;
    if (dataBundle != null) {
    ArrayList<String> filterValues = dataBundle.getStringArrayList("filterValues");
    CriteriaInterface payLower = new CriteriaPayLower(Double.parseDouble(filterValues.get(0)));
    CriteriaInterface payUpper = new CriteriaPayUpper(Double.parseDouble(filterValues.get(1)));
    CriteriaInterface dist = new CriteriaDistance(Double.parseDouble(filterValues.get(2)));
    CriteriaInterface profession = new CriteriaProfession(filterValues.get(3));
    CriteriaInterface payBounds = new AndCriteria(payLower, profession, payUpper, dist );
    payBounds.meetCriteria(employees).forEach((a) -> {
        createNewEmployee(payBounds.meetCriteria(employees).get(counter++), mContainer);
        if (mContainer.getChildCount() == 1)
            {
            textViewNoElements.setVisibility(View.GONE);
            }
    });
    }
}}


