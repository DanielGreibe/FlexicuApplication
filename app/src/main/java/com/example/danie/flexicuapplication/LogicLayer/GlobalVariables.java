package com.example.danie.flexicuapplication.LogicLayer;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class GlobalVariables extends Application {
    private static com.google.firebase.auth.FirebaseUser FirebaseUser;

    public static com.google.firebase.auth.FirebaseUser getFirebaseUser() {
        return FirebaseUser;
    }

    public void setFirebaseUser(com.google.firebase.auth.FirebaseUser firebaseUser) {
        this.FirebaseUser = firebaseUser;
    }
}
