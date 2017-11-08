/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Fbn;
import com.advantech.dao.FbnDAO;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class FbnService {

    @Autowired
    private FbnDAO fbnDAO;

    public Fbn getLastInputData() {
        return fbnDAO.getLastInputData();
    }

    public List<Fbn> getSensorDataInDay() {
        return fbnDAO.getSensorDataInDay();
    }

    public List<Map> getSensorCurrentStatus() {
        return fbnDAO.getSensorCurrentStatus();
    }
    
    public List<Fbn> getSensorStatus(int BABid) {
        return fbnDAO.getSensorStatus(BABid);
    }

    public Fbn getBABFinalStationSensorStatus(int BABid) {
        return fbnDAO.getBABFinalStationSensorStatus(BABid);
    }

    public Integer checkLastFBNMinuteDiff() {
        DateTime now = new DateTime();
        Fbn f = this.getLastInputData();
        return f == null ? null : Minutes.minutesBetween(convert(getSensorTime(f)), now).getMinutes();
    }

    public Integer checkHoursDiff(Fbn f) {
        DateTime now = new DateTime();
        return f == null ? null : Hours.hoursBetween(convert(getSensorTime(f)), now).getHours();
    }

    private String getSensorTime(Fbn f) {
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

    public List<Map> getTotalAbnormalData(int BABid) {
        return fbnDAO.getTotalAbnormalData(BABid);
    }

    public List<Map> getAbnormalData(int BABid) {
        return fbnDAO.getAbnormalData(BABid);
    }

    public boolean sensorDataClean(String date) {
        return fbnDAO.sensorDataClean(date);
    }
}
