/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Wei.Cheng
 */
//@WebAppConfiguration
//@ContextConfiguration(locations = {
//    "classpath:servlet-context.xml",
//    "classpath:hibernate.cfg.xml"
//})
//@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateTest {

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AuditService auditService;

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    CRUD testing.
//    @Transactional
//    @Rollback(true)
//    @Test
    public void test() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        AuditReader reader = AuditReaderFactory.get(session);
        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, true);
        // if you want revision properties like revision number/type etc
        //                .addProjection(AuditEntity.revisionNumber())
        // for your normal entity properties
        //                .addProjection(AuditEntity.id())

//        q.addProjection(AuditEntity.selectEntity(false));
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

//    @Transactional
//    @Rollback(true)
//    @Test
    public void testHibernateQuery() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<Worktime> cq = cb.createQuery(Worktime.class);
//        Root<Worktime> root = cq.from(Worktime.class);
//        cq.select(root);
////        query.where(cb.equal(root.get("id"), 8592));
//        cq.orderBy(cb.desc(root.get("id")));
//        TypedQuery<Worktime> q = session.createQuery(cq);
//        q.setFirstResult(1);
//        q.setMaxResults(5);

    }

}
