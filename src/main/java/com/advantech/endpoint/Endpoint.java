/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 105/01/14改為主要show出當下sensor資料的websocket server 改良版
 * 參考來源 : 
 * websocket : https://dzone.com/articles/sample-java-web-socket-client
 * quartz : http://potatolattle.blogspot.tw/2013/10/java-classquartz.html
 * 感應器狀態
 */
package com.advantech.endpoint;

import com.advantech.quartzJob.PollingSensorStatus;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Wei.Cheng
 */
public class Endpoint extends BasicHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(Endpoint.class);

    private static final String JOB_NAME = "JOB1";

    @PostConstruct
    private void init() {
        log.info("Endpoint schedule polling job: " + PollingSensorStatus.class.getName());
        super.init(PollingSensorStatus.class, JOB_NAME);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
        //Push the current status on client first connect
        try {
            wss.sendMessage(new TextMessage(new PollingSensorStatus().getData()));
        } catch (IOException ex) {
            log.error(ex.toString());
        }

//        HandshakeRequest req = (HandshakeRequest) conf.getUserProperties().get("handshakereq");
//        Map<String,List<String>> headers = req.getHeaders();
        sessions.add(wss);
//        System.out.println("New session opened: " + session.getId());

        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        int a = sessions.size();
        if (a == 1) {
            pollingDBAndBrocast();
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        sessions.remove(wss);
        log.error(thrwbl.toString(), thrwbl);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        sessions.remove(wss);
//        System.out.println("session closed: " + session.getId());

        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
        if (sessions.isEmpty()) {
            unPollingDB();
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
