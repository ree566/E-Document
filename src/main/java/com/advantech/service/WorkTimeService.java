/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.WorkTimeDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class WorkTimeService {

    private final WorkTimeDAO workTimeDAO;

    protected WorkTimeService() {
        workTimeDAO = new WorkTimeDAO();
    }

    public Double getTestStandardTime(String modelName) {
        List<Map> l = workTimeDAO.getTestStandardTime(modelName);
        
        if (l.isEmpty()) {
            return null;
        }

        String columnName = "T1_Time";
        Map data = l.get(0);
        Double testStandard = (Double) data.get(columnName);
        return testStandard;
    }
}
