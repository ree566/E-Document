package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.model.BABDAO;
import com.advantech.entity.BAB;
import com.advantech.entity.BABHistory;
import com.advantech.entity.Line;
import com.advantech.helper.PropertiesReader;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
public class BABService {

    private final BABDAO babDAO;
    private final int BALANCE_ROUNDING_DIGIT = PropertiesReader.getInstance().getBalanceRoundingDigit();

    private final Integer BAB_NO_RECORD_SIGN = -1;
    private final Integer BAB_UNCLOSE_SIGN = null;
    private final Integer BAB_CLOSED_SIGN = 1;

    private final Integer FIRST_STATION_NUMBER = 1;

    protected BABService() {
        babDAO = new BABDAO();
    }

    public BAB getBAB(int id) {
        return babDAO.getBAB(id);
    }

    public List<BAB> getBAB(String modelName, String dateFrom, String dateTo) {
        return babDAO.getBAB(modelName, dateFrom, dateTo);
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

    public List<BAB> getProcessingBABByLine(int lineNo) throws JSONException {
        return babDAO.getProcessingBABByLine(lineNo);
    }

    public List<Array> getAvailableModelName() {
        return babDAO.getAvailableModelName();
    }

    public boolean checkSensorIsClosed(int BABid, int sensorNo) {
        return babDAO.checkSensorIsClosed(BABid, sensorNo);
    }

    public List<Map> getBABInfo(String lineType, String sitefloor, String startDate, String endDate) {
        return seperateNotFilterBab(babDAO.getBABInfo(startDate, endDate), lineType, sitefloor);
    }

    protected List<Map> seperateNotFilterBab(List<Map> l, String lineType, String sitefloor) {
        Iterator it = l.iterator();
        while (it.hasNext()) {
            Map m = (Map) it.next();
            Integer floor = (Integer) m.get("sitefloor");
            String type = (String) m.get("lineType");

            if ("-1".equals(lineType) && !"-1".equals(sitefloor)) {
                if (!floor.toString().equals(sitefloor)) {
                    it.remove();
                }
            } else if (!"-1".equals(lineType) && "-1".equals(sitefloor)) {
                if (!type.equals(lineType)) {
                    it.remove();
                }
            } else if (!"-1".equals(lineType) && !"-1".equals(sitefloor)) {
                if (!type.equals(lineType) || !floor.toString().equals(sitefloor)) {
                    it.remove();
                }
            } else {
                break;
            }
        }
        return l;
    }

    public List<Map> getLineBalanceCompare(String Model_name, String lineType) {
        return babDAO.getLineBalanceCompare(Model_name, lineType);
    }

    public List<Map> getLineBalanceCompare(int BABid) {
        return babDAO.getLineBalanceCompare(BABid);
    }

    public List<BAB> getBABIdForCaculate() {
        return babDAO.getBABIdForCaculate();
    }

    public JSONArray getClosedBABAVG(int BABid) throws JSONException {
        List l = babDAO.getClosedBABAVG(BABid);
        return new JSONArray(l);
    }

    public List<BAB> getTimeOutBAB() {
        return babDAO.getTimeOutBAB();
    }

    public List<BAB> getProcessingBAB() {
        return babDAO.getProcessingBAB();
    }

    public JSONArray getLastGroupStatus(int BABid) {
        List l = babDAO.getLastGroupStatus(BABid);
        return new JSONArray(l);
    }

    public List<Map> getClosedBABInfo(String startDate, String endDate) {
        return babDAO.getClosedBABInfoDetail(startDate, endDate);
    }

    public boolean insertBABAlarm(List<AlarmAction> l) {
        return babDAO.insertTestAlarm(l);
    }

    public boolean updateBABAlarm(List<AlarmAction> l) {
        return babDAO.updateBABAlarm(l);
    }

    public boolean resetBABAlarm() {
        return babDAO.resetBABAlarm();
    }

    public boolean removeAllAlarmSign() {
        return babDAO.removeAllAlarmSign();
    }

    public boolean setBABAlarmToTestingMode() {
        return babDAO.setBABAlarmToTestingMode();
    }

    public String checkAndStartBAB(BAB bab, String jobnumber) {
        LineService lineService = BasicService.getLineService();

        List<BAB> prevBAB = babDAO.getProcessingBABByPOAndLine(bab.getPO(), bab.getLine());
        if (!prevBAB.isEmpty()) {
            return "工單號碼已經存在";
        }

        Line line = lineService.getLine(bab.getLine());

        if (line.isIsOpened()) {
            if (startBAB(bab)) {
                BAB b = this.babDAO.getLastInputBAB(bab.getLine());//get last insert id
                BABLoginStatusService bs = BasicService.getBabLoginStatusService();
                return bs.recordBABPeople(b.getId(), this.FIRST_STATION_NUMBER, jobnumber) ? "success" : "發生錯誤，工單已經投入，人員資訊無法記錄";
            } else {
                return "error";
            }
        } else {
            return "線別尚未開啟";
        }
    }

    public boolean startBAB(BAB bab) {
        BAB prevBab = babDAO.getLastInputBAB(bab.getLine());
        if (prevBab != null && getBABAvgs(prevBab.getId()).length() == 0) {
            //沒有的話把上一筆工單直接結束掉
            babDAO.closeBABDirectly(prevBab);
        }
        return babDAO.insertBAB(bab);
    }

    public String closeBAB(int BABid) {
        BAB processingBab = babDAO.getProcessingBAB(BABid);
        if (processingBab != null) {
            String message = closeBAB(processingBab);
            return message;
        } else {
            return "查無工單資料，請聯絡管理人員";
        }
    }

    //檢查統計值是否為空，空值直接讓使用者作結束
    //檢查上一顆sensor使否有在紀錄中，有把紀錄記在LineBalancingMain
    public String closeBAB(BAB bab) throws JSONException {
//        JSONArray babAvgs = getBABAvgs(bab.getId()); //先各站別取平均再算平衡率
        JSONArray babAvgs = getBABAvgs(bab.getId()); //先各站別取平衡率再算平均
        boolean needToSave = false;
        if (babAvgs != null && babAvgs.length() != 0) {
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
        return (needToSave ? babDAO.stopAndSaveBab(bab) : babDAO.closeBABDirectly(bab)) ? "success" : "發生問題，請聯絡管理人員";
    }

    public boolean closeBABWithoutCheckPrevSensor(BAB bab) {
        JSONArray babAvgs = getBABAvgs(bab.getId());
        bab.setBabavgs(babAvgs);
        return babAvgs.length() != 0 ? babDAO.stopAndSaveBab(bab) : babDAO.closeBABDirectly(bab);
    }

    public double getLineBalance(int BABid) throws JSONException {
        return BasicService.getLineBalanceService().caculateLineBalance(getBABAvgs(BABid));
    }

    public JSONArray getAvg(int BABid) {
        BAB b = this.getBAB(BABid);
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

    public List<Map> getBABAvgsInSpecGroup(int BABid) {
        return babDAO.getBABAvgsInSpecGroup(BABid);
    }

    public double getAvgType2(int BABid, int closedSign) throws Exception {
        List<Map> l = getLineBalanceDetail(BABid, closedSign);
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

    public List<Map> getLineBalanceDetail(int BABid, int isused) {
        return isused == BAB_CLOSED_SIGN ? babDAO.getClosedBalanceDetail(BABid) : babDAO.getBalaceDetail(BABid);
    }

    public List<Map> getBABTimeDetail(int BABid, int isused) {
        return isused == BAB_CLOSED_SIGN ? babDAO.getBABTimeHistoryDetail(BABid) : babDAO.getSensorStatus(BABid);
    }

    public JSONArray getSensorDiffChart(int BABid, int isused) {
        JSONArray totalArrObj = new JSONArray();

        List<Map> l = getBABTimeDetail(BABid, isused);

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
        List<BAB> l = babDAO.getProcessingBABByPOAndLine(po, Integer.parseInt(lineNo));
        if (l.isEmpty()) {
            return jsonObj;
        }
        BAB bab = l.get(0);
        if (bab != null) {
            jsonObj = new JSONObject(new Gson().toJson(bab));
            JSONObject babHistory = new JSONObject();
            List<BABHistory> historys = babDAO.getBABHistory(bab);
            for (int i = 1, len = bab.getPeople(); i <= len; i++) {
                babHistory.put("T" + i, 0);
            }
            for (BABHistory bh : historys) {
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
}
