/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.BabStatus;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestExcelExport {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private SessionFactory sessionFactory;

//    @Test
    public void testAddDataToTemp() throws Exception {
        Resource r = resourceLoader.getResource("classpath:excel-template\\report_1_template.xls");
        try (InputStream is = r.getInputStream()) {

            List l = new ArrayList();

            try (OutputStream os = new FileOutputStream("C:\\Users\\Wei.Cheng\\Desktop\\object_collection_output.xls")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Context context = new Context();
                context.putVar("sheetViews", l);
                context.putVar("dateFormat", dateFormat);

                Transformer transformer = TransformerFactory.createTransformer(is, os);
                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
                evaluator.getJexlEngine().setSilent(false);

                JxlsHelper helper = JxlsHelper.getInstance();
                helper.processTemplate(context, transformer);
//                .processTemplate(is, os, context);
            }
        }
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getData() {
        String testModel = "UTC-520FP-IKA0E";
        String lineTypeName = "ASSY";

        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(Bab.class, "b");
        List<Bab> l = c.createAlias("b.line", "l")
                .createAlias("l.lineType", "lt")
                .add(Restrictions.eq("b.modelName", testModel))
                .add(Restrictions.eq("lt.name", lineTypeName))
                .add(Restrictions.eq("b.babStatus", BabStatus.CLOSED))
                .setMaxResults(10)
                .list();

        l.forEach(b -> {
            Hibernate.initialize(b.getBabPcsDetailHistorys());
            Hibernate.initialize(b.getBabAlarmHistorys());
        });
        
        HibernateObjectPrinter.print(l);

//        return l;
    }

}
