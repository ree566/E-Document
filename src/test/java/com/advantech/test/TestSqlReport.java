/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.BabPcsDetailHistoryDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.service.SystemReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.joda.time.DateTime;
import static org.junit.Assert.*;
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
public class TestSqlReport {

    @Autowired
    private SystemReportService systemReportService;

    @Test
    @Transactional
    @Rollback(true)
    public void testCountermeasureForExcel() {
        List l = systemReportService.getCountermeasureForExcel("18/01/01", "18/02/01");
        HibernateObjectPrinter.print(l);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testPersonalAlmForExcel() {
        List l = systemReportService.getPersonalAlmForExcel("18/01/01", "18/02/01");
        HibernateObjectPrinter.print(l);
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testEmptyRecordDownExcel() {
        List l = systemReportService.getEmptyRecordDownExcel("18/01/01", "18/02/01");
        HibernateObjectPrinter.print(l);
    }
}
