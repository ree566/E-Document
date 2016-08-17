/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.BAB;
import com.advantech.helper.PropertiesReader;
import com.advantech.quartzJob.LineBalancePeopleGenerator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class BabLineTypeFacade extends BasicLineTypeFacade {

    private static BabLineTypeFacade instance;

    private final LineBalanceService lineBalanceService;
    private final BABService babService;
    private final LineBalancePeopleGenerator lbGenerator;

    private static final int ASSY_PEOPLES = 4, PACKING_PEOPLES = 3;

    private final double BAB_STANDARD;

    //設定線別人數
    private enum Line {

        L1(ASSY_PEOPLES),
        LA(ASSY_PEOPLES),
        LB(ASSY_PEOPLES),
        LF(PACKING_PEOPLES),
        LG(PACKING_PEOPLES),
        LH(PACKING_PEOPLES);

        Line(int type) {
            this.type = type;
        }
        private final int type;

        public int getTypeNumber() {
            return type;
        }
    }

    private BabLineTypeFacade() {
        lineBalanceService = BasicService.getLineBalanceService();
        babService = BasicService.getBabService();
        lbGenerator = LineBalancePeopleGenerator.getInstance();

        PropertiesReader p = PropertiesReader.getInstance();
        BAB_STANDARD = p.getBabStandard();
        super.setTxtName(p.getBabTxtName());

    }

    public static BabLineTypeFacade getInstance() {
        if (instance == null) {
            instance = new BabLineTypeFacade();
        }
        return instance;
    }

    @Override
    protected void initMap() {
        dataMap.clear();
        for (Line line : Line.values()) {
            for (int i = 1, length = line.getTypeNumber(); i <= length; i++) {
                dataMap.put(line + "-L-" + i, super.NORMAL_SIGN);
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

                JSONArray sensorDatas = babService.getLastGroupStatus(BABid);

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
    protected boolean setAlarmSignToDb(List<AlarmAction> l) {
        return babService.updateBABAlarm(l);
    }

    @Override
    protected boolean resetAlarmSignToDb() {
        return babService.resetBABAlarm();
    }

}
