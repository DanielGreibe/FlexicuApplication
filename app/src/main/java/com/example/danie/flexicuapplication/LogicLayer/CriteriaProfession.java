package com.example.danie.flexicuapplication.LogicLayer;


import java.util.ArrayList;
import java.util.List;

public class CriteriaProfession implements CriteriaInterface {
    private String searchCriteria;
    public CriteriaProfession(String searchCriteria)
    {
        this.searchCriteria = searchCriteria;
    }
    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {
        List<CrudEmployee> profession = new ArrayList<CrudEmployee>();

        for(CrudEmployee employee: persons) {
            if(employee.getJob().equals(searchCriteria)){
                profession.add(employee);
            }
        }
        return profession;
    }

}
