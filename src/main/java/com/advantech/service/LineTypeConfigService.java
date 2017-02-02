/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.LineTypeConfig;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.LineTypeConfigDAO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class LineTypeConfigService {

    private final LineTypeConfigDAO lineTypeConfigDAO;

    protected LineTypeConfigService() {
        lineTypeConfigDAO = new LineTypeConfigDAO();
    }

    public List<LineTypeConfig> getAll() {
        return lineTypeConfigDAO.getAll();
    }

    public LineTypeConfig getOne(int id) {
        return lineTypeConfigDAO.getOne(id);
    }

    public boolean insert(LineTypeConfig lineTypeConfig) {
        List<LineTypeConfig> l = new ArrayList();
        l.add(lineTypeConfig);
        return this.insert(l);
    }

    public boolean insert(List<LineTypeConfig> l) {
        return lineTypeConfigDAO.insert(l);
    }

    public boolean update(LineTypeConfig lineTypeConfig) {
        List<LineTypeConfig> l = new ArrayList();
        l.add(lineTypeConfig);
        return this.update(l);
    }

    public boolean update(List<LineTypeConfig> l) {
        return lineTypeConfigDAO.update(l);
    }

    public boolean updateValue(List<LineTypeConfig> l) {
        return lineTypeConfigDAO.updateValue(l);
    }

    public boolean delete(LineTypeConfig lineTypeConfig) {
        return lineTypeConfigDAO.delete(lineTypeConfig);
    }

    //將本地設定於options.properties的變數更新到SQL中，系統init時更新
    public void initBasicVariable() {
        String assy = "ASSY";
        String packing = "Packing";
        String test = "Test";
        String cell = "Cell";

        String balanceStandard = "LINEBALANCE_STANDARD";
        String alarmPercentStandard = "ALARM_PERCENT_STANDARD";
        String productivityStandardMin = "PRODUCTIVITY_STANDARD_MIN";
        String productivityStandardMax = "PRODUCTIVITY_STANDARD_MAX";

        PropertiesReader p = PropertiesReader.getInstance();

        List l = new ArrayList();

        l.add(new LineTypeConfig(assy, balanceStandard, p.getAssyStandard()));
        l.add(new LineTypeConfig(packing, balanceStandard, p.getPackingStandard()));

        l.add(new LineTypeConfig(assy, alarmPercentStandard, p.getAssyAlarmPercent()));
        l.add(new LineTypeConfig(packing, alarmPercentStandard, p.getPackingAlarmPercent()));

        l.add(new LineTypeConfig(test, productivityStandardMin, p.getTestStandardMin()));
        l.add(new LineTypeConfig(test, productivityStandardMax, p.getTestStandardMax()));

        l.add(new LineTypeConfig(cell, productivityStandardMin, p.getCellStandardMin()));
        l.add(new LineTypeConfig(cell, productivityStandardMax, p.getCellStandardMax()));

        this.updateValue(l);
    }

}
