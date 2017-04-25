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

import com.advantech.entity.BAB;
import com.advantech.service.BasicService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint(value = "/echo6/{clientId}")
public class Endpoint6 {

    private static final Logger log = LoggerFactory.getLogger(Endpoint6.class);
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static final Gson gson = new Gson();

    private static final HashMap<Integer, List<BAB>> processingBAB = new HashMap();

    static {
        syncCurrentBabStatus();
    }

    private static void groupBab(List<BAB> l) {
        for (BAB b : l) {
            Integer line = b.getLine();
            if (!processingBAB.containsKey(line)) {
                List<BAB> list = new ArrayList();
                list.add(b);
                processingBAB.put(line, list);
            } else {
                processingBAB.get(line).add(b);
            }
        }
    }

    private static void syncCurrentBabStatus() {
        processingBAB.clear();
        List l = BasicService.getBabService().getTodayBAB();
        groupBab(l);
    }

    public static void syncAndEcho() {
        syncCurrentBabStatus();
        echoStatus();
    }

    public static void addNewBab(BAB b) {
        System.out.println("update bab");
//        sendAll(new JSONObject().put("action", "update").put("status", gson.toJson(b)).toString());
        processingBAB.get(b.getLine()).add(b);
        echoStatus();

    }

    @OnOpen
    public void onOpen(@PathParam("clientId") String clientId, final Session session) {
        //Push the current status on client first connect
        sessions.add(session);
        echoStatus();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //無作用，目前暫時當作echo測試
        System.out.println("received msg " + message + " from " + session.getId());

        if (message.equals("sync")) {
            syncCurrentBabStatus();
            echoStatus();
        } else if (message.equals("clear")) {
            clearProcessingBABStatus();
        }

        sendAll("user" + session.getId() + "> " + message);

    }

    private static void echoStatus() {
        JSONObject obj = new JSONObject();
        obj.put("action", "init");
        obj.put("status", processingBAB);
        sendAll(obj.toString());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
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

    public static void clearSessions() {
        sessions.clear();
    }

    public static void clearProcessingBABStatus() {
        processingBAB.clear();
        echoStatus();
    }
}
