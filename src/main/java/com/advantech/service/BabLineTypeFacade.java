/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Bab;
import com.advantech.model.Line;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.AlarmBabAction;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.view.BabLastGroupStatus;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
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
    private LineBalancingService lineBalanceService;

    @Autowired
    private BabService babService;

    @Autowired
    private AlarmBabActionService almService;

    @Autowired
    private LineService lineService;

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    private double BAB_STANDARD;

    @PostConstruct
    protected void init() {
        PropertiesReader p = PropertiesReader.getInstance();
        BAB_STANDARD = p.getAssyStandard();
        this.initMap();
        this.initAlarmSign();
    }

    @Override
    protected void initMap() {
        dataMap.clear();
        List<Line> babLineStatus = lineService.findAll();
        babLineStatus.forEach((line) -> {
            String lineName = line.getName().trim();
            for (int i = 1, length = line.getPeople(); i <= length; i++) {
                dataMap.put(lineName + "-L-" + i, super.NORMAL_SIGN);
            }
        });
    }

    //組測亮燈邏輯type 2(目前使用)
    @Override
    public boolean generateData() {
        boolean isSomeBabUnderStandard = false;

        List<Bab> babGroups = babService.findProcessing();
        List<BabSettingHistory> allSettings = babSettingHistoryService.findProcessing();

        if (hasDataInCollection(allSettings) && hasDataInCollection(babGroups)) {
            //把所有有在bab資料表的id集合起來，找到統計值之後依序寫入txt(各線別取當日最早輸入的工單id來亮燈)
            JSONArray transBabData = new JSONArray();
            processingJsonObject = new JSONObject();

            for (Bab bab : babGroups) {
                List<BabSettingHistory> babSettings = allSettings.stream()
                        .filter(rec -> rec.getBab().getId() == bab.getId()).collect(toList());

                if (babSettings.isEmpty()) {
                    continue;
                }

                List<BabLastGroupStatus> status = sqlViewService.findBabLastGroupStatus(bab.getId());
                int currentGroupSum = status.size();//看目前組別人數是否有到達bab裏頭設定的人數
                int peoples = bab.getPeople();
                if (currentGroupSum == 0 || currentGroupSum != peoples) {
                    //Insert an empty status
                    //BabSettingHistory in allSettings are proxy object generate by hibernate
                    //Can't transform to json by google.Gson directly
                    babSettings.forEach((setting) -> {
                        JSONObject obj = new JSONObject();
                        obj.put("tagName", setting.getTagName().getName());
                        obj.put("station", setting.getStation());
                        transBabData.put(obj);
                    });

                    isSomeBabUnderStandard = true;

                } else {
                    double maxTimeDiff = -1;
                    double sumTimeDiff = -1;
                    int dataindex = -1;
                    for (int i = 0, length = currentGroupSum; i < length; i++) {//for loop find the maxium number
                        //jsonObject的getInt所用參數不一樣，假使是取單台要getInt(diff)，整套則是getInt(average)
                        Double timeDiff = status.get(i).getDiff() * 1.0;
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
                    for (int i = 0; i < currentGroupSum; i++) {
                        BabLastGroupStatus bgs = status.get(i);
                        bgs.setIsmax(isUnderBalance ? (dataindex == i) : false);
                        transBabData.put(new JSONObject(bgs));
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
            JSONArray sensorDatas = avgs.getJSONArray("data");
            if (sensorDatas.length() != 0) {
                initMap();
                for (int i = 0, length = sensorDatas.length(); i < length; i++) {
                    JSONObject sensorData = sensorDatas.getJSONObject(i);
                    if (sensorData.has("ismax") && sensorData.getBoolean("ismax")) {
                        String tagName = sensorData.getString("tagName");
                        tagName = tagName.replace("-S-", "-L-");
                        dataMap.put(tagName, super.ALARM_SIGN); //0的資料不覆蓋節省效率
                    }
                }
            }
        }
    }

    @Override
    public void initAlarmSign() {
        List l = almService.findAll();
        almService.delete(l);
        almService.insert(this.mapToAlarmSign(dataMap));
    }

    @Override
    public void setAlarmSign(List l) {
        almService.update(l);
    }

    @Override
    public void resetAlarmSign() {
        almService.reset();
    }

    @Override
    public void setAlarmSignToTestingMode() {
        almService.AlarmToTestingMode();
    }

    @Override
    protected List mapToAlarmSign(Map map) {
        List l = new ArrayList();
        if (map != null && !map.isEmpty()) {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                String tableId = key.toString();
                int action = (int) map.get(key);
                l.add(new AlarmBabAction(tableId, action));
            }
        }
        return l;
    }

}
