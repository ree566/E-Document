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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint(value = "/echo5/{clientId}")
public class Endpoint5 {

    private static final Logger log = LoggerFactory.getLogger(Endpoint5.class);

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(@PathParam("clientId") String clientId, final Session session) {
        //Push the current status on client first connect
        sessions.add(session);
        try {
            sendAll("Hello user " + session.getId());
            session.getBasicRemote().sendText("There are current " + sessions.size() + " users in this endpoint");
            showUrlParam(session);
        } catch (IOException ex) {
            log.error(ex.toString());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //無作用，目前暫時當作echo測試
        System.out.println("received msg " + message + " from " + session.getId());
        sendAll("(" + session.getId() + ") :" + message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
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

    private void showUrlParam(Session session) {
        Map map = session.getRequestParameterMap();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            System.out.println("Receive endpoint param: " + map.get(it.next()));
        }
    }

}
