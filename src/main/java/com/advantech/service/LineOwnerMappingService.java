/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.ActionCodeMapping;
import com.advantech.model.ActionCodeMappingDAO;
import com.advantech.model.LineOwnerMappingDAO;
import static java.lang.System.out;
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

    public List<Map> getLineOwnerMappingView() {
        return lineOwnerMappingDAO.getLineOwnerMappingView();
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

}
