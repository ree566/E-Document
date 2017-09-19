/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.webservice.WsClient;
import org.springframework.beans.factory.annotation.Autowired;


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
    public void test() throws Exception {
//        String test = "<root><METHOD ID='WMPSO.TxStandardWordtime'/><STANDARD_WORKTIME><UNIT_NO>B</UNIT_NO><STATION_ID>2</STATION_ID><LINE_ID>37</LINE_ID><ITEM_NO>UTC-542FP-ATB0E</ITEM_NO><TOTAL_CT>3.35</TOTAL_CT><FIRST_TIME>0</FIRST_TIME><CT>10</CT><SIDE>5010</SIDE><OP_CNT>1</OP_CNT><KP_TYPE/><MACHINE_CNT>3</MACHINE_CNT></STANDARD_WORKTIME></root>";
//        TxResponse res = client.simpleTxSendAndReceive(test);
//        assertNotNull(res);
//        System.out.println(res.getTxResult());

//        String ticker = "MSFT";
//        GetWeatherResponse response = (GetWeatherResponse) client.someOperation(ticker);
//        System.err.println(response.getGetWeatherResult());
    }
}
