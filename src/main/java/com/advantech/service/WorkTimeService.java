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
        return this.getStandartTime(modelName, "T1_Time");
    }

    public Double getAssyStandardTime(String modelName) {
        return this.getStandartTime(modelName, "ASSY_Time");
    }

    private Double getStandartTime(String modelName, String columnName) {
        List<Map> l = this.getWorkTimePerModelView(modelName);
        if (!l.isEmpty()) {
            Map data = l.get(0);
            Double standardTime = data.containsKey(columnName) ? (Double) data.get(columnName) : null;
            return standardTime;
        } else {
            return null;
        }
    }

    public List<Map> getWorkTimePerModelView(String modelName) {
        return workTimeDAO.getWorkTimePerModelView(modelName);
    }
}
