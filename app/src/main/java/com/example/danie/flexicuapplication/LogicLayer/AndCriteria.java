package com.example.danie.flexicuapplication.LogicLayer;

import android.location.Criteria;

import java.util.List;


public class AndCriteria implements CriteriaInterface {

    private CriteriaInterface criteria;
    private CriteriaInterface otherCriteria;
    private CriteriaInterface someOther;

    public AndCriteria(CriteriaInterface criteria, CriteriaInterface otherCriteria, CriteriaInterface someOther) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
        this.someOther = someOther;

    }

    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {

        List<CrudEmployee> firstCriteriaPersons = criteria.meetCriteria(persons);
        return otherCriteria.meetCriteria(firstCriteriaPersons);
    }
}