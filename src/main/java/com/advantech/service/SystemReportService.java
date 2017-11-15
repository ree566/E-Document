/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.SystemReportDao;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
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

    public List<Map> getUnFillCountermeasureBabs(int floor_id) {
        return systemReportDao.getUnFillCountermeasureBabs(floor_id);
    }

    public List<Map> getCountermeasureForExcel(String startDate, String endDate) {
        return systemReportDao.getCountermeasureForExcel(startDate, endDate);
    }

    public List<Map> getPersonalAlmForExcel(String startDate, String endDate) {
        return systemReportDao.getPersonalAlmForExcel(startDate, endDate);
    }

    public List<Map> getCountermeasureAndPersonalAlm(String startDate, String endDate) {
        return systemReportDao.getCountermeasureAndPersonalAlm(startDate, endDate);
    }

    public List<Map> getEmptyRecordDownExcel(String startDate, String endDate) {
        return systemReportDao.getEmptyRecordDownExcel(startDate, endDate);
    }

}
