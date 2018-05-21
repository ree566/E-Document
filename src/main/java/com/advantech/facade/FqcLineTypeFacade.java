/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.facade;

import com.advantech.model.Fqc;
import com.advantech.model.FqcLine;
import com.advantech.model.FqcSettingHistory;
import com.advantech.model.PassStationRecord;
import com.advantech.service.FqcLineService;
import com.advantech.service.FqcSettingHistoryService;
import com.advantech.webservice.WebServiceRV;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class FqcLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(FqcLineTypeFacade.class);

    @Autowired
    private FqcLineService fqcLineService;

    @Autowired
    private FqcSettingHistoryService fqcSettingHistoryService;

    @Autowired
    private WebServiceRV rv;

    @PostConstruct
    protected void init() {
//        this.initMap();
    }

    @Override
    protected void initMap() {
        this.dataMap.clear();
        List<FqcLine> l = fqcLineService.findAll();
        l.forEach((line) -> {
            dataMap.put(line.getName(), super.NORMAL_SIGN);
        });
    }

    /*
        Find all processing fqc with user info
        Get webService FQC_RI count and calc user's productivity
     */
    @Override
    protected boolean generateData() {
        processingJsonObject = new JSONObject();

        List<FqcSettingHistory> l = fqcSettingHistoryService.findProcessing();

        List result = new ArrayList();

        l.forEach((history) -> {
            String jobnumber = history.getJobnumber();
            Fqc fqc = history.getFqc();
            String po = fqc.getPo();
            List<PassStationRecord> records = rv.getPassStationRecords(po);
            records.removeIf(p -> !jobnumber.equals(p.getUserNo()));

            //currentPcs除了0以外對公式無影響
            int currentPcs = records.size();
            
            double productivity = 0.0;
            int standardTime = 150;

            if (currentPcs != 0) {
                productivity = calculateProductivity(standardTime, fqc.getFirstPcsTimeCost(), currentPcs);
            }

            Map m = new HashMap();
            m.put("history", history);
            m.put("productivity", productivity);
            m.put("currentPcs", currentPcs);
            m.put("standardTime", standardTime);
            result.add(m);
            
        });
        processingJsonObject.put("data", result);
        return false;

    }

    /*
            檢驗效率 = 標準工時 * N台 / (結束 - 開始) * N台
            N台 = 幾個FQC RI
     */
    private Double calculateProductivity(int standardTime, int firstPcsTimeCost, int currentPcs) {
        Double producitvity = ((double) standardTime * (double) currentPcs) / ((double) firstPcsTimeCost * (double) currentPcs);
        return producitvity;
    }

    @Override
    public void initAlarmSign() {
//        List l = almService.findAll();
//        almService.delete(l);
//        almService.insert(this.mapToAlarmSign(dataMap));
    }

    @Override
    public void setAlarmSign(List l) {
//        almService.update(l);
    }

    @Override
    public void resetAlarmSign() {
//        almService.reset();
    }

    @Override
    public void setAlarmSignToTestingMode() {
//        almService.AlarmToTestingMode();
    }

    @Override
    protected List mapToAlarmSign(Map map) {
        return null;
    }

}
