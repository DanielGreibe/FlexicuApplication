package com.example.danie.flexicuapplication;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class GlobalVariables extends Application {
    private com.google.firebase.auth.FirebaseUser FirebaseUser;

    public com.google.firebase.auth.FirebaseUser getFirebaseUser() {
        return FirebaseUser;
    }

    public void setFirebaseUser(com.google.firebase.auth.FirebaseUser firebaseUser) {
        this.FirebaseUser = firebaseUser;
    }
}
