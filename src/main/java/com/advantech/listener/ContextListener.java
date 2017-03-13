/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ContextListener implements ServletContextListener {

    //this listener is unused http://stackoverflow.com/questions/19573457/simple-example-for-quartz-2-2-and-tomcat-7
    //quartz only need to modify at web.xml & the quartz properties/xml to start, stop, wait
    private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

    public ContextListener() {
//        getLoggerExtender();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        BasicConfigurator.configure();


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

       

    }
}
