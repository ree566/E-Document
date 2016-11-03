/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.BasicDAO;
import com.advantech.service.BasicService;
import com.google.gson.Gson;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class TestClass {

    public static void main(String args[]) throws Exception {
        BasicDAO.dataSourceInit1();
        List<Map> l = BasicService.getLineOwnerMappingService().getLineOwnerMappingView();
        separateData(l);
    }

    private static void separateData(List<Map> ownerMappingList) {
        Map<String, List<Map>> hashMap = new HashMap();
        for (Map m : ownerMappingList) {
            String lineName = (String) m.get("lineName");
            if (!hashMap.containsKey(lineName)) {
                List<Map> list = new ArrayList();
                list.add(m);
                hashMap.put(lineName, list);
            } else {
                hashMap.get(lineName).add(m);
            }
        }
        out.println(new Gson().toJson(hashMap));
    }
}
