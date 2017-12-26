/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.PassStation;
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

/**
 *
 * @author Wei.Cheng
 */
public class WebServiceRVTest {

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
     * Test of getInstance method, of class WebServiceRV.
     */
//    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        WebServiceRV expResult = null;
        WebServiceRV result = WebServiceRV.getInstance();
        assertNotEquals(expResult, result);
    }

    /**
     * Test of getKanbanUsersForString method, of class WebServiceRV.
     */
//    @Test
    public void testGetKanbanUsersForString() throws Exception {
        System.out.println("getKanbanUsersForString");
        WebServiceRV instance = WebServiceRV.getInstance();
        List<String> expResult = new ArrayList();
        List<String> result = instance.getKanbanUsersForString();
        assertNotEquals(expResult, result);

//        for (String st : result) {
//            out.println(st);
//        }
    }

    /**
     * Test of getKanbanWorkId method, of class WebServiceRV.
     */
//    @Test
    public void testGetKanbanWorkId() throws Exception {
        System.out.println("getKanbanWorkId");
        String jobnumber = "A-7275";
        WebServiceRV instance = WebServiceRV.getInstance();
        String expResult = "";
        String result = instance.getKanbanWorkId(jobnumber);
        assertNotEquals(expResult, result);
        out.println(result);
    }

    /**
     * Test of getModelnameByPo method, of class WebServiceRV.
     */
//    @Test
    public void testGetModelnameByPo() throws Exception {
        System.out.println("getModelnameByPo");
        String po = "PAGB079ZA";
        WebServiceRV instance = WebServiceRV.getInstance();
        String expResult = "";
        String result = instance.getModelnameByPo(po);
        assertNotEquals(expResult, result);
        out.println(result);
    }

    /**
     * Test of getMESUser method, of class WebServiceRV.
     */
//    @Test
    public void testGetMESUser() {
        System.out.println("getMESUser");
        String jobnumber = "A-7275";
        WebServiceRV instance = WebServiceRV.getInstance();
        UserOnMes expResult = null;
        UserOnMes result = instance.getMESUser(jobnumber);
        assertNotEquals(expResult, result);
        out.println(new Gson().toJson(result));
    }

    /**
     * Test of getPassStationRecords method, of class WebServiceRV.
     */
//    @Test
    public void testGetPassStationRecords() {
        System.out.println("getPassStationRecords");
        String po = "PNGC030ZA";
        Integer lineId = 55;
        WebServiceRV instance = WebServiceRV.getInstance();
        List<PassStation> expResult = null;
        List<PassStation> result = instance.getPassStationRecords(po, "ASSY");
        assertNotEquals(expResult, result);
        for (PassStation p : result) {
            out.println(new Gson().toJson(p));
        }
    }

    /**
     * Test of getTestLineTypeUsers method, of class WebServiceRV.
     */
//    @Test
    public void testGetTestLineTypeUsers() {
        System.out.println("getTestLineTypeUsers");
        WebServiceRV instance = WebServiceRV.getInstance();
        List<TestRecord> expResult = null;
        List<TestRecord> result = instance.getTestLineTypeRecords();
        assertNotEquals(expResult, result);
        for (TestRecord t : result) {
            out.println(new Gson().toJson(t));
        }
    }

    @Test
    public void testGetTestLineTypeRecord() throws JsonProcessingException{
        List<TestRecord> l = WebServiceRV.getInstance().getTestLineTypeRecords();
        assertNotEquals(0, l.size());
        HibernateObjectPrinter.print(l);
    }
}
