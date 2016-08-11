/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.service.BasicService;
import com.advantech.service.LineService;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SessionListener implements HttpSessionListener {

    private static final Logger log = LoggerFactory.getLogger(SessionListener.class);
    private static boolean modifyFlag = true;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("session create");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LineService service = BasicService.getLineService();
        if (modifyFlag == true) {
            service.loginBAB(7);
            modifyFlag = false;
            log.info("session destory, open line");
        } else {
            service.logoutBAB(7);
            modifyFlag = true;
            log.info("session destory, close line");
        }
    }

}
