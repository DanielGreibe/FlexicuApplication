package com.example.danie.flexicuapplication.LogicLayer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Random;

public class CrudRentOut {
    int rentId;
    String id;
    String name;
    String job;
    String pic;
    String rentStart;
    String rentEnd;
    String rank;
    String pay;

    public CrudRentOut(String id, String rentStart, String rentEnd){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.id = id;
        this.rentStart = rentStart;
        this.rentEnd = rentEnd;
        this.pic = "TEST";

        DatabaseReference myRefId = database.getReference(GlobalVariables.getFirebaseUser().getUid()+"/Medarbejdere/"+id);

        myRefId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("SNAPSHOT HERE "+snapshot.toString());
                    //Parse JSON
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(snapshot.getValue().toString());
                    JsonObject obj = element.getAsJsonObject();

                    name = obj.get("name").toString().replaceAll("\"","");
                    job = obj.get("job").toString().replaceAll("\"","");
                    //pic = obj.get("pic").toString().replaceAll("\"","");
                    rank = obj.get("rank").toString().replaceAll("\"","");
                    pay = obj.get("pay").toString().replaceAll("\"","");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error!");
            }
        });


        boolean[] unique = {false};
        while (!unique[0]) {
            int random = new Random().nextInt(9000) + 1000;
           DatabaseReference myRefMedarbejder = database.getReference(GlobalVariables.getFirebaseUser().getUid() + "/Medarbejdere");

            //Check for existing ID.
            myRefMedarbejder.child(String.valueOf(random)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        System.out.println("Unique ID");
                        unique[0] = true;
                    } else {
                        System.out.println("ID not Unique");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error!");
                    unique[0] = false;
                }
            });
            this.rentId = random;
            unique[0] = true;
        }
    }
    public int getRentId() {
        return rentId;
    }
}
