package com.example.danie.flexicuapplication.LogicLayer;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class GlobalVariables extends Application {
    private static com.google.firebase.auth.FirebaseUser FirebaseUser;

    private String tempEmployeeDescription;
    private String tempEmployeeImage;
    private String tempEmployeePay;
    private String tempEmployeeProfession;
    private String tempEmployeeName;
    private String tempEmployeeYear;
    private String tempEmployeeZipcode;
    private String tempEmployeeDistance;

    public static com.google.firebase.auth.FirebaseUser getFirebaseUser()
    {
        return FirebaseUser;
    }

    public void setFirebaseUser(com.google.firebase.auth.FirebaseUser firebaseUser) {
        this.FirebaseUser = firebaseUser;
        }

    public String getTempEmployeeDescription()
        {
        return tempEmployeeDescription;
        }

    public void setTempEmployeeDescription(String tempEmployeeDescription)
        {
        this.tempEmployeeDescription = tempEmployeeDescription;
        }

    public String getTempEmployeeImage()
        {
        return tempEmployeeImage;
        }

    public void setTempEmployeeImage(String tempEmployeeImage)
        {
        this.tempEmployeeImage = tempEmployeeImage;
        }

    public String getTempEmployeePay()
        {
        return tempEmployeePay;
        }

    public void setTempEmployeePay(String tempEmployeePay)
        {
        this.tempEmployeePay = tempEmployeePay;
        }

    public String getTempEmployeeProfession()
        {
        return tempEmployeeProfession;
        }

    public void setTempEmployeeProfession(String tempEmployeeProfession)
        {
        this.tempEmployeeProfession = tempEmployeeProfession;
        }

    public String getTempEmployeeName()
        {
        return tempEmployeeName;
        }

    public void setTempEmployeeName(String tempEmployeeName)
        {
        this.tempEmployeeName = tempEmployeeName;
        }

    public String getTempEmployeeYear()
        {
        return tempEmployeeYear;
        }

    public void setTempEmployeeYear(String tempEmployeeYear)
        {
        this.tempEmployeeYear = tempEmployeeYear;
        }

    public String getTempEmployeeZipcode()
        {
        return tempEmployeeZipcode;
        }

    public void setTempEmployeeZipcode(String tempEmployeeZipcode)
        {
        this.tempEmployeeZipcode = tempEmployeeZipcode;
        }

    public String getTempEmployeeDistance()
        {
        return tempEmployeeDistance;
        }

    public void setTempEmployeeDistance(String tempEmployeeDistance)
        {
        this.tempEmployeeDistance = tempEmployeeDistance;
        }
    }
