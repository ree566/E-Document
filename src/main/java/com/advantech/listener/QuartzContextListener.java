/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.model.BasicDAO;
import com.advantech.quartzJob.DataTransformer;
import com.advantech.helper.ThreadLocalCleanUtil;
import com.advantech.endpoint.SensorEndpoint;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Wei.Cheng
 */
public class QuartzContextListener implements ServletContextListener {

    //this listener is unused http://stackoverflow.com/questions/19573457/simple-example-for-quartz-2-2-and-tomcat-7
    //quartz only need to modify at web.xml & the quartz properties/xml to start, stop, wait
    private final SensorEndpoint se = new SensorEndpoint();
    boolean endpointChangeFlag = false;
//    
//    private Logger logger = Logger.getLogger(QuartzContextListener.class);
    public QuartzContextListener() {
//        getLoggerExtender();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        BasicConfigurator.configure();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataTransformer.initInnerObjs();
        BasicDAO.objectInit();
//        logger.info("contextDestroyed");
//        logger = LogManager.getLogger(QuartzContextListener.class);
//        WebApplicationContext webApplicationContext = (WebApplicationContext) sce
//                .getServletContext()
//                .getAttribute(
//                        WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
//        org.quartz.impl.StdScheduler startQuertz = (org.quartz.impl.StdScheduler) webApplicationContext
//                .getBean("startQuertz");
//        if (startQuertz != null) {
//            startQuertz.shutdown();
//        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        ThreadLocal<Integer> tlocal = new ThreadLocal<Integer>();  
//        ClassFactory.cleanCache();
//        try {
//            if (scheduler != null) {
//                scheduler.shutdown();
//                tlocal.remove();
        ThreadLocalCleanUtil.clearThreadLocals();
//            ThreadLocalCleanUtil.closeLog4j();
        //web service當tomcat在做reload會有資源為釋放之情形(多次reload可能會memory leak)
        //http://timen-zbt.iteye.com/blog/1814795
//            Thread.sleep(1000);
//            }
//            System.gc();
//        } catch (InterruptedException ex) {
//            logger.error(ex);
//        }
//        Configurator.shutdown((LoggerContext) LogManager.getContext());
        //安裝quartz後當tomcat重新啟動時會出現memory leak(http://www.cnblogs.com/leeying/p/3782102.html)
        //使用以上方法

    }
}
