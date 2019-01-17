package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDemo {
    public void start() {
        List<CrudEmployee> persons = new ArrayList<>();

        persons.add(new CrudEmployee.EmployeBuilder("mathias").pay(140).builder());
        persons.add(new CrudEmployee.EmployeBuilder("fleix").pay(500).builder());
        persons.add(new CrudEmployee.EmployeBuilder("john").pay(45).builder());

        CriteriaInterface payy = new CriteriaPayLower(400);

        //Search by First and last name (And criteria)
        for(CrudEmployee person: persons)
        {
            System.out.println(payy.meetCriteria(persons));
        }

    }


    public static void printPeeps(List<CrudEmployee> peeps){
        for(CrudEmployee employee : peeps){
            System.out.println(employee.getName() + " - " + employee.getPay() + " - " +  employee.getJob());
        }

    }



}
