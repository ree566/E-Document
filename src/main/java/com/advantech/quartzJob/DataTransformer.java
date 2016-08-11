/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心class，把看板webservice傳出來的xml分解出來與測試table對照，看是否要亮燈
 * 以及分析組裝包裝，帶出目前統計值(計算邏輯寫在資料庫中，BABAVG這個table function)，看最高值亮燈
 */
package com.advantech.quartzJob;

import com.advantech.entity.AlarmAction;
import com.advantech.helper.PropertiesReader;
import com.advantech.helper.TxtWriter;
import com.advantech.helper.WebServiceRV;
import com.advantech.entity.BAB;
import com.advantech.entity.Test;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.advantech.service.LineBalanceService;
import com.advantech.service.TestService;
import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class DataTransformer {

    private static final Logger log = LoggerFactory.getLogger(DataTransformer.class);

    private static boolean controlJobFlag = true;

    private static DataTransformer instance;

    private final LineBalanceService lineBalanceService;
    private final TestService testService;
    private final BABService babService;
    private final LineBalancePeopleGenerator lbGenerator;

    private final WebServiceRV rv;
    private final TxtWriter txtWriter;

    private static final Map PEOPLE_MATCH = new HashMap(), PEOPLE_NOT_MATCH = new HashMap(), BAB_ISUSED = new HashMap();//占存資料用map

    private static JSONObject testJsonObj, babJsonObj;//暫存處理過的資料到
    private static final int ASSY_PEOPLES = 4, PACKING_PEOPLES = 3;

    private final int maxTestTable;

    //Don't repeatedly reset txt when not exist any alarm sign.
    private static boolean testResetFlag, babResetFlag, isWriteToTxt, isWriteToDB;

    private final int ALARM_SIGN = 1, NORMAL_SIGN = 0;
    private final int TEST_USER_NOT_IN_SYSTEM_SIGN = -1, TEST_USER_NOT_IN_XML_SIGN = 2;

    private final String testTxtName, babTxtName;

    private final double TEST_STANDARD, BAB_STANDARD;

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

    private DataTransformer() {
        PropertiesReader p = PropertiesReader.getInstance();
        testTxtName = p.getTestTxtName();
        babTxtName = p.getBabTxtName();
        babResetFlag = true;
        testResetFlag = true;
        TEST_STANDARD = p.getTestStandard();
        BAB_STANDARD = p.getBabStandard();
        maxTestTable = p.getMaxTestTable();
        rv = WebServiceRV.getInstance();
        txtWriter = TxtWriter.getInstance();
        lineBalanceService = BasicService.getLineBalanceService();
        babService = BasicService.getBabService();
        testService = BasicService.getTestService();
        lbGenerator = LineBalancePeopleGenerator.getInstance();
        isWriteToTxt = p.isWriteToTxt();
        isWriteToDB = p.isWriteToDB();
    }

    public static DataTransformer getInstance() {
        if (instance == null) {
            instance = new DataTransformer();
        }
        return instance;
    }

    public void doDailyJob() {
        //分開以免一個exception全部被job都進入catch
        checkAndSetTestTxt();
        checkAndSetBabTxt();
//        lbGenerator.generateTestPeople();
    }

    private void checkAndSetTestTxt() {
        try {
            saveToTxt(testTxtName);
        } catch (Exception ex) {
            log.error(testTxtName + " " + ex.toString());
        }
    }

    private void checkAndSetBabTxt() {
        try {
            saveToTxt(babTxtName);
        } catch (Exception ex) {
            log.error(babTxtName + " " + ex.toString());
        }
    }

    private void saveToTxt(String txtName) throws Exception {
        boolean isDocNameTest = testTxtName.equals(txtName);
        boolean isNeedToOutputResult = isDocNameTest ? matchingTestData() : matchingBABData1();

        if (controlJobFlag == true) {
            if (isNeedToOutputResult) {
                outputResult(isDocNameTest ? PEOPLE_MATCH : BAB_ISUSED, txtName);
            } else {
                resetOutputResult(txtName);
            }
        }
    }

    private void outputResult(Map m, String txtName) throws IOException {
        if (isWriteToTxt) {
            txtWriter.writeTxtWithFileName(m, txtName);
        }
        if (isWriteToDB) {
            saveTestAlarm(m, txtName);
        }
        if (isWriteToTxt || isWriteToDB) {
            changeFlagStatus(txtName);
        }
    }

    private void changeFlagStatus(String txtName) throws IOException {
        if (testTxtName.equals(txtName)) {
            if (testResetFlag == false) {
                testResetFlag = true;
            }
        } else if (babResetFlag == false) {
            babResetFlag = true;
        }
    }

    private void resetOutputResult(String txtName) throws IOException {
        if (isWriteToTxt || isWriteToDB) {
            if (testTxtName.equals(txtName)) {
                if (testResetFlag == true) {
                    if (isWriteToTxt) {
                        initTestMap();
                        outputResult(PEOPLE_MATCH, txtName);
                    }
                    if (isWriteToDB) {
                        babService.resetTestAlarm();
                    }
                    testResetFlag = false;
                }
            } else if (babResetFlag == true) {
                if (isWriteToTxt) {
                    initBabMap();
                    outputResult(BAB_ISUSED, txtName);
                }
                if (isWriteToDB) {
                    babService.resetBABAlarm();
                }
                babResetFlag = false;
            }
        }
    }

    public boolean saveTestAlarm(Map map, String txtName) throws IOException {
        if (map == null || map.isEmpty()) {
            return false;
        }
        List l = new ArrayList();
        Iterator it = map.keySet().iterator();

        while (it.hasNext()) {
            Object key = it.next();
            String tableId = key.toString();
            int action = (int) map.get(key);
            l.add(new AlarmAction(tableId, action));
        }

        return testTxtName.equals(txtName) ? babService.updateTestAlarm(l) : babService.updateBABAlarm(l);
    }

    private void initTestMap() {
        PEOPLE_MATCH.clear();
        PEOPLE_NOT_MATCH.clear();
        for (int i = 1; i <= maxTestTable; i++) {
            PEOPLE_MATCH.put(("T" + i), NORMAL_SIGN);
        }
    }

    private void initBabMap() {
        BAB_ISUSED.clear();
        for (Line line : Line.values()) {
            for (int i = 1, length = line.getTypeNumber(); i <= length; i++) {
                BAB_ISUSED.put(line + "-L-" + i, NORMAL_SIGN);
            }
        }
    }

    private boolean matchingTestData() throws JSONException {
        boolean isSomeoneUnderStandard = false;
        List<Test> tables = testService.getAllTableInfo();
        if (hasDataInCollection(tables)) {
            initTestMap();
            JSONArray userArr = new JSONArray();
            JSONArray kanbanUsers;

            kanbanUsers = rv.getKanbantestUsers();

            testJsonObj = new JSONObject();
            boolean isInTheWebService = false;
            for (int a = 0, length = kanbanUsers.length(); a < length; a++) {
                JSONObject user = kanbanUsers.getJSONObject(a);
                Double PRODUCTIVITY = user.getDouble("PRODUCTIVITY");
                String no = user.getString("USER_NO");
                String name = user.getString("USER_NAME");
                for (Iterator it = tables.iterator(); it.hasNext();) {
                    Test ti = (Test) it.next();
                    if (ti.getUserid().trim().equals(no)) {
                        int tableid = ti.getTableNum();
                        
                        JSONObject fitUser = new JSONObject();
                        fitUser.put("name", name)
                                .put("number", no)
                                .put("table", tableid)
                                .put("sitefloor", ti.getSitefloor())
                                .put("PRODUCTIVITY", PRODUCTIVITY);
                        if (PRODUCTIVITY < TEST_STANDARD) {
                            fitUser.put("isalarm", ALARM_SIGN);
                            PEOPLE_MATCH.put(tableid, ALARM_SIGN);
                            isSomeoneUnderStandard = true;
                        } else {
                            fitUser.put("isalarm", NORMAL_SIGN);
                        }
                        userArr.put(fitUser);
                        it.remove();
                        isInTheWebService = true;
                        break;
                    }
                }
                if (isInTheWebService) {
                    isInTheWebService = false;
                } else {
                    PEOPLE_NOT_MATCH.put(name, TEST_USER_NOT_IN_SYSTEM_SIGN); //沒核對到資料庫的人員傳回m2給前端
                }
            }

            userArr = separateAbnormalUser(tables, userArr);

// test alarm status
//            userArr = putTestTable(userArr);
//            isSomeoneUnderStandard = true;
//
            testJsonObj.put("data", userArr);
        } else {
            testJsonObj = null;
        }
        return isSomeoneUnderStandard;
    }

    private JSONArray separateAbnormalUser(List<Test> l, JSONArray j) {
        for (Test ti : l) {//邊對邊拿掉collection的data，剩下的就是沒刷入系統的人
            JSONObject abnormalUser = new JSONObject();
            abnormalUser.put("name", "n/a")
                    .put("number", ti.getUserid())
                    .put("table", ti.getTableNum())
                    .put("PRODUCTIVITY", "0.0")
                    .put("isalarm", TEST_USER_NOT_IN_XML_SIGN);
            j.put(abnormalUser);
        }
        return j;
    }

    //測試亮燈method
    private JSONArray putTestTable(JSONArray j) {
        PEOPLE_MATCH.put("T" + 24, ALARM_SIGN);
        return j.put(
                new JSONObject()
                .put("name", "test")
                .put("number", "test")
                .put("table", 24)
                .put("PRODUCTIVITY", "0.0")
                .put("isalarm", ALARM_SIGN)
        );
    }

    //組測亮燈邏輯type 1
    private boolean matchingBABData() throws JSONException {
        boolean isSomeBabUnderStandard = false;
        List<BAB> babGroups = babService.getBABIdForCaculate();
        if (hasDataInCollection(babGroups)) {
            //把所有有在bab資料表的id集合起來，找到統計值之後依序寫入txt(各線別取當日最早輸入的工單id來亮燈)
            JSONArray transBabData = new JSONArray();
            babJsonObj = new JSONObject();
            for (BAB bab : babGroups) {
                JSONArray sensorDatas = babService.getAvg(bab.getId());
                int peoples = sensorDatas.length();
                if (peoples == 0) {
                    continue;
                }

                double maxTimeDiff = -1;
                double sumTimeDiff = -1;
                int maxTimeIndex = -1;
                for (int i = 0, length = sensorDatas.length(); i < length; i++) {//for loop find the maxium number
                    //jsonObject的getInt所用參數不一樣，假使是取單台要getInt(diff)，整套則是getInt(average)
                    double timeDiff = sensorDatas.getJSONObject(i).getInt("average");
                    if (maxTimeDiff < timeDiff) {
                        maxTimeDiff = timeDiff;
                        maxTimeIndex = i;
                    }
                    sumTimeDiff += timeDiff;
                }

                boolean isUnderBalance = (lineBalanceService.caculateLineBalance(maxTimeDiff, sumTimeDiff, peoples) < BAB_STANDARD);

                if (isUnderBalance) {
                    isSomeBabUnderStandard = true;
                }
                for (int i = 0, length = sensorDatas.length(); i < length; i++) {
                    transBabData.put(sensorDatas.getJSONObject(i).put("ismax", isUnderBalance ? (maxTimeIndex == i) : false));
                }
            }
            babJsonObj.put("data", transBabData);
        } else {
            babJsonObj = null;//drop the data if no data in the database
        }
        if (isSomeBabUnderStandard) {
            babDataToMap(babJsonObj);
        }
        return isSomeBabUnderStandard;
    }

    //組測亮燈邏輯type 2(目前使用)
    private boolean matchingBABData1() throws Exception {
        boolean isSomeBabUnderStandard = false;
        List<BAB> babGroups = babService.getBABIdForCaculate();
        if (hasDataInCollection(babGroups)) {
            //把所有有在bab資料表的id集合起來，找到統計值之後依序寫入txt(各線別取當日最早輸入的工單id來亮燈)
            JSONArray transBabData = new JSONArray();
            babJsonObj = new JSONObject();

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
            babJsonObj.put("data", transBabData);
        } else {
            babJsonObj = null;//drop the data if no data in the database
        }
        if (isSomeBabUnderStandard) {
            babDataToMap(babJsonObj);
        }
        return isSomeBabUnderStandard;
    }

    private void babDataToMap(JSONObject avgs) {
        if (avgs != null) {
            StringBuilder alarmSensor = new StringBuilder(50);
            JSONArray sensorDatas = avgs.getJSONArray("data");
            if (sensorDatas.length() != 0) {
                initBabMap();
                for (int i = 0, length = sensorDatas.length(); i < length; i++) {
                    JSONObject sensorData = sensorDatas.getJSONObject(i);
                    if (sensorData.getBoolean("ismax")) {
                        alarmSensor.append(sensorData.getString("TagName"));
                        alarmSensor.append("-L-");
                        alarmSensor.append(sensorData.getInt("T_Num"));
                        BAB_ISUSED.put(alarmSensor.toString(), ALARM_SIGN); //0的資料不覆蓋節省效率
                        alarmSensor.setLength(0);
                    }
                }
            }
        }
    }

    //為了給前端資料
    public static Map getPeopleNotMatch() {
        return PEOPLE_NOT_MATCH;
    }

    public static JSONObject getTestJsonObj() {
        return testJsonObj;
    }

    public static JSONObject getBabJsonObj() {
        return babJsonObj;
    }

    public static void setWriteTxtActionContorlSign(boolean controlJobFlag) {
        DataTransformer.controlJobFlag = controlJobFlag;
    }

    public static void initInnerObjs() {
        PEOPLE_MATCH.clear();
        PEOPLE_NOT_MATCH.clear();
        BAB_ISUSED.clear();
        testJsonObj = null;
        babJsonObj = null;
    }

    private boolean hasDataInCollection(Collection c) {
        return c != null && !c.isEmpty();
    }
}
