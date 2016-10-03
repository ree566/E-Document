package com.advantech.service;

import com.advantech.entity.Countermeasure;
import com.advantech.model.BasicDAO;
import com.advantech.model.CountermeasureDAO;
import com.google.gson.Gson;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
public class CountermeasureService {

    private final CountermeasureDAO countermeasureDAO;

    protected CountermeasureService() {
        countermeasureDAO = new CountermeasureDAO();
    }

    public List<Countermeasure> getCountermeasure() {
        return countermeasureDAO.getCountermeasures();
    }

    public Countermeasure getCountermeasure(int BABid) {
        return countermeasureDAO.getCountermeasure(BABid);
    }

    public List<Map> getCountermeasureView() {
        return countermeasureDAO.getCountermeasureView();
    }

    public List<Map> getUnFillCountermeasureBabs() {
        return countermeasureDAO.getUnFillCountermeasureBabs();
    }

    public List<Map> getUnFillCountermeasureBabs(String sitefloor) {
        return countermeasureDAO.getUnFillCountermeasureBabs(sitefloor);
    }

    public List<Map> getCountermeasureView(String lineType, String sitefloor, String startDate, String endDate) {
        return countermeasureDAO.getCountermeasureView(lineType, sitefloor, startDate, endDate);
    }

    public List<Map> getPersonalAlm(String lineType, String sitefloor, String startDate, String endDate) {
        List<Map> l = countermeasureDAO.getPersonalAlm(lineType, sitefloor, startDate, endDate);

//        out.print(l);
//        int babId = 0;
//
//        List<Map> finalData = new ArrayList();
//
//        Map map = null;
//        for (Map m : l) {
//            int id = (int) m.get("id");
//            if (id != 0) {
//                if (id != babId) {
//                    if (map != null) {
//                        finalData.add(map);
//                        map = null;
//                    } else {
//                        map = m;
//                        babId = id;
//                    }
//                } else if (map != null && id == babId) {
//                    map.putAll(m);
//                }
//            }
//        }
//
//        return finalData;
        return l;
    }

    public List<Map> getErrorCode() {
        return countermeasureDAO.getErrorCode();
    }

    public List<Map> getErrorCode(int cm_id) {
        return countermeasureDAO.getErrorCode(cm_id);
    }

    public List<Map> getActionCode() {
        return countermeasureDAO.getActionCode();
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

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit();
        List<Map> l = new CountermeasureService().getPersonalAlm("ASSY", "6", "16-09-01", "16-10-03");
        for (Map m : l) {
            out.println(new Gson().toJson(m));
        }
    }

}
