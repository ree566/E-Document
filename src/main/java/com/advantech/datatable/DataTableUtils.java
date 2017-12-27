/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.datatable;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class DataTableUtils {

    public static DataTableResponse toDataTableResponse(List l) {
        return new DataTableResponse(l == null || l.isEmpty() ? new ArrayList() : l);
    }

    public static DataTableResponse toDataTableResponse(JSONObject o) {
        return new DataTableResponse(o == null ? new JSONObject() : o);
    }

    public static DataTableResponse toDataTableResponse(JSONArray o) {
        return new DataTableResponse(o == null ? new JSONArray() : o);
    }
}
