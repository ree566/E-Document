/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.WorktimeStandardtimeUploadPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static junit.framework.Assert.assertEquals;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.internal.property.EntityPropertyName;
import org.hibernate.envers.query.internal.property.ModifiedFlagPropertyName;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //CRUD testing.
    @Transactional
    @Rollback(true)
//    @Test
    public void testSheetView() throws Exception {
        Worktime w = worktimeService.findByModel("UTC-542FP-ATB0E");
        port.uploadStandardTime(w);
    }

//    @Test
    public void testResult() throws Exception {
        List<Worktime> l = worktimeService.findWithFullRelation(new PageInfo().setRows(1));
    }

//    @Test
    public void testCustomValidator() {
        Worktime w = new Worktime();
        w.setFlowByBabFlowId(null);
        Set<ConstraintViolation<Worktime>> constraintViolations = validator.validate(w);
        Iterator it = constraintViolations.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testAudit() throws JsonProcessingException {

        AuditQuery q = AuditReaderFactory.get(sessionFactory.getCurrentSession()).createQuery().forRevisionsOfEntity(Worktime.class, false, true);
        List<Object[]> resultList = q.addProjection(AuditEntity.revisionNumber())
                // for your normal entity properties
                .addProjection(AuditEntity.id())
                .addProjection(AuditEntity.property("assy")) // for each of your entity's properties
                // for the modification properties
                .addProjection(new AuditProperty<>(new ModifiedFlagPropertyName(new EntityPropertyName("assy"))))
                .add(AuditEntity.id().eq(8364))
                .getResultList();
        
        HibernateObjectPrinter.print(resultList);
    }

    @Transactional
    @Rollback(true)
//    @Test
    public void testProc() throws Exception {
        PageInfo p = new PageInfo();
        p.setRows(Integer.MAX_VALUE);
        p.setSearchField("modifiedDate");
        p.setSearchOper("gt");
        p.setSearchString("2017-08-02");

        List l = worktimeService.findAll(p);

        assertEquals(26, l.size());
    }

}
