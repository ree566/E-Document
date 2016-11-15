/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 105/01/14改為主要show出當下sensor資料的websocket server 改良版
 * 參考來源 : 
 * websocket : https://dzone.com/articles/sample-java-web-socket-client
 * quartz : http://potatolattle.blogspot.tw/2013/10/java-classquartz.html
 */
package com.advantech.endpoint;

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.PropertiesReader;
import com.advantech.quartzJob.PollingSensorStatus;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint(
        value = "/echo"
//        , 
//        configurator = WebSocketConfig.class
)
public class Endpoint {

    private static final Logger log = LoggerFactory.getLogger(Endpoint.class);
    private static final Queue<Session> queue = new ConcurrentLinkedQueue<>();

    private static final JobKey JOB_KEY;
    private static final TriggerKey TRIGGER_KEY;
    private static final String POLLING_FREQUENCY;

    static {
        String JOB_NAME = "JOB1";
        JOB_KEY = new JobKey(JOB_NAME, JOB_NAME + "Group");
        TRIGGER_KEY = new TriggerKey(JOB_NAME + "Trigger", JOB_NAME + "Group");
        POLLING_FREQUENCY = PropertiesReader.getInstance().getEndpointQuartzTrigger();
    }

    @OnOpen
    public void onOpen(final Session session) {
//        HandshakeRequest req = (HandshakeRequest) conf.getUserProperties().get("handshakereq");
//        Map<String,List<String>> headers = req.getHeaders();
        queue.add(session);
//        System.out.println("New session opened: " + session.getId());

        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        int a = queue.size();
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
        queue.remove(session);
//        System.out.println("session closed: " + session.getId());

        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
        if (queue.isEmpty()) {
            unPollingDB();
        }
    }

    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
//        System.err.println("Error on session " + session.getId());
    }

    ///Brocast the servermessage to all online users.
    public static void sendAll(String msg) {
        try {
            /* Send the new rate to all open WebSocket sessions */
            ArrayList<Session> closedSessions = new ArrayList<>();
            for (Session session : queue) {
                if (!session.isOpen()) {
//                    System.err.println("Closed session: " + session.getId());
                    closedSessions.add(session);
                } else {
                    session.getBasicRemote().sendText(msg);
                }
            }
            queue.removeAll(closedSessions);
//            System.out.println("Sending " + msg + " to " + queue.size() + " clients");
        } catch (Throwable ex) {
            log.error(ex.toString());
        }
    }

    // Generate when connect users are at least one.
    private void pollingDBAndBrocast() {
        try {
            CronTrigMod.getInstance().generateAJob(PollingSensorStatus.class, JOB_KEY, TRIGGER_KEY, POLLING_FREQUENCY);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    // Delete when all users are disconnect.
    private void unPollingDB() {
        try {
            CronTrigMod.getInstance().removeAJob(JOB_KEY, TRIGGER_KEY);
//            System.out.println("trigger has been removed");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }
}
