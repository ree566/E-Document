/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.entity.User;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.Service;
import org.tempuri.ServiceSoap;

/**
 *
 * @author Wei.Cheng
 */
public class WebServiceTX {

    private final String kanbanLogin = "A", kanbanLogout = "U";
    private final URL url;//webservice位置(放在專案中，因為url無法讀取，裏頭標籤衝突)
    private static final Logger log = LoggerFactory.getLogger(WebServiceTX.class);

    private static WebServiceTX instance;

    private WebServiceTX() {
        url = this.getClass().getClassLoader().getResource("wsdl/Service.wsdl");
    }

    public static WebServiceTX getInstance() {
        if (instance == null) {
            instance = new WebServiceTX();
        }
        return instance;
    }

    //Get data from WebService
    private String getWebServiceResponse(String data, String action) throws Exception {
        Service service = new Service(url);
        ServiceSoap port = service.getServiceSoap();
        String result = port.tx(data, action);
        return result;
    }

    public String kanbanUserLogin(String jobnumber) {
        User user = WebServiceRV.getInstance().getMESUser(jobnumber);
        String data = "<root><METHOD ID='WMPSO.TxWorkManPowerCard001'/><WORK_MANPOWER_CARD>"
                + "<WORK_ID>-1</WORK_ID>"
                + "<LINE_ID>23</LINE_ID>"
                + "<STATION_ID>11</STATION_ID>"
                + "<FACTORY_NO>TWM3</FACTORY_NO>"
                + "<UNIT_NO>T</UNIT_NO>"
                + "<USER_NO>" + user.getUserNo() + "</USER_NO>"
                + "<USER_NAME_CH>" + user.getUserName() + "</USER_NAME_CH>"
                + "<WORK_DESC></WORK_DESC>"
                + "<CARD_FLAG>1</CARD_FLAG>"
                + "<USER_ID>" + user.getUserId() + "</USER_ID>"
                + "</WORK_MANPOWER_CARD></root>";
        try {
            return this.getWebServiceResponse(data, kanbanLogin);
        } catch (Exception ex) {
            log.error(ex.toString());
            return ex.toString();
        }
    }

    public String kanbanUserLogout(String jobnumber) {
        User user = WebServiceRV.getInstance().getMESUser(jobnumber);
        String data = "<root><METHOD ID='WMPSO.TxWorkManPowerCard001'/><WORK_MANPOWER_CARD>"
                + "<WORK_ID>" + user.getUserId() + "</WORK_ID>"
                + "<LINE_ID>-1</LINE_ID>"
                + "<STATION_ID>-1</STATION_ID>"
                + "<FACTORY_NO></FACTORY_NO>"
                + "<UNIT_NO></UNIT_NO>"
                + "<USER_NO></USER_NO>"
                + "<USER_NAME_CH>" + user.getUserName() + "</USER_NAME_CH>"
                + "<WORK_DESC></WORK_DESC>"
                + "<CARD_FLAG>-1</CARD_FLAG>"
                + "<USER_ID>" + user.getUserId() + "</USER_ID>"
                + "</WORK_MANPOWER_CARD></root>";
        try {
            return this.getWebServiceResponse(data, kanbanLogout);
        } catch (Exception ex) {
            log.error(ex.toString());
            return ex.toString();
        }
    }

    public static void main(String arg[]) {

    }
}
