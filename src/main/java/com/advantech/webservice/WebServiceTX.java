/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.model.UserOnMes;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.Service;
import org.tempuri.ServiceSoap;
import static com.google.common.base.Preconditions.*;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class WebServiceTX {

    private URL url;//webservice位置(放在專案中，因為url無法讀取，裏頭標籤衝突)
    private static final Logger log = LoggerFactory.getLogger(WebServiceTX.class);
    
    @Autowired
    private WebServiceRV rv;

    @PostConstruct
    private void init() {
        Class clz = this.getClass();
        log.info(clz.getName() + " init url");
        url = clz.getClassLoader().getResource("wsdl/Service.wsdl");
    }

    //Get data from WebService
    private String simpleTxSendAndReceive(String data, String action) {
        Service service = new Service(url);
        ServiceSoap port = service.getServiceSoap();
        String result = port.tx(data, action);
        return result;
    }

    private void sendData(String data, UploadType uploadType) {
        String st = this.simpleTxSendAndReceive(data, uploadType.toString());
        checkState(!"OK".equals(st), st);
    }

    public void kanbanUserLogin(String jobnumber) {
        UserOnMes user = rv.getMESUser(jobnumber);

        checkUserIsAvailable(user);

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
        sendData(data, UploadType.INSERT);
    }

    public void kanbanUserLogout(String jobnumber) {
        String workId = rv.getKanbanWorkId(jobnumber);
        UserOnMes user = rv.getMESUser(jobnumber);

        checkUserIsAvailable(user);

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
        sendData(data, UploadType.UPDATE);
    }

    private void checkUserIsAvailable(UserOnMes user) {
        checkArgument(user == null || user.getUserId() == null || user.getUserNo() == null, "The user is not exist.");
    }

}
