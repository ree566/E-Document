package com.advantech.service;

import com.advantech.model.AlarmAction;
import com.advantech.model.Bab;
import com.advantech.model.BabHistory;
import com.advantech.model.BabStatus;
import com.advantech.model.Line;
import com.advantech.helper.PropertiesReader;
import com.advantech.dao.BabDAO;
import com.advantech.model.LineStatus;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private BabLoginStatusService bs;
    
    @Autowired
    private LineBalancingService lineBalanceService;
    
    private int BALANCE_ROUNDING_DIGIT;

    private final Integer FIRST_STATION_NUMBER = 1;

    private int MININUM_SAVE_TO_DB_QUANTITY;

    @PostConstruct
    protected void BABService() {
        PropertiesReader p = PropertiesReader.getInstance();
        BALANCE_ROUNDING_DIGIT = p.getBalanceRoundingDigit();
        MININUM_SAVE_TO_DB_QUANTITY = p.getBabSaveToRecordStandardQuantity();
    }

    public Bab getBAB(int id) {
        return babDAO.getBAB(id);
    }

    public List<Bab> getBAB(String modelName, String dateFrom, String dateTo) {
        return babDAO.getBAB(modelName, dateFrom, dateTo);
    }

    public List<Bab> getTodayBAB() {
        return babDAO.getTodayBAB();
    }

    public List<Map> getBABForMap() {
        return babDAO.getBABForMap();
    }

    public List<Map> getBABForMap(int BABid) {
        return babDAO.getBABForMap(BABid);
    }

    public List<Map> getBABForMap(String date) {
        return babDAO.getBABForMap(date);
    }

    public List<Bab> getProcessingBABByLine(int lineNo) throws JSONException {
        return babDAO.getProcessingBABByLine(lineNo);
    }

    public List<Array> getAvailableModelName() {
        return babDAO.getAvailableModelName();
    }

    public boolean checkSensorIsClosed(int BABid, int sensorNo) {
        return babDAO.checkSensorIsClosed(BABid, sensorNo);
    }

    public Integer getPoTotalQuantity(String PO) {
        return babDAO.getPoTotalQuantity(PO);
    }

    public List<Map> getBABInfo(String startDate, String endDate) {
        return babDAO.getBABInfo(startDate, endDate);
    }

    public List<Map> getLineBalanceCompare(String Model_name, String lineType) {
        return babDAO.getLineBalanceCompare(Model_name, lineType);
    }

    public List<Map> getLineBalanceCompare(int BABid) {
        return babDAO.getLineBalanceCompare(BABid);
    }

    public List<Bab> getAllProcessing() {
        return babDAO.getAllProcessing();
    }

    public Bab getProcessing(int BABid) {
        List l = babDAO.getProcessing(BABid);
        return l.isEmpty() ? null : (Bab) l.get(0);
    }

    public List<Bab> getAssyProcessing() {
        return babDAO.getAssyProcessing();
    }

    public JSONArray getClosedBABAVG(int BABid) throws JSONException {
        List l = babDAO.getClosedBABAVG(BABid);
        return new JSONArray(l);
    }

    public List<Bab> getTimeOutBAB() {
        return babDAO.getTimeOutBAB();
    }

    public List<Bab> getProcessingBAB() {
        return babDAO.getProcessingBAB();
    }

    public JSONArray getLastGroupStatusForJson(int BABid) {
        return new JSONArray(getLastGroupStatus(BABid));
    }

    public List<Map> getLastGroupStatus(int BABid) {
        return babDAO.getLastGroupStatus(BABid);
    }

//    @Override
    public boolean insertAlarm(List<AlarmAction> l) {
        return babDAO.insertAlarm(l);
    }

//    @Override
    public boolean updateAlarm(List<AlarmAction> l) {
        return babDAO.updateAlarm(l);
    }

//    @Override
    public boolean resetAlarm() {
        return babDAO.resetAlarm();
    }

//    @Override
    public boolean removeAlarmSign() {
        return babDAO.removeAlarmSign();
    }

    public boolean setBABAlarmToTestingMode() {
        return babDAO.setBABAlarmToTestingMode();
    }

    public String checkAndStartBAB(Bab bab, String jobnumber) {
        List<Bab> prevBAB = babDAO.getProcessingBABByPOAndLine(bab.getPO(), bab.getLine());
        if (!prevBAB.isEmpty()) {
            return "工單號碼已經存在";
        }

        Bab prevBab = babDAO.getLastInputBAB(bab.getLine());
        boolean needToClosePrev = (prevBab != null && getBABAvgs(prevBab.getId()).length() == 0);

        if (prevBab != null && !needToClosePrev && prevBab.getStartPosition() != bab.getStartPosition()) {
            return "上一套工單與本次工單的起始站別不符，請等待上套工單完成再做投入";
        }

        Line line = lineService.getLine(bab.getLine());

        if (line.getLineStatus() == LineStatus.OPEN) {
            if (babDAO.insertBAB(bab)) {
                if (needToClosePrev) {
                    babDAO.closeBABDirectly(prevBab);
                }
                Bab b = this.babDAO.getLastInputBAB(bab.getLine());//get last insert id
                return bs.recordBABPeople(b.getId(), this.FIRST_STATION_NUMBER, jobnumber) ? "success" : "發生錯誤，工單已經投入，人員資訊無法記錄";
            } else {
                return "error";
            }
        } else {
            return "線別尚未開啟";
        }
    }

    public String closeBAB(int BABid) {
        Bab processingBab = babDAO.getProcessingBAB(BABid);
        if (processingBab != null) {
            String message = closeBAB(processingBab);
            return message;
        } else {
            return "查無工單資料，請聯絡管理人員";
        }
    }

    //檢查統計值是否為空，空值直接讓使用者作結束
    //檢查上一顆sensor使否有在紀錄中，有把紀錄記在LineBalancingMain
    public String closeBAB(Bab bab) {
        JSONArray babAvgs = getBABAvgs(bab.getId()); //先各站別取平衡率再算平均
        boolean needToSave = false;
        if (babAvgs != null && babAvgs.length() != 0) {
            if (isQuantityAboveMininum(bab)) { //儲存進資料庫所需要的最少機台樣本數
                bab.setBabavgs(babAvgs);
                boolean prevSensorCloseFlag;
                if (bab.getPeople() != 2) {
                    prevSensorCloseFlag = babDAO.checkSensorIsClosed(bab.getId(), bab.getPeople() - 1);
                    if (prevSensorCloseFlag == false) {
                        return "關閉失敗，請檢查上一站是否關閉";
                    }
                } else {
                    prevSensorCloseFlag = true;
                }
                needToSave = prevSensorCloseFlag;
            }
        }
        return (needToSave ? babDAO.stopAndSaveBab(bab) : babDAO.closeBABDirectly(bab)) ? "success" : "發生問題，請聯絡管理人員";
    }

    //低於設定值的資料不作儲存
    private boolean isQuantityAboveMininum(Bab bab) {
        int dataCount = babDAO.getBalancePerGroup(bab.getId()).size();
        return dataCount > MININUM_SAVE_TO_DB_QUANTITY;
    }

    public boolean closeBABWithoutCheckPrevSensor(Bab bab) {
        JSONArray babAvgs = getBABAvgs(bab.getId());
        bab.setBabavgs(babAvgs);
        return babAvgs.length() != 0 ? babDAO.stopAndSaveBab(bab) : babDAO.closeBABDirectly(bab);
    }

    public double getLineBalance(int BABid) throws JSONException {
        return lineBalanceService.caculateLineBalance(getBABAvgs(BABid));
    }

    public JSONArray getAvg(int BABid) {
        Bab b = this.getBAB(BABid);
        return (b.isIsBabClosed() ? getClosedBABAVG(BABid) : getBABAvgs(BABid));
    }

    public Double getTotalAvg(int BABid) {
        JSONArray j = this.getAvg(BABid);
        Double total = 0.0;
        int people = j.length();
        for (Object o : j) {
            JSONObject obj = (JSONObject) o;
            total += obj.getDouble("average");
        }
        return total / people;
    }

    private JSONArray getBABAvgs(int BABid) {
        List l = babDAO.getBABAvgs(BABid);
        return new JSONArray(l);
    }

    public List<Map> getBABAvgsInSpecGroup(int BABid, int groupStart, int groupEnd) {
        return babDAO.getBABAvgsInSpecGroup(BABid, groupStart, groupEnd);
    }

    public double getAvgType2(int BABid, BabStatus state) throws Exception {
        List<Map> l = getLineBalanceDetail(BABid, state);
        if (l.isEmpty()) {
            return 0;
        } else {
            double sum = 0;
            for (Map m : l) {
                sum += ((BigDecimal) m.get("balance")).setScale(BALANCE_ROUNDING_DIGIT, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            double balanceAvg = sum / l.size();
            return balanceAvg;
        }
    }

    public List<Map> getLineBalanceDetail(int BABid, BabStatus state) {
        return state == BabStatus.CLOSED ? babDAO.getClosedBalanceDetail(BABid) : babDAO.getBalancePerGroup(BABid);
    }

    public List<Map> getBABTimeDetail(int BABid, BabStatus state) {
        return state == BabStatus.CLOSED ? babDAO.getBABTimeHistoryDetail(BABid) : babDAO.getSensorStatus(BABid);
    }

    public JSONArray getSensorDiffChart(int BABid, BabStatus state) {
        JSONArray totalArrObj = new JSONArray();

        List<Map> l = getBABTimeDetail(BABid, state);

        JSONObject innerObj = new JSONObject();
        JSONArray arr = new JSONArray();
        String tName = "";
        int loopCount = 1;
        int listSize = l.size();
        for (Map m : l) {
            boolean isLast = loopCount == listSize;
            String st = (String) m.get("TagName");
            int groupid = (int) m.get("groupid");
            if ("".equals(tName)) {
                tName = st;
            }
            if (!st.equals(tName) || isLast) {
                innerObj.put("name", tName);
                innerObj.put("dataPoints", arr);
                totalArrObj.put(innerObj);
                if (!isLast) {
                    innerObj = new JSONObject();
                    arr = new JSONArray();
                }
                tName = st;
            }
            arr.put(new JSONObject().put("x", groupid).put("y", m.get("diff")));
            loopCount++;
            //there will be error when data group only have one
        }
        return totalArrObj;
    }

    public JSONObject getBABInfoWithSensorState(String po, String lineNo) throws JSONException {
        JSONObject jsonObj = null;
        List<Bab> l = babDAO.getProcessingBABByPOAndLine(po, Integer.parseInt(lineNo));
        if (l.isEmpty()) {
            return jsonObj;
        }
        Bab bab = l.get(0);
        if (bab != null) {
            jsonObj = new JSONObject(new Gson().toJson(bab));
            JSONObject babHistory = new JSONObject();
            List<BabHistory> historys = babDAO.getBABHistory(bab);
            for (int i = 1, len = bab.getPeople(); i <= len; i++) {
                babHistory.put("T" + i, 0);
            }
            for (BabHistory bh : historys) {
                babHistory.put("T" + bh.getT_Num(), 1);
            }
            jsonObj.put("S_State", babHistory);
        }
        return jsonObj;
    }

    public JSONObject stopSensor(int BABid, int station) {
        boolean checkCloseFlag = false;
        boolean sensorEndFlag = false;
        JSONObject message = new JSONObject();
        boolean existBabStatistics = (getBABAvgs(BABid).length() != 0);
        message.put("total", existBabStatistics);
        if (existBabStatistics) {
            //第二站不做檢查，因為假使第一顆沒有換下一套(儲存紀錄)，後面無法做工單關閉，而且假如只開兩站不會執行此function
            checkCloseFlag = (station == 2 ? true : this.checkSensorIsClosed(BABid, station - 1));

            if (checkCloseFlag) {
                sensorEndFlag = babDAO.stopSingleSensor(station, BABid);
            }
        }
        message.put("history", checkCloseFlag);
        message.put("do_sensor_end", sensorEndFlag);

        return message;
    }

    public int getMininumSaveToDbQuantity() {
        return MININUM_SAVE_TO_DB_QUANTITY;
    }
}
