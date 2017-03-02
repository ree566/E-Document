/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.LineOwnerMapping;
import com.advantech.model.BasicDAO;
import com.advantech.model.LineOwnerMappingDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class LineOwnerMappingService {

    private final LineOwnerMappingDAO lineOwnerMappingDAO;

    protected LineOwnerMappingService() {
        lineOwnerMappingDAO = new LineOwnerMappingDAO();
    }

    public List<LineOwnerMapping> getAll() {
        return lineOwnerMappingDAO.getAll();
    }

    public LineOwnerMapping getOne(int id) {
        List<LineOwnerMapping> l = lineOwnerMappingDAO.getOne(id);
        return l.isEmpty() ? null : l.get(0);
    }

    public List<LineOwnerMapping> getByLine(int lineId) {
        return lineOwnerMappingDAO.getByLine(lineId);
    }

    public List<Map> getLineOwnerMappingView() {
        return lineOwnerMappingDAO.getLineOwnerMappingView();
    }

    public List<Map> getLineNotSetting() {
        return lineOwnerMappingDAO.getLineNotSetting();
    }

    public JSONObject getSeparateLineOwnerMapping() {
        return separateData(this.getLineOwnerMappingView(), "lineName");
    }

    public List<Map> getResponsorPerSitefloorView() {
        return lineOwnerMappingDAO.getResponsorPerSitefloorView();
    }

    public JSONObject getSeparateResponsorPerSitefloor() {
        return separateData(this.getResponsorPerSitefloorView(), "sitefloor");
    }

    private JSONObject separateData(List<Map> ownerMappingList, String separateKey) {
        Map<Object, List> hashMap = new HashMap();
        for (Map m : ownerMappingList) {
            Object lineName = m.get(separateKey) instanceof String ? ((String) m.get(separateKey)).trim() : m.get(separateKey);
            Object userName = m.get("user_name");
            if (!hashMap.containsKey(lineName)) {
                List list = new ArrayList();
                list.add(userName);
                hashMap.put(lineName, list);
            } else {
                hashMap.get(lineName).add(userName);
            }
        }
        return new JSONObject(hashMap);
    }

    public static void main(String arg0[]) {
        BasicDAO.dataSourceInit1();
        JSONObject responsorPerLine = BasicService.getLineOwnerMappingService().getSeparateLineOwnerMapping();
        
        System.out.println(responsorPerLine);
    }

}
