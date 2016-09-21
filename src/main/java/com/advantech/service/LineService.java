/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.Line;
import com.advantech.model.LineDAO;
import java.util.List;
import org.json.JSONException;

/**
 *
 * @author Wei.Cheng
 */
public class LineService {

    private final LineDAO lineDAO;

    protected LineService() {
        lineDAO = new LineDAO();
    }

    public List<Line> getLine() {
        return lineDAO.getOpenedLine();
    }

    public Line getLine(int lineNo) {
        return lineDAO.getLine(lineNo);
    }
    
     public List<Line> getLine(String sitefloor) {
        return lineDAO.getLine(sitefloor);
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
