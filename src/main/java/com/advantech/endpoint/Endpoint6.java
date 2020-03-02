/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.endpoint;

import com.advantech.model.db1.Bab;
import com.advantech.service.db1.SqlViewService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
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
 * @author Wei.Cheng
 */
public class Endpoint6 implements WebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(Endpoint6.class);
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    private Map<Integer, List<Bab>> processingBab = new HashMap();

    @Autowired
    private SqlViewService sqlViewService;

    @PostConstruct
    private void syncCurrentBabStatus() {
        processingBab.clear();
        List<Bab> l = sqlViewService.findBabLastInputPerLine();
        processingBab = l.stream().collect(groupingBy(b -> b.getLine().getId(), toList()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
        //Push the current status on client first connect
        sessions.add(wss);
        echoStatus();
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        //無作用，目前暫時當作echo測試
        System.out.println("received msg " + wsm.getPayload() + " from " + wss.getId());

        if (wsm.getPayload().equals("sync")) {
            syncCurrentBabStatus();
            echoStatus();
        } else if (wsm.getPayload().equals("clear")) {
            clearProcessingBabStatus();
        }

        sendAll("user" + wss.getId() + "> " + wsm.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        sessions.remove(wss);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        sessions.remove(wss);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void echoStatus() {
        JSONObject obj = new JSONObject();
        obj.put("action", "init");
        obj.put("status", processingBab);
        sendAll(obj.toString());
    }

    ///Brocast the servermessage to all online users.
    public void sendAll(String msg) {
        /* Send the new rate to all open WebSocket sessions */
        ArrayList<WebSocketSession> closedSessions = new ArrayList<>();
        sessions.forEach((session) -> {
            try {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    synchronized (session) {
                        TextMessage t = new TextMessage(msg);
                        session.sendMessage(t);
                    }
                }
            } catch (IOException ex) {
                log.error("Error cause on session id " + session.getId(), ex);
            }
        });
        sessions.removeAll(closedSessions);
    }

    public void clearSessions() {
        sessions.clear();
    }

    public void clearProcessingBabStatus() {
        processingBab.clear();
        echoStatus();
    }

    public void syncAndEcho() {
        this.syncCurrentBabStatus();
        this.echoStatus();
    }

}
