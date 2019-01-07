package com.example.danie.flexicuapplication.LogicLayer;


import java.util.ArrayList;
import java.util.List;

public class CriteriaProfession implements CriteriaInterface {
    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {
        List<CrudEmployee> profession = new ArrayList<CrudEmployee>();

        for(CrudEmployee employee: persons) {
            if(employee.getJob().equals("elektro")){
                profession.add(employee);
            }
        }
        return profession;
    }


}
