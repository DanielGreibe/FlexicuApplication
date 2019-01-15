package com.example.danie.flexicuapplication.LogicLayer;

import java.util.List;

public class OrCriteria implements CriteriaInterface {

    private CriteriaInterface criteria;
    private CriteriaInterface otherCriteria;

    public OrCriteria(CriteriaInterface criteria, CriteriaInterface otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<CrudEmployee> meetCriteria(List<CrudEmployee> persons) {
        List<CrudEmployee> firstCriteriaItems = criteria.meetCriteria(persons);
        List<CrudEmployee> otherCriteriaItems = otherCriteria.meetCriteria(persons);

        for (CrudEmployee person : otherCriteriaItems) {
            if(!firstCriteriaItems.contains(person)){
                firstCriteriaItems.add(person);
            }
        }
        return firstCriteriaItems;
    }
}