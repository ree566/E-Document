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
                
                String no = user.getUserNo();
                String name = user.getUserName();
                Double PRODUCTIVITY = user.getProductivity();
                
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
                            dataMap.put(tableid, ALARM_SIGN);
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
            processingJsonObject.put("data", userArr);
        } else {
            processingJsonObject = null;
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
