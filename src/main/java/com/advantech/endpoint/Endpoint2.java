/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 組測包狀態回饋
 */
package com.advantech.endpoint;

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.PropertiesReader;
import com.advantech.quartzJob.PollingBabAndTestResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint("/echo2")
public class Endpoint2 {
    
    private static final Logger log = LoggerFactory.getLogger(Endpoint2.class);
//    private static final Queue<Session> queue = new ConcurrentLinkedQueue<>();
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    
    private static final String POLLING_FREQUENCY;
    private static final String JOB_NAME = "JOB2";
    
    static {
        POLLING_FREQUENCY = PropertiesReader.getInstance().getEndpointQuartzTrigger();
    }
    
    @OnOpen
    public void onOpen(final Session session) {
        
        //Push the current status on client first connect
        try {
            session.getBasicRemote().sendText(new PollingBabAndTestResult().getData());
        } catch (IOException ex) {
            log.error(ex.toString());
        }
        
        sessions.add(session);
        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        int a = sessions.size();
        if (a == 1) {
            System.out.println("Some session exist, begin polling.");
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
        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
        if (sessions.isEmpty()) {
            unPollingDB();
            System.out.println("All session closed");
        }
    }
    
    @OnError
    public void error(Session session, Throwable t) {
        sessions.remove(session);
    }

    ///Brocast the servermessage to all online users.
    public static void sendAll(String msg) {
        try {
            /* Send the new rate to all open WebSocket sessions */
            ArrayList<Session> closedSessions = new ArrayList<>();
            for (Session session : sessions) {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    session.getBasicRemote().sendText(msg);
                }
            }
            sessions.removeAll(closedSessions);
        } catch (Throwable ex) {
            log.error(ex.toString());
        }
    }

    // Generate when connect users are at least one.
    private void pollingDBAndBrocast() {
        try {
            CronTrigMod.getInstance().scheduleJob(PollingBabAndTestResult.class, JOB_NAME, POLLING_FREQUENCY);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    // Delete when all users are disconnect.
    private void unPollingDB() {
        try {
            CronTrigMod.getInstance().removeJob(JOB_NAME);
//            System.out.println("trigger has been removed");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }
    
    public static void clearSessions() {
        sessions.clear();
    }
}
