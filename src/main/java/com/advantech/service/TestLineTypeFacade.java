/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.Test;
import com.advantech.entity.TestLineTypeUser;
import com.advantech.helper.PropertiesReader;
import com.advantech.helper.WebServiceRV;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class TestLineTypeFacade extends BasicLineTypeFacade {

    private static TestLineTypeFacade instance;

    private final Map PEOPLE_NOT_MATCH = new HashMap();

    private final int maxTestTable;
    private final double TEST_STANDARD;

    private final WebServiceRV rv;
    private final TestService testService;

    private final int TEST_USER_NOT_IN_SYSTEM_SIGN = -1, TEST_USER_NOT_IN_XML_SIGN = 2;

    public TestLineTypeFacade() {
        PropertiesReader p = PropertiesReader.getInstance();
        maxTestTable = p.getMaxTestTable();
        TEST_STANDARD = p.getTestStandard();

        rv = WebServiceRV.getInstance();
        testService = BasicService.getTestService();
        super.setTxtName(p.getTestTxtName());
    }

    public static TestLineTypeFacade getInstance() {
        if (instance == null) {
            instance = new TestLineTypeFacade();
        }
        return instance;
    }

    @Override
    protected void initMap() {
        super.dataMap.clear();
        PEOPLE_NOT_MATCH.clear();
        for (int i = 1; i <= maxTestTable; i++) {
            dataMap.put(("T" + i), NORMAL_SIGN);
        }
    }

    @Override
    protected boolean generateData() {
        boolean isSomeoneUnderStandard = false;
        List<Test> tables = testService.getAllTableInfo();
        if (hasDataInCollection(tables)) {
            initMap();
            JSONArray userArr = new JSONArray();

            List<TestLineTypeUser> kanbanUsers = rv.getKanbantestUsers();

            processingJsonObject = new JSONObject();
            boolean isInTheWebService = false;

            for (TestLineTypeUser user : kanbanUsers) {

                String jobnumber = user.getUserNo();
                String userName = user.getUserName();
                Double productivity = user.getProductivity();

                for (Iterator it = tables.iterator(); it.hasNext();) {
                    Test ti = (Test) it.next();
                    if (ti.getUserid().trim().equals(jobnumber)) {
                        int tableNo = ti.getTableNum();
                        int status;

                        if (productivity < TEST_STANDARD) {
                            status = ALARM_SIGN;
                            dataMap.put(tableNo, ALARM_SIGN);
                            isSomeoneUnderStandard = true;
                        } else {
                            status = NORMAL_SIGN;
                        }

                        userArr.put(newTestUser(userName, jobnumber, tableNo, productivity, ti.getSitefloor(), status));
                        it.remove();//把比對過的資料移除，剩下的就是有在本系統XML卻找不到人的使用者
                        isInTheWebService = true;//對到人之後跳出迴圈，換下一個人做比對
                        break;
                    }
                }
                if (isInTheWebService) {
                    isInTheWebService = false;
                } else {
                    PEOPLE_NOT_MATCH.put(userName, TEST_USER_NOT_IN_SYSTEM_SIGN); //沒核對到資料庫的人員傳回m2給前端
                }
            }
            userArr = separateAbnormalUser(tables, userArr);//把剩下的人以異常訊號回報給前端
            processingJsonObject.put("data", userArr);
        } else {
            processingJsonObject = null;
        }
        return isSomeoneUnderStandard;
    }

    private JSONArray separateAbnormalUser(List<Test> l, JSONArray j) {
        String emptyUserName = "n/a";
        Double emptyProductivity = 0.0;
        for (Test ti : l) {
            j.put(newTestUser(emptyUserName, ti.getUserid(), ti.getTableNum(), emptyProductivity, ti.getSitefloor(), TEST_USER_NOT_IN_XML_SIGN));
        }
        return j;
    }

    private JSONObject newTestUser(String name, String jobnumber, int tableNo, Double productivity, String sitefloor, int status) {
        return new JSONObject()
                .put("name", name)
                .put("number", jobnumber)
                .put("table", tableNo)
                .put("PRODUCTIVITY", productivity)
                .put("sitefloor", sitefloor)
                .put("isalarm", status);
    }

    @Override
    protected boolean setAlarmSignToDb(List<AlarmAction> l) {
        return testService.updateTestAlarm(l);
    }

    @Override
    protected boolean resetAlarmSignToDb() {
        return testService.resetTestAlarm();
    }

    public Map getPEOPLE_NOT_MATCH() {
        return PEOPLE_NOT_MATCH;
    }

}
