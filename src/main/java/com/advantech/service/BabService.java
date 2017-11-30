package com.advantech.service;

import com.advantech.model.AlarmAction;
import com.advantech.model.Bab;
import com.advantech.model.BabPcsDetailHistory;
import com.advantech.model.BabStatus;
import com.advantech.model.Line;
import com.advantech.helper.PropertiesReader;
import com.advantech.dao.BabDAO;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.LineStatus;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.google.common.base.Preconditions.*;

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
public class BabService {

    @Autowired
    private BabDAO babDAO;

    @Autowired
    private LineService lineService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private LineBalancingService lineBalanceService;

    @PostConstruct
    protected void BABService() {
    }

    public void BABDAO() {
        babDAO.BABDAO();
    }

    public List<Bab> findAll() {
        return babDAO.findAll();
    }

    public Bab findByPrimaryKey(Object obj_id) {
        return babDAO.findByPrimaryKey(obj_id);
    }

    public List<Bab> findByDate(DateTime sD, DateTime eD) {
        return babDAO.findByDate(sD, eD);
    }

    public List<Bab> findByModelAndDate(String modelName, DateTime sD, DateTime eD) {
        return babDAO.findByModelAndDate(modelName, sD, eD);
    }

    public List<Bab> findTodays() {
        DateTime d = new DateTime();
        return this.findByDate(d, d);
    }

    public List<Bab> findProcessing() {
        return babDAO.findProcessing();
    }

    public List<Bab> findProcessingByLine(int line_id) {
        return babDAO.findProcessingByLine(line_id);
    }

    public List<String> findAllModelName() {
        return babDAO.findAllModelName();
    }

    public int insert(Bab pojo) {
        return babDAO.insert(pojo);
    }

    public int update(Bab pojo) {
        return babDAO.update(pojo);
    }

    public int delete(Bab pojo) {
        return babDAO.delete(pojo);
    }
    
}
