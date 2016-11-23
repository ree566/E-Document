/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.BAB;
import com.advantech.entity.Line;
import com.advantech.helper.PropertiesReader;
import com.advantech.quartzJob.LineBalancePeopleGenerator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BabLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(BabLineTypeFacade.class);

    private static BabLineTypeFacade instance;

    private final LineBalanceService lineBalanceService;
    private final BABService babService;

    private final double BAB_STANDARD;

    private BabLineTypeFacade() {
        lineBalanceService = BasicService.getLineBalanceService();
        babService = BasicService.getBabService();
        PropertiesReader p = PropertiesReader.getInstance();
        BAB_STANDARD = p.getBabStandard();
        this.init();
    }

    public static BabLineTypeFacade getInstance() {
        return instance == null ? new BabLineTypeFacade() : instance;
    }

    private void init() {
        this.initMap();
        if (isWriteToDB) {
            boolean initStatus = this.initDbAlarmSign();
            if (initStatus == false) {
                log.error("Init db output fail.");
            }
        }
    }

    @Override
    protected void initMap() {
        dataMap.clear();
        List<Line> babLineStatus = BasicService.getLineService().getLine();
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
        List<BAB> babGroups = babService.getBABIdForCaculate();
        if (hasDataInCollection(babGroups)) {
            //把所有有在bab資料表的id集合起來，找到統計值之後依序寫入txt(各線別取當日最早輸入的工單id來亮燈)
            JSONArray transBabData = new JSONArray();
            processingJsonObject = new JSONObject();

            for (BAB bab : babGroups) {
                int BABid = bab.getId();

                JSONArray sensorDatas = babService.getLastGroupStatusForJson(BABid);

                int peoples = sensorDatas.length();
                if (peoples == 0) {
                    continue;
                }

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
                    if (sensorData.getBoolean("ismax")) {
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
        BABService bs = BasicService.getBabService();
        return bs.removeAllAlarmSign() && bs.insertBABAlarm(super.mapToAlarmSign(dataMap));
    }

    @Override
    public boolean setDbAlarmSignToTestMode() {
        return babService.setBABAlarmToTestingMode();
    }

    @Override
    protected boolean setDbAlarmSign(List<AlarmAction> l) {
        return babService.updateBABAlarm(l);
    }

    @Override
    protected boolean resetDbAlarmSign() {
        return babService.resetBABAlarm();
    }

}
