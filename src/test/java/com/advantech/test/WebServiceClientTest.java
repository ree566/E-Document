/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.websocket.WsClient;
import hello.wsdl.GetWeatherResponse;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
//@WebAppConfiguration
//@ContextConfiguration(locations = {
//    "classpath:servlet-context.xml"
//})
//@RunWith(SpringJUnit4ClassRunner.class)
public class WebServiceClientTest {

    @Autowired
    private WsClient client;
    
//    @Test
    public void test() {
        GetWeatherResponse response = client.simpleSendAndReceive();
        assertNotNull(response);
        System.out.println(response.getGetWeatherResult());
        
//        String ticker = "MSFT";
//        GetWeatherResponse response = (GetWeatherResponse) client.someOperation(ticker);
//        System.err.println(response.getGetWeatherResult());
    }
}
