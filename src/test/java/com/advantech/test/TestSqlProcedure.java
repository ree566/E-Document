/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.BabDAO;
import com.advantech.dao.BabPcsDetailHistoryDAO;
import com.advantech.dao.SqlProcedureDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSqlProcedure {

    @Autowired
    private SqlProcedureDAO procDAO;

    @Autowired
    private BabPcsDetailHistoryDAO babPcsDetailHistoryDAO;
    
    @Autowired
    private BabDAO babDAO;

//    @Test
    @Transactional
    @Rollback(true)
    public void testGetBabDetail() {
        DateTime sD = new DateTime(2017, 12, 01, 0, 0, 0, 0);
        DateTime eD = new DateTime(2017, 12, 31, 0, 0, 0, 0);
//        List l = procDAO.findBabDetail("ASSY", "5", sD, eD, false);
//        assertNotEquals(0, l.size());
//        System.out.println(l.size());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabPcsDetailHistory() {
        List l = babPcsDetailHistoryDAO.findByBabForMap(14223);
        HibernateObjectPrinter.print(l);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testProc() {
        DateTime d = new DateTime();
        String st = "";
        int i = 1;

//        procDAO.findBabBestLineBalanceRecord(1, d, d);
        
//        procDAO.findBabDetail(1, 1, d, d, true); 
        
//        procDAO.findBabDetailWithBarcode(1, 1, d, d, true);
//        procDAO.findBabLastBarcodeStatus(123);
//        procDAO.findBabLastGroupStatus(123);
//        procDAO.findBabLineProductivity("", "", 1, "", 1, d, d);
//        procDAO.findBabLineProductivityWithBarcode("", "", 1, "", 1, d, d);
        
//        procDAO.findBabPassStationExceptionReport("", "", d, d, 0); 

//        procDAO.findBabPassStationRecord("", "", d, d, ""); 

//        procDAO.findBabPcsDetail(st, st, d, d);
//        procDAO.findBabPcsDetailWithBarcode(st, st, d, d);

//        procDAO.findBabPreAssyProductivity(1, 1, d, d); 
        
//        procDAO.findLineBalanceCompare(st, st);
//        procDAO.findLineBalanceCompareByBab(i);
//        procDAO.findLineBalanceCompareByBabWithBarcode(i);
//        procDAO.findPreAssyModuleUnexecuted(d, d);
//        procDAO.findSensorCurrentGroupStatus(i);
//        procDAO.findTestPassStationProductivity(d, d);
        
//        procDAO.getTotalAbnormalData(i); //proc M3_BW.sensorTotalAbnormalCheck not found
//        procDAO.getAbnormalData(i); proc not found
//        procDAO.ModelSopRemarkDetail(st, i);
        
        Bab b = babDAO.findByPrimaryKey(185);
//        procDAO.closeBabDirectly(b);
//        procDAO.closeBabWithSaving(b);
//        procDAO.closeBabWithSavingWithBarcode(b);
        
        procDAO.sensorDataClean(d.withTime(0, 0, 0, 0).toDate());
        
//        procDAO.findWorktime();
//        procDAO.findWorktime(st);
//        procDAO.findUserInfoRemote();
//        procDAO.findUserInfoRemote(st);
    }

}
