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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
        return instance == null ? new WebServiceTX() : instance;
    }

    //Get data from WebService
    private String getWebServiceResponse(String data, String action) throws Exception {
        Service service = new Service(url);
        ServiceSoap port = service.getServiceSoap();
        String result = port.tx(data, action);
        return result;
    }

    public String kanbanUserLogin(String jobnumber) {
        try {
            WebServiceRV rv = WebServiceRV.getInstance();
            User user = rv.getMESUser(jobnumber);

            if (user == null) {
                return "The user is not exist.";
            }

            String lineId = "21", stationId = "-1";// 目前暫時寫死，等待有固定查詢來源

            String data = "<root><METHOD ID='WMPSO.TxWorkManPowerCard001'/><WORK_MANPOWER_CARD>"
                    + "<WORK_ID>-1</WORK_ID>"
                    + "<LINE_ID>" + lineId + "</LINE_ID>"
                    + "<STATION_ID>" + stationId + "</STATION_ID>"
                    + "<FACTORY_NO>TWM3</FACTORY_NO>"
                    + "<UNIT_NO>T</UNIT_NO>"
                    + "<USER_NO>" + user.getUserNo() + "</USER_NO>"
                    + "<USER_NAME_CH>" + user.getUserName() + "</USER_NAME_CH>"
                    + "<WORK_DESC>\"\"</WORK_DESC>"
                    + "<CARD_FLAG>1</CARD_FLAG>"
                    + "<USER_ID>" + user.getUserId() + "</USER_ID>"
                    + "</WORK_MANPOWER_CARD></root>";
            return this.getWebServiceResponse(data, kanbanLogin);
        } catch (Exception ex) {
            log.error(ex.toString());
            return ex.toString();
        }
    }

    protected String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }

    public String kanbanUserLogout(String jobnumber) {
        try {
            WebServiceRV rv = WebServiceRV.getInstance();
            String workId = rv.getKanbanWorkId(jobnumber);
            User user = rv.getMESUser(jobnumber);
            String data = "<root><METHOD ID='WMPSO.TxWorkManPowerCard001'/><WORK_MANPOWER_CARD>"
                    + "<WORK_ID>" + workId + "</WORK_ID>"
                    + "<LINE_ID>-1</LINE_ID>"
                    + "<STATION_ID>-1</STATION_ID>"
                    + "<FACTORY_NO></FACTORY_NO>"
                    + "<UNIT_NO></UNIT_NO>"
                    + "<USER_NO></USER_NO>"
                    + "<USER_NAME_CH></USER_NAME_CH>"
                    + "<WORK_DESC></WORK_DESC>"
                    + "<CARD_FLAG>-1</CARD_FLAG>"
                    + "<USER_ID>" + user.getUserId() + "</USER_ID>" //get userid from second xml
                    + "</WORK_MANPOWER_CARD></root>";
            return this.getWebServiceResponse(data, kanbanLogout);
        } catch (Exception ex) {
            log.error(ex.toString());
            return ex.toString();
        }
    }

    public static void main(String arg[]) {

    }
}
