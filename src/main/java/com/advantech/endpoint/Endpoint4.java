/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.endpoint;

import com.advantech.quartzJob.PollingFqcResult;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Wei.Cheng Polling cell jobs unfinished
 */
public class Endpoint4 extends BasicHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(Endpoint4.class);

    private static final String JOB_NAME = "PollingFqc";

    @Autowired
    private PollingFqcResult pollingJob;
    
    private static boolean isJobScheduled = false;

    @PostConstruct
    private void init() {
        log.info("Endpoint4 init polling job: " + JOB_NAME);
        super.init(JOB_NAME);
        if (super.sessions != null && !super.sessions.isEmpty()) {
            sessions.clear();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
        //Push the current status on client first connect
        wss.sendMessage(new TextMessage(pollingJob.getData()));

        //        HandshakeRequest req = (HandshakeRequest) conf.getUserProperties().get("handshakereq");
//        Map<String,List<String>> headers = req.getHeaders();
        sessions.add(wss);
//        System.out.println("New session opened: " + session.getId());

        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        synchronized (sessions) {
            int a = sessions.size();
            if (a > 0 && isJobScheduled == false) {
                pollingDBAndBrocast();
                isJobScheduled = true;
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {

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
