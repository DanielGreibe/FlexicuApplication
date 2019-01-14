package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDemo {
    public void start() {


    }


    public static void printPeeps(List<CrudEmployee> peeps){
        for(CrudEmployee employee : peeps){
            System.out.println(employee.getName() + " - " + employee.getPay() + " - " +  employee.getJob());
        }

    }



}
