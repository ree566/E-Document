package com.advantech.service;

import com.advantech.model.BABDAO;
import com.advantech.entity.BAB;
import com.advantech.entity.BABHistory;
import com.advantech.helper.PropertiesReader;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Array;
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
    private final int BAB_CLOSED_SIGN = 1;
    private final int BAB_UNCLOSED_SIGN = 0;
    private final int BALANCE_ROUNDING_DIGIT = PropertiesReader.getInstance().getBalanceRoundingDigit();

    protected BABService() {
        babDAO = new BABDAO();
    }

    public JSONObject getBABByLine(int lineNo) throws JSONException {
        JSONObject jsonObj = null;
        List<BAB> babs = babDAO.getProcessingBABByLine(lineNo);
        if (babs != null && !babs.isEmpty()) {
            jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            for (BAB b : babs) {
                jsonArr.put(new Gson().toJson(b));
            }
            jsonObj.put("BABData", jsonArr);
        }
        return jsonObj;
    }

    public List<Array> getAvailableModelName() {
        return babDAO.getAvailableModelName();
    }

    public List<BAB> getBABInfo(String lineType) {
        return babDAO.getBABInfo(lineType);
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

    public String checkAndStartBAB(BAB bab) {
        LineService lineService = BasicService.getLineService();
        int lineClosedSign = lineService.getLINE_CLOSED_SIGN();

        List<BAB> prevBAB = babDAO.getProcessingBABByPOAndLine(bab.getPO(), bab.getLine());
        if (!prevBAB.isEmpty()) {
            return "工單號碼已經存在";
        }

        JSONObject lineState = lineService.getLineState(bab.getLine());
        return lineState.getInt("isused") == lineClosedSign ? "線別尚未開啟" : (startBAB(bab) ? "success" : "error");
    }

    public boolean startBAB(BAB bab) {
        BAB prevBab = babDAO.getEarliestBAB(bab.getLine());
        if (prevBab != null) {
            if (getBABAvgs(prevBab.getId()).length() == 0) {
                //沒有的話把上一筆工單直接結束掉
                babDAO.closeBABDirectly(prevBab);
            }
        }
        return babDAO.insertBAB(bab);
    }

    public String closeBAB(String babNo) throws Exception {
        List<BAB> processingBab = babDAO.getProcessingBAB(Integer.parseInt(babNo));
        if (processingBab != null && !processingBab.isEmpty()) {
            BAB bab = processingBab.get(0);
            String message = closeBAB(bab);
            return message;
        }
        return "查無工單資料，請聯絡管理人員";
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
                prevSensorCloseFlag = babDAO.checkPrevSensorIsClosed(bab.getId(), bab.getPeople() - 1);
                if (prevSensorCloseFlag == false) {
                    return "關閉失敗，檢檢查上一站是否關閉";
                }
            } else {
                prevSensorCloseFlag = true;
            }
            needToSave = prevSensorCloseFlag;
        }
        return (needToSave ? babDAO.stopAndSaveBab(bab) : babDAO.closeBABDirectly(bab)) ? "success" : "發生問題，請聯絡管理人員";
    }

    public double getLineBalance(int babNo) throws JSONException {
        return BasicService.getLineBalanceService().caculateLineBalance(getBABAvgs(babNo));
    }

    public JSONArray getAvg(int BABid, int isused) {
        return (isused == BAB_CLOSED_SIGN ? getClosedBABAVG(BABid) : getBABAvgs(BABid));
    }

    private JSONArray getBABAvgs(int BABid) throws JSONException {
        List l = babDAO.getBABAvgs(BABid);
        return new JSONArray(l);
    }

    public double getAvgType2(int BABid, int closedSign) throws Exception {
        List<Map> l = getLineBalanceDetail(BABid, (closedSign == BAB_CLOSED_SIGN ? BAB_CLOSED_SIGN : BAB_UNCLOSED_SIGN));
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

    public List<Map> getLineBalanceDetail(int BABid, int BABStatus) {
        return BABStatus == BAB_CLOSED_SIGN ? babDAO.getClosedBalanceDetail(BABid) : babDAO.getBalaceDetail(BABid);
    }

    public JSONArray getSensorDiffChart(int BABid, int isused) {
        JSONArray totalArrObj = new JSONArray();

        List<Map> l = (isused == BAB_CLOSED_SIGN ? babDAO.getBABTimeHistoryDetail(BABid) : babDAO.getSensorStatus(BABid));

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

    /**
     * Check sensor & return update message
     *
     * @param sensorNum
     * @param BABid
     * @return
     */
    public JSONObject stopSensor(int sensorNum, int BABid) {
        boolean checkCloseFlag = false;
        boolean sensorEndFlag = false;
        JSONObject message = new JSONObject();
        boolean existBabStatistics = (getBABAvgs(BABid).length() != 0);
        message.put("total", existBabStatistics);
        if (existBabStatistics) {
            //第二顆不做檢查，因為假使第一顆沒有換下一套(儲存紀錄)，後面無法做工單關閉
            checkCloseFlag = (sensorNum == 2 ? true : babDAO.checkPrevSensorIsClosed(BABid, sensorNum - 1));

            if (checkCloseFlag) {
                sensorEndFlag = babDAO.stopSingleSensor(sensorNum, BABid);
            }
        }
        message.put("history", checkCloseFlag);
        message.put("do_sensor_end", sensorEndFlag);

        return message;
    }

    public int getBAB_CLOSED_SIGN() {
        return BAB_CLOSED_SIGN;
    }

    public int getBAB_UNCLOSED_SIGN() {
        return BAB_UNCLOSED_SIGN;
    }

}
