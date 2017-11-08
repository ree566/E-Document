/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.AlarmAction;
import com.advantech.model.Bab;
import com.advantech.model.Line;
import com.advantech.helper.PropertiesReader;
import java.util.List;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
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
public class BabLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(BabLineTypeFacade.class);

    @Autowired
    private LineBalanceService lineBalanceService;
    
    @Autowired
    private BabService babService;
    
    @Autowired
    private LineService lineService;

    private double BAB_STANDARD;

    @PostConstruct
    protected void init() {
        PropertiesReader p = PropertiesReader.getInstance();
        BAB_STANDARD = p.getAssyStandard();
        
        this.initMap();
    }

    @Override
    protected void initMap() {
        dataMap.clear();
        List<Line> babLineStatus = lineService.getLine();
        for (Line line : babLineStatus) {
            String lineName = line.getName().trim();
            for (int i = 1, length = line.getPeople(); i <= length; i++) {
                dataMap.put(lineName + "-L-" + i, super.NORMAL_SIGN);
            }
        }
    }

    //組測亮燈邏輯type 2(目前使用)
    @Override
    public boolean generateData() {
        boolean isSomeBabUnderStandard = false;

        List<Bab> babGroups = babService.getAllProcessing();
        if (hasDataInCollection(babGroups)) {
            //把所有有在bab資料表的id集合起來，找到統計值之後依序寫入txt(各線別取當日最早輸入的工單id來亮燈)
            JSONArray transBabData = new JSONArray();
            processingJsonObject = new JSONObject();

            for (Bab bab : babGroups) {
                int BABid = bab.getId();
                JSONArray sensorDatas = babService.getLastGroupStatusForJson(BABid);
                int currentGroupSum = sensorDatas.length();//看目前組別人數是否有到達bab裏頭設定的人數
                int peoples = bab.getPeople();
                if (sensorDatas.length() == 0 || currentGroupSum != peoples) {
                    
                    //Insert an empty status 
                    for (int i = bab.getStartPosition(), length = bab.getStartPosition() + bab.getPeople() - 1; i <= length; i++) {
                        JSONObject obj = new JSONObject();
                        obj.put("TagName", bab.getLineName());
                        obj.put("lineName", bab.getLineName());
                        obj.put("stationId", i - bab.getStartPosition() + 1);
                        obj.put("linetype", bab.getLinetype());
                        obj.put("T_Num", i);
                        transBabData.put(obj);
                    }
                    isSomeBabUnderStandard = true;
                    
                } else {
                    double maxTimeDiff = -1;
                    double sumTimeDiff = -1;
                    int dataindex = -1;
                    for (int i = 0, length = sensorDatas.length(); i < length; i++) {//for loop find the maxium number
                        //jsonObject的getInt所用參數不一樣，假使是取單台要getInt(diff)，整套則是getInt(average)
                        double timeDiff = sensorDatas.getJSONObject(i).getInt("diff");
                        if (maxTimeDiff < timeDiff) {
                            maxTimeDiff = timeDiff;
                            dataindex = i;
                        }
                        sumTimeDiff += timeDiff;
                    }

                    //Alarm by the last group lineBalance
                    double lineBalance = lineBalanceService.caculateLineBalance(maxTimeDiff, sumTimeDiff, peoples);

                    boolean isUnderBalance = (Double.compare(lineBalance, BAB_STANDARD) < 0);

                    if (isUnderBalance) {
                        isSomeBabUnderStandard = true;
                    }
                    for (int i = 0, length = sensorDatas.length(); i < length; i++) {
                        transBabData.put(sensorDatas.getJSONObject(i).put("ismax", isUnderBalance ? (dataindex == i) : false));
                    }
                }
            }
            processingJsonObject.put("data", transBabData);
        } else {
            processingJsonObject = null;//drop the data if no data in the database
        }
        if (isSomeBabUnderStandard) {
            babDataToMap(processingJsonObject);
        }
        return isSomeBabUnderStandard;
    }

    private void babDataToMap(JSONObject avgs) {
        if (avgs != null) {
            StringBuilder alarmSensor = new StringBuilder(50);
            JSONArray sensorDatas = avgs.getJSONArray("data");
            if (sensorDatas.length() != 0) {
                initMap();
                for (int i = 0, length = sensorDatas.length(); i < length; i++) {
                    JSONObject sensorData = sensorDatas.getJSONObject(i);
                    if (sensorData.has("ismax") && sensorData.getBoolean("ismax")) {
                        alarmSensor.append(sensorData.getString("TagName"));
                        alarmSensor.append("-L-");
                        alarmSensor.append(sensorData.getInt("T_Num"));
                        dataMap.put(alarmSensor.toString(), super.ALARM_SIGN); //0的資料不覆蓋節省效率
                        alarmSensor.setLength(0);
                    }
                }
            }
        }
    }

    @Override
    protected boolean initDbAlarmSign() {
        return babService.removeAlarmSign() && babService.insertAlarm(super.mapToAlarmSign(dataMap));
    }

    @Override
    public boolean setDbAlarmSignToTestMode() {
        return babService.setBABAlarmToTestingMode();
    }

    @Override
    protected boolean setDbAlarmSign(List<AlarmAction> l) {
        return babService.updateAlarm(l);
    }

    @Override
    protected boolean resetDbAlarmSign() {
        return babService.resetAlarm();
    }

}
