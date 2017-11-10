package com.advantech.service;

import com.advantech.model.Countermeasure;
import com.advantech.dao.CountermeasureDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class CountermeasureService {

    @Autowired
    private CountermeasureDAO countermeasureDAO;

    public List<Countermeasure> getCountermeasure() {
        return countermeasureDAO.getCountermeasures();
    }

    public List<Map> getCountermeasures() {
        return countermeasureDAO.getCountermeasuress();
    }

    public Countermeasure getCountermeasure(int BABid) {
        return countermeasureDAO.getCountermeasure(BABid);
    }

    public List<Map> getCountermeasureView() {
        return countermeasureDAO.getCountermeasureView();
    }

    public Map getCountermeasureView(int BABid) {
        List<Map> l = countermeasureDAO.getCountermeasureView(BABid);
        return l.isEmpty() ? null : l.get(0);
    }

    public List<Map> getUnFillCountermeasureBabs() {
        return countermeasureDAO.getUnFillCountermeasureBabs();
    }

    public List<Map> getUnFillCountermeasureBabs(String sitefloor) {
        return countermeasureDAO.getUnFillCountermeasureBabs(sitefloor);
    }

    //客製化EXCEL規格
    public List<Map> getCountermeasureForExcel(String startDate, String endDate) {
        return countermeasureDAO.getCountermeasureForExcel(startDate, endDate);
    }

    public List<Map> getPersonalAlmForExcel(String startDate, String endDate) {
        return countermeasureDAO.getPersonalAlmForExcel(startDate, endDate);
    }

    public List<Map> getCountermeasureAndPersonalAlm(String startDate, String endDate) {
        return countermeasureDAO.getCountermeasureAndPersonalAlm(startDate, endDate);
    }

    public List<Map> transformPersonalAlmDataPattern(List<Map> l) {
        List<Map> tList = new ArrayList();
        Map baseMap = null;
        int baseId = 0;
        String userIdFieldName = "USER_ID";
        String stationFieldName = "station";
        String failPercentFieldName = "failPercent(Personal)";
        String idFieldName = "id";
        String failPercentFieldNameCH = "亮燈頻率";

        for (int i = 0; i < l.size(); i++) {
            Map m = l.get(i);
            if (i == 0) {
                baseMap = m;
                baseId = (int) m.get(idFieldName);
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
            } else if ((int) m.get("id") != baseId) {
                tList.add(baseMap);
                baseMap = m;
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
                baseId = (int) m.get(idFieldName);
            } else if (baseMap != null && (int) m.get(idFieldName) == baseId) {
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                if (i == (l.size() - 1)) {
                    tList.add(baseMap);
                }
            }
        }
        return tList;
    }

    private Map removeUnusedKeyInMap(Map m, String... keys) {
        for (String st : keys) {
            m.remove(st);
        }
        return m;
    }
    //--------效率報表------------------------------

    public List<Map> transformEfficiencyReportPattern(List<Map> l) {
        List<Map> tList = new ArrayList();
        Map baseMap = null;
        int baseId = 0;
        String userIdFieldName = "USER_ID";
        String stationFieldName = "station";
        String failPercentFieldName = "failPcs";
        String idFieldName = "id";
        String failPercentFieldNameCH = "亮燈次數";

        for (int i = 0; i < l.size(); i++) {
            Map m = l.get(i);
            if (i == 0) {
                baseMap = m;
                baseId = (int) m.get(idFieldName);
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
            } else if ((int) m.get("id") != baseId) {
                tList.add(baseMap);
                baseMap = m;
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
                baseId = (int) m.get(idFieldName);
            } else if (baseMap != null && (int) m.get(idFieldName) == baseId) {
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                if (i == (l.size() - 1)) {
                    tList.add(baseMap);
                }
            }
        }
        return tList;
    }

    public List<Map> getEditor(int cm_id) {
        return countermeasureDAO.getEditor(cm_id);
    }

    public boolean insertCountermeasure(int BABid, List<String> errorCode_id, String solution, String editor) {
        return countermeasureDAO.insertCountermeasure(BABid, solution, errorCode_id, editor);
    }

    public boolean updateCountermeasure(int id, List<String> errorCode_id, String solution, String editor) {
        if (this.getCountermeasure(id) == null) {
            return this.insertCountermeasure(id, errorCode_id, solution, editor);
        } else {
            return countermeasureDAO.updateCountermeasure(id, solution, errorCode_id, editor);
        }
    }

    public boolean deleteCountermeasure(int id) {
        return countermeasureDAO.deleteCountermeasure(id);
    }

}
