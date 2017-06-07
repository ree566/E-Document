/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.TextMessage;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class SocketController {

    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);

    @Autowired
    private SocketHandler socketHandler;

    @RequestMapping(value = "/slogin/{name}")
    public String login(HttpSession session, @PathVariable(value = "name") String name) {
        logger.info("用户" + name + "登录了建立连接啦");

        session.setAttribute("user", name);

        return "home";
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String sendMessage() {

//        socketHandler.sendMessageToUser("liulichao", new TextMessage("这是一条测试的消息"));
        socketHandler.sendMessageToUsers(new TextMessage("这是一条测试的消息"));

        return "message";
    }

}
