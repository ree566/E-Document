package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.BAB;
import com.advantech.entity.BABHistory;
import com.advantech.entity.Line;
import com.advantech.helper.PropertiesReader;
import com.advantech.interfaces.AlarmActions;
import com.advantech.model.BABDAO;
import com.advantech.model.BABPreDAO;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
public class BABPreService {

    private final BABPreDAO babPreDAO;

    protected BABPreService() {
        babPreDAO = new BABPreDAO();
    }

    public List<BAB> findAll() {
        return babPreDAO.findAll();
    }

    public boolean insert(BAB bab) {
        return babPreDAO.insert(bab);
    }

    public boolean update(BAB bab) {
        return babPreDAO.update(bab);
    }

    public boolean delete(int id) {
        return babPreDAO.delete(id);
    }
}
