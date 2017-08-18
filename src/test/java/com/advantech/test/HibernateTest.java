/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.WorktimeStandardtimeUploadPort;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.internal.property.EntityPropertyName;
import org.hibernate.envers.query.internal.property.ModifiedFlagPropertyName;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    "classpath:servlet-context.xml",
    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateTest {

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private WorktimeStandardtimeUploadPort port;

    @Autowired
    private AuditService auditService;

    private static Validator validator;

    @Autowired
    private WorktimeAutouploadSettingService settingService;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Transactional
    @Rollback(true)
    @Test
    public void test() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        AuditReader reader = AuditReaderFactory.get(session);
        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, true);
        // if you want revision properties like revision number/type etc
        //                .addProjection(AuditEntity.revisionNumber())
        // for your normal entity properties
        //                .addProjection(AuditEntity.id())

        String[] st = this.getAllFields();
//        String[] st = new String[0];

        for (String f : st) {
            if ("id".equals(f) || "bwFields".equals(f) || "worktimeFormulaSettings".equals(f)) {
                continue;
            }
//            q.addProjection(AuditEntity.property(f)); // for each of your entity's properties
            // for the modification properties
            q.addProjection(new AuditProperty<>(new ModifiedFlagPropertyName(new EntityPropertyName(f))));
        }

        q.add(AuditEntity.id().eq(8394));

        List resultList = q.getResultList();

        HibernateObjectPrinter.print(resultList);
    }

    private String[] getAllFields() {
        Worktime w = new Worktime();
        Class objClass = w.getClass();

        List<String> list = new ArrayList<>();
        // Get the public methods associated with this class.
        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && !name.startsWith("setDefault")) {
                list.add(lowerCaseFirst(name.substring(3)));
            }
        }
        return list.toArray(new String[0]);
    }

    private String lowerCaseFirst(String st) {
        StringBuilder sb = new StringBuilder(st);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

}
