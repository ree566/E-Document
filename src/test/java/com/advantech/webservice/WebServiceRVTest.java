/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.PassStationRecord;
import com.advantech.model.TestRecord;
import com.advantech.model.UserOnMes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class WebServiceRVTest {
    
    @Autowired
    private WebServiceRV rv;

    public WebServiceRVTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getKanbanUsersForString method, of class WebServiceRV.
     */
    @Test
    public void testGetKanbanUsersForString() throws Exception {
        System.out.println("getKanbanUsersForString");
        List<String> result = rv.getKanbanUsersForString();
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getKanbanWorkId method, of class WebServiceRV.
     */
    @Test
    public void testGetKanbanWorkId() throws Exception {
        System.out.println("getKanbanWorkId");
        String jobnumber = "A-7275";
        String expResult = "";
        String result = rv.getKanbanWorkId(jobnumber);
        assertNotEquals(expResult, result);
        out.println(result);
    }

    /**
     * Test of getModelnameByPo method, of class WebServiceRV.
     */
    @Test
    public void testGetModelnameByPo() throws Exception {
        System.out.println("getModelnameByPo");
        String po = "PAGB079ZA";
        String expResult = "";
        String result = rv.getModelnameByPo(po);
        assertNotEquals(expResult, result);
        out.println(result);
    }

    /**
     * Test of getMESUser method, of class WebServiceRV.
     */
    @Test
    public void testGetMESUser() {
        System.out.println("getMESUser");
        String jobnumber = "A-7275";
        UserOnMes expResult = null;
        UserOnMes result = rv.getMESUser(jobnumber);
        assertNotEquals(expResult, result);
        out.println(new Gson().toJson(result));
    }

    /**
     * Test of getPassStationRecords method, of class WebServiceRV.
     */
    @Test
    public void testGetPassStationRecords() {
        System.out.println("getPassStationRecords");
        String po = "PNGC030ZA";
        Integer lineId = 55;
        List<PassStationRecord> expResult = null;
        List<PassStationRecord> result = rv.getPassStationRecords(po);
        assertNotEquals(expResult, result);
        for (PassStationRecord p : result) {
            out.println(new Gson().toJson(p));
        }
    }

    /**
     * Test of getTestLineTypeUsers method, of class WebServiceRV.
     */
    @Test
    public void testGetTestLineTypeUsers() {
        System.out.println("getTestLineTypeUsers");
        List<TestRecord> expResult = null;
        List<TestRecord> result = rv.getTestLineTypeRecords();
        assertNotEquals(expResult, result);
        for (TestRecord t : result) {
            out.println(new Gson().toJson(t));
        }
    }

    @Test
    public void testGetTestLineTypeRecord() throws JsonProcessingException{
        List<TestRecord> l = rv.getTestLineTypeRecords();
        assertNotEquals(0, l.size());
        HibernateObjectPrinter.print(l);
    }
}
