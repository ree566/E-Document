/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import static com.advantech.model.Permission.initPermission;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class ContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(ServletContextListener.class);

    public ContextListener() {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            initPermission(
                    Integer.parseInt(context.getInitParameter("VISITOR_PERMISSION")),
                    Integer.parseInt(context.getInitParameter("UNIT_OPERATOR_PERMISSION")),
                    Integer.parseInt(context.getInitParameter("UNIT_LEADER_PERMISSION")),
                    Integer.parseInt(context.getInitParameter("SYSOP_PERMISSION")),
                    Integer.parseInt(context.getInitParameter("TEST_FIELD_ACCESS_LIMIT_PERMISSION"))
            );
        } catch (Exception ex) {
            log.error("Permission init fail");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
