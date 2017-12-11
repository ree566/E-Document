/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.CellLine;
import com.advantech.helper.PropertiesReader;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
//@Component
public class CellLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(CellLineTypeFacade.class);

    @Autowired
    private CellService cellService;

    @Autowired
    private CellLineService cellLineService;

    @Autowired
    private PassStationService passStationService;

    private Double cellStandardMin, cellStandardMax;

//    @PostConstruct
    protected void init() {
        PropertiesReader p = PropertiesReader.getInstance();
        cellStandardMin = p.getCellStandardMin();
        cellStandardMax = p.getCellStandardMax();

        this.initMap();
    }

    @Override
    protected void initMap() {
        this.dataMap.clear();
        List<CellLine> l = cellLineService.findAll();
        for (CellLine cellLine : l) {
            dataMap.put(cellLine.getOutputName(), super.NORMAL_SIGN);
        }
    }

    @Override
    protected boolean generateData() {
        List<Map> l = passStationService.getCellLastGroupStatusView();
        boolean isCellsUnderBalance = false;
        this.initMap();
        if (!l.isEmpty()) {
            processingJsonObject = new JSONObject();
            String percentKeyName = "percent";
            String lineNameKeyName = "linename";
            String outputLineNameKeyName = "outputLinename";
            for (Map m : l) {
                if (m.containsKey(percentKeyName) && m.containsKey(lineNameKeyName) && m.containsKey(outputLineNameKeyName)) {
                    String outputLineName = (String) m.get(outputLineNameKeyName);
                    Double percent = (Double) m.get(percentKeyName);

                    boolean isPass = isInTheRange(percent);
                    if (isPass) {
                        dataMap.put(outputLineName, this.NORMAL_SIGN);
                    } else {
                        dataMap.put(outputLineName, this.ALARM_SIGN);
                        isCellsUnderBalance = true;
                    }
                    m.put("isAlarm", !isPass);
                } else {
                    log.error("Can not find the spec key in map, need keys " + percentKeyName + " " + lineNameKeyName + " " + outputLineNameKeyName);
                    return false;
                }
            }
            processingJsonObject.put("data", l);
        } else {
            processingJsonObject = null;
        }
        return isCellsUnderBalance;
    }

    private boolean isInTheRange(Double percent) {
        return percent >= this.cellStandardMin && percent <= this.cellStandardMax;
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
