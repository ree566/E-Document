/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.SystemReportDao;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class SystemReportService {

    @Autowired
    private SystemReportDao systemReportDao;

    public List<Map> getCountermeasureForExcel(String lineTypeName, int floorId, String startDate, String endDate, boolean isAboveStandard) {
        return systemReportDao.getCountermeasureForExcel(lineTypeName, floorId, startDate, endDate, isAboveStandard);
    }

    public List<Map> getPersonalAlmForExcel(String lineTypeName, int floorId, String startDate, String endDate, boolean isAboveStandard) {
        return systemReportDao.getPersonalAlmForExcel(lineTypeName, floorId, startDate, endDate, isAboveStandard);
    }

    public List<Map> getCountermeasureAndPersonalAlm(String startDate, String endDate) {
        return systemReportDao.getCountermeasureAndPersonalAlm(startDate, endDate);
    }

    public List<Map> getEmptyRecordDownExcel(String lineTypeName, int floorId, String startDate, String endDate) {
        return systemReportDao.getEmptyRecordDownExcel(lineTypeName, floorId, startDate, endDate);
    }

}
