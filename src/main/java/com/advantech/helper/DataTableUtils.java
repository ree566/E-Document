/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class DataTableUtils {

    public static String toDataTableResponse(List l) {
        JSONObject obj = new JSONObject();

        if (l == null || l.isEmpty()) {
            obj.put("data", new ArrayList());
        } else {
            obj.put("data", l);
        }

        return obj.toString();
    }

    public static String toDataTableResponse(JSONObject o) {
        JSONObject obj = new JSONObject();

        if (o == null) {
            obj.put("data", new JSONObject());
        } else {
            obj.put("data", o);
        }

        return obj.toString();
    }
}
