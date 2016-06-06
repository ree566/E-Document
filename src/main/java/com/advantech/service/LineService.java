/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.LineDAO;
import com.google.gson.Gson;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei.Cheng
 */
public class LineService {

    private final LineDAO lineDAO;
    private final int LINE_OPEN_SIGN = 1;
    private final int LINE_CLOSED_SIGN = 0;

    protected LineService() {
        lineDAO = new LineDAO();
    }

    public String loginBAB(JSONObject lineStateInfo, int lineNo) throws JSONException {
        return isLineOpened(lineStateInfo) ? "此線別尚未結束或者已使用中" : (lineDAO.openSingleLine(lineNo) ? "success" : "fail");
    }

    public String logoutBAB(JSONObject lineStateInfo, int lineNo) throws JSONException {
        return isLineClosed(lineStateInfo) ? "站別尚未開始，無法結束" : (lineDAO.closeSingleLine(lineNo) ? "success" : "fail");
    }

    private boolean isLineOpened(JSONObject lineStateInfo) throws JSONException {
        return lineStateInfo.getInt("isused") == LINE_OPEN_SIGN;
    }

    private boolean isLineClosed(JSONObject lineStateInfo) throws JSONException {
        return lineStateInfo.getInt("isused") == LINE_CLOSED_SIGN;
    }

    public boolean closeAllLine() {
        return lineDAO.allLineEnd();
    }

    public JSONArray getLine() {
        //確認無人之後寫進資料庫 cookie作儲存 cookie失效時更改登入狀態
        //選擇線別後選擇是開始或結束人，查看該線別是否在進行中(看BAB資料表看是否該線別有感應器的1(尚未結束))
        //登入之後該線別的結束人可把該線手動作結束動作
        //前一天的就不用看了，只看當天
        List l = lineDAO.getLine();
        return new JSONArray(l);
    }

    public JSONObject getLineState(int lineNo) throws JSONException {
        JSONArray lineArr = getLine();
        JSONObject matchLine = null;
        for (Object obj : lineArr) {
            JSONObject jsonObj = (JSONObject) obj;
            if (jsonObj.getInt("id") == lineNo) {
                matchLine = jsonObj;
                break;
            }
        }
        return matchLine;
    }

    public int getLINE_CLOSED_SIGN() {
        return LINE_CLOSED_SIGN;
    }

}
