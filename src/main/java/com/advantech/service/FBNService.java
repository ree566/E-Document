/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.FBNDAO;
import com.advantech.entity.FBN;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Wei.Cheng
 */
public class FBNService {

    private final FBNDAO fbnDAO;

    protected FBNService() {
        fbnDAO = new FBNDAO();
    }

    public FBN getLastInputData() {
        return fbnDAO.getLastInputData();
    }

    public List<FBN> getSensorDataInDay() {
        return fbnDAO.getSensorDataInDay();
    }

    public List<Map> getSensorInstantlyStatus() {
        return fbnDAO.getSensorInstantlyStatus();
    }

    public List<Map> getBalancePerGroup(int BABid) {
        return fbnDAO.getBalancePerGroup(BABid);
    }

    public FBN getBABFinalStationSensorStatus(int BABid) {
        return fbnDAO.getBABFinalStationSensorStatus(BABid);
    }

    public Integer checkLastFBNTimeDiff() {
        DateTime now = new DateTime();
        return Minutes.minutesBetween(convert(getSensorTime(this.getLastInputData())), now).getMinutes();
    }

    public Integer checkTimeDiff(FBN f) {
        DateTime now = new DateTime();
        return Minutes.minutesBetween(convert(getSensorTime(f)), now).getMinutes();
    }

    private String getSensorTime(FBN f) {
        return convertFullDateTime(f.getLogDate(), f.getLogTime());
    }

    private String convertFullDateTime(String... str) {
        String dateString = "";
        for (String st : str) {
            dateString += st.trim() + " ";
        }
        return dateString;
    }

    private DateTime convert(String date) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yy/MM/dd HH:mm:ss ");
        return dtf.parseDateTime(date);
    }
}
