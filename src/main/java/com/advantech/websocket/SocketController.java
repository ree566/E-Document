/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.websocket;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Wei.Cheng
 */
@Controller
//@EnableScheduling
public class SocketController {

//    @Autowired
//    private MessageSendingOperations<OutputMessage> messagingTemplate;

    private static final Map<Integer, OutputMessage> rowStatus;

    static {
        rowStatus = new HashMap<>();
    }

    @RequestMapping("/helloSocket")
    public String index() {
        return "/stomp";
    }

    @RequestMapping("/socketClient")
    public String client() {
        return "/stomp-client";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message, Principal principal) {
        System.out.println("Receive websocket message...");
        String userName = principal.getName();
        OutputMessage om = new OutputMessage(userName, message.getRowId(), message.getAction(), getTime());
        if (message.getAction().equals(RowAction.LOCK.toString())) {
            om = this.lockRow(om);
        } else if (message.getAction().equals(RowAction.UNLOCK.toString())) {
            om = this.unLockRow(om);
        }
        return om;
    }

    @SubscribeMapping("/topic/messages")
    public Map getStatus() {
        return rowStatus;
    }

    private OutputMessage lockRow(OutputMessage om) {
        if (rowStatus.containsKey(om.getRowId())) {
            om = new OutputMessage("SERVER", om.getRowId(), RowAction.ERROR_MESSAGE.toString(), getTime());
        } else {
            rowStatus.put(om.getRowId(), om);
        }
        return om;
    }

    private OutputMessage unLockRow(OutputMessage om) {
        if (rowStatus.containsKey(om.getRowId())) {
            OutputMessage lockedRowMessage = rowStatus.get(om.getRowId());
            if (lockedRowMessage.getUsername().equals(om.getUsername())) {
                rowStatus.remove(om.getRowId());
            } else {
                om = generateErrorMessage(om.getRowId());
            }
        } else {
            om = generateErrorMessage(om.getRowId());
        }
        return om;
    }

    private OutputMessage generateErrorMessage(int errorRowId) {
        return new OutputMessage("SERVER", errorRowId, RowAction.ERROR_MESSAGE.toString(), getTime());
    }

    @Scheduled(fixedDelay = 30000)
    public void sendDataUpdates() {
        System.out.println("Server sending message...");
//        messagingTemplate.convertAndSend("/topic/messages", new OutputMessage("SERVER", "SERVER MESSAGE PER 30 SECOND", getTime()));
    }

    private String getTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    @MessageMapping("/action")
    public void handleAction() throws Exception {
        throw new Exception("This is the testing exception.");
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

}
