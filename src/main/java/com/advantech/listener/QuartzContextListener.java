/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.model.BasicDAO;
import com.advantech.helper.ThreadLocalCleanUtil;
import com.advantech.endpoint.Endpoint;
import com.advantech.helper.PropertiesReader;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Wei.Cheng
 */
public class QuartzContextListener implements ServletContextListener {

    //this listener is unused http://stackoverflow.com/questions/19573457/simple-example-for-quartz-2-2-and-tomcat-7
    //quartz only need to modify at web.xml & the quartz properties/xml to start, stop, wait

    public QuartzContextListener() {
//        getLoggerExtender();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        BasicConfigurator.configure();
        BasicDAO.dataSourceInit();
        PropertiesReader.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
            BasicDAO.objectInit();

            Thread.sleep(1000);
            ThreadLocalCleanUtil.clearThreadLocals();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //web service當tomcat在做reload會有資源為釋放之情形(多次reload可能會memory leak)
        //http://timen-zbt.iteye.com/blog/1814795
        //安裝quartz後當tomcat重新啟動時會出現memory leak(http://www.cnblogs.com/leeying/p/3782102.html)
        //使用以上方法
    }
}
