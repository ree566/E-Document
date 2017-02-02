/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.endpoint.Endpoint;
import com.advantech.endpoint.Endpoint2;
import com.advantech.endpoint.Endpoint3;
import com.advantech.endpoint.Endpoint4;
import com.advantech.helper.PropertiesReader;
import com.advantech.helper.ThreadLocalCleanUtil;
import com.advantech.model.BasicDAO;
import com.advantech.service.BasicService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class QuartzContextListener implements ServletContextListener {

    //this listener is unused http://stackoverflow.com/questions/19573457/simple-example-for-quartz-2-2-and-tomcat-7
    //quartz only need to modify at web.xml & the quartz properties/xml to start, stop, wait
    
    private static final Logger log = LoggerFactory.getLogger(QuartzContextListener.class);

    public QuartzContextListener() {
//        getLoggerExtender();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        BasicConfigurator.configure();
        BasicDAO.dataSourceInit();
        PropertiesReader.getInstance();
        BasicService.getLineTypeConfigService().initBasicVariable();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            BasicDAO.objectInit();
//            CronTrigMod.getInstance().unScheduleAllJob();
            
            Endpoint.clearSessions();
            Endpoint2.clearSessions();
            Endpoint3.clearSessions();
            Endpoint4.clearSessions();

            Thread.sleep(3000);
            ThreadLocalCleanUtil.clearThreadLocals();
        } catch (Exception e) {
            log.error(e.toString());
        }

        //web service當tomcat在做reload會有資源為釋放之情形(多次reload可能會memory leak)
        //http://timen-zbt.iteye.com/blog/1814795
        //安裝quartz後當tomcat重新啟動時會出現memory leak(http://www.cnblogs.com/leeying/p/3782102.html)
        //使用以上方法
    }
}
