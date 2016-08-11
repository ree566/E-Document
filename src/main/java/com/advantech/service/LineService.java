/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Line;
import com.advantech.model.LineDAO;
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

    protected LineService() {
        lineDAO = new LineDAO();
    }
    
    public JSONArray getLine() {
        //確認無人之後寫進資料庫 cookie作儲存 cookie失效時更改登入狀態
        //選擇線別後選擇是開始或結束人，查看該線別是否在進行中(看BAB資料表看是否該線別有感應器的1(尚未結束))
        //登入之後該線別的結束人可把該線手動作結束動作
        //前一天的就不用看了，只看當天
        List l = lineDAO.getLine();
        return new JSONArray(l);
    }
    
     public Line getLine(int lineNo) {
        return lineDAO.getLine(lineNo);
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

    public String loginBAB(int lineNo) throws JSONException {
        return isLineOpened(lineNo) ? "此線別尚未結束或者已使用中。" : (lineDAO.openSingleLine(lineNo) ? "success" : "fail");
    } 

    public String logoutBAB(int lineNo) throws JSONException {
        return isLineClosed(lineNo) ? "站別尚未開始，無法結束。" : (lineDAO.closeSingleLine(lineNo) ? "success" : "fail");
    }

    private boolean isLineOpened(int lineNo) {
        Line line = lineDAO.getLine(lineNo);
        return line.isIsOpened();
    }

    private boolean isLineClosed(int lineNo) {
        return !this.isLineOpened(lineNo);
    }

    public boolean closeAllLine() {
        return lineDAO.allLineEnd();
    }

}
