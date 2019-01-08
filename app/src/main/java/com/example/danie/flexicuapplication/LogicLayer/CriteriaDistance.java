package com.example.danie.flexicuapplication.LogicLayer;

import java.util.ArrayList;
import java.util.List;

public class CriteriaDistance implements CriteriaInterface{

    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {
        List<CrudEmployee> distance = new ArrayList<CrudEmployee>();

        for(CrudEmployee employee : persons){
            if(employee.getDist() < 15){
                distance.add(employee);
            }
        }
        return distance;
    }
}
