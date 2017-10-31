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

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.PropertiesReader;
import com.advantech.quartzJob.PollingSensorStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint("/echo")
@Component
public class Endpoint {

    private static final Logger log = LoggerFactory.getLogger(Endpoint.class);
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    private static String POLLING_FREQUENCY;
    private static final String JOB_NAME = "JOB1";

    @Autowired
    private CronTrigMod ctm;

    @PostConstruct
    protected void init(){
        POLLING_FREQUENCY = PropertiesReader.getInstance().getEndpointQuartzTrigger();
    }

    @OnOpen
    public void onOpen(final Session session) {
        //Push the current status on client first connect
        try {
            session.getBasicRemote().sendText(new PollingSensorStatus().getData());
        } catch (IOException ex) {
            log.error(ex.toString());
        }

//        HandshakeRequest req = (HandshakeRequest) conf.getUserProperties().get("handshakereq");
//        Map<String,List<String>> headers = req.getHeaders();
        sessions.add(session);
//        System.out.println("New session opened: " + session.getId());

        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        int a = sessions.size();
        if (a == 1) {
            pollingDBAndBrocast();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //無作用，目前暫時當作echo測試
        System.out.println("received msg " + message + " from " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
//        System.out.println("session closed: " + session.getId());

        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
        if (sessions.isEmpty()) {
            unPollingDB();
        }
    }

    @OnError
    public void error(Session session, Throwable t) {
        sessions.remove(session);
//        System.err.println("Error on session " + session.getId());
    }

    ///Brocast the servermessage to all online users.
    public static void sendAll(String msg) {
        try {
            /* Send the new rate to all open WebSocket sessions */
            ArrayList<Session> closedSessions = new ArrayList<>();
            for (Session session : sessions) {
                if (!session.isOpen()) {
//                    System.err.println("Closed session: " + session.getId());
                    closedSessions.add(session);
                } else {
                    session.getBasicRemote().sendText(msg);
                }
            }
            sessions.removeAll(closedSessions);
//            System.out.println("Sending " + msg + " to " + queue.size() + " clients");
        } catch (Throwable ex) {
            log.error(ex.toString());
        }
    }

    // Generate when connect users are at least one.
    private void pollingDBAndBrocast() {
        try {
            ctm.scheduleJob(PollingSensorStatus.class, JOB_NAME, POLLING_FREQUENCY);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    // Delete when all users are disconnect. 
    private void unPollingDB() {
        try {
            ctm.removeJob(JOB_NAME);
//            System.out.println("trigger has been removed");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    public static void clearSessions() {
        sessions.clear();
    }
}
