/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.endpoint;

import com.advantech.helper.CronTrigMod;
import com.advantech.helper.PropertiesReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Wei.Cheng
 */
public abstract class BasicHandler {

    private static final Logger log = LoggerFactory.getLogger(BasicHandler.class);

    @Autowired
    private PropertiesReader p;

    @Autowired
    private CronTrigMod ctm;

    protected final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());

    private String jobName;
    private Class<?> clz;

    protected void init(Class<?> clz, String jobName) {
        this.clz = clz;
        this.jobName = jobName;
    }

    ///Brocast the servermessage to all online users.
    public void sendAll(String msg) {
        try {
            /* Send the new rate to all open WebSocket sessions */
            ArrayList<WebSocketSession> closedSessions = new ArrayList<>();
            for (WebSocketSession session : sessions) {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    TextMessage t = new TextMessage(msg);
                    session.sendMessage(t);
                }
            }
            sessions.removeAll(closedSessions);
        } catch (Throwable ex) {
            log.error(ex.toString());
        }
    }

    // Generate when connect users are at least one.
    protected void pollingDBAndBrocast() {
        try {
            String cron = p.getEndpointPollingCron();
            ctm.scheduleJob(clz, jobName, cron);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    // Delete when all users are disconnect.
    protected void unPollingDB() {
        try {
            ctm.removeJob(jobName);
//            System.out.println("trigger has been removed");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

}
