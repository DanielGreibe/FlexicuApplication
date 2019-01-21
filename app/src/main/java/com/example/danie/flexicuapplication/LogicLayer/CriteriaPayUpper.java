package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaPayUpper implements CriteriaInterface {
    private double searchCriteria;

    public CriteriaPayUpper(double searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> employees) {

        List<CrudEmployee> payOver = new ArrayList<CrudEmployee>();

        for (CrudEmployee employee : employees) {
            if (employee.getPay() <= searchCriteria) {
                payOver.add(employee);
            }
        }
        return payOver;
    }
}
