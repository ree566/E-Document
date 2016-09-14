/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoAnnotations.Mock;

/**
 *
 * @author Wei.Cheng
 */
public class CountermeasureServletTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession session;

    public CountermeasureServletTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of doPost method, of class CountermeasureServlet.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost testing");

//        HttpServletRequest req = mock(HttpServletRequest.class);
//        HttpServletResponse res = mock(HttpServletResponse.class);
        
        when(request.getParameter("action")).thenReturn("getExcel");
        
        CountermeasureServlet instance = new CountermeasureServlet();
        instance.init();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);
        instance.doPost(request, response);

        String notExcept = null;
        String notExcept2 = "";

        String result = sw.getBuffer().toString().trim();
        System.out.println("Result: " + result);

        assertNotEquals(notExcept, result);
        assertNotEquals(notExcept2, result);

    }

}
