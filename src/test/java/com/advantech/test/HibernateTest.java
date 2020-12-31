/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.HrcType;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeAutouploadSetting;
import com.advantech.model.WorktimeFormulaSetting;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.service.WorktimeUploadMesService;
import com.advantech.webservice.port.MaterialPropertyUploadPort;
import com.advantech.webservice.port.StandardtimeUploadPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import javax.transaction.Transactional;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static junit.framework.Assert.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateTest {

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AuditService auditService;

    private static Validator validator;

    @Autowired
    private WorktimeUploadMesService worktimeUploadMesService;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    @Transactional
//    @Rollback(true)
//    @Test
    public void testAudit() throws JsonProcessingException {
        DateTime d = new DateTime("2017-09-26").withHourOfDay(0);

        Session session = sessionFactory.getCurrentSession();
        AuditReader reader = AuditReaderFactory.get(session);
        AuditQuery q = reader.createQuery()
                .forRevisionsOfEntity(Worktime.class, false, false)
                .addProjection(AuditEntity.id())
                .add(AuditEntity.id().lt(8607))
                .add(AuditEntity.revisionProperty("REVTSTMP").gt(d.getMillis()))
                .add(AuditEntity.or(
                        AuditEntity.property("assyPackingSop").hasChanged(),
                        AuditEntity.property("testSop").hasChanged()
                ));

        List l = q.getResultList();
        assertEquals(26, l.size());
        HibernateObjectPrinter.print(l);
    }

//    CRUD testing.
//    @Test
//    @Transactional
//    @Rollback(true)
    public void test() throws Exception {
        this.testUpdate();
    }

    public void testUpdate() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Worktime w = (Worktime) session.load(Worktime.class, 17915);
        w.setModelName("TTBB");
        worktimeService.update(w);
        throw new Exception("this is a testing exception");
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

//    @Test
    @Transactional
    @Rollback(false)
    public void testClone() throws Exception {
        Worktime w = worktimeService.findByPrimaryKey(8614);
        assertNotNull(w);

        String modelName = w.getModelName();

        List<String> modelNames = new ArrayList();

        for (int i = 0; i <= 10; i++) {
            modelNames.add(modelName + "-CLONE-" + i);
        }

        worktimeService.insertSeries(modelName, modelNames);
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void deleteClone() throws Exception {
        List<Worktime> l = sessionFactory.getCurrentSession().createQuery("from Worktime w where upper(w.modelName) like '%CLONE%'").list();
        assertEquals(13, l.size());
        l.forEach((w) -> {
            try {
                worktimeService.delete(w);
            } catch (Exception e) {
                System.out.println(w.getModelName() + " delete fail.");
                System.out.println(e);
            }
        });
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testJava8() throws Exception {
        Worktime w = (Worktime) sessionFactory.getCurrentSession().createQuery("from Worktime w where w.id = 17982").uniqueResult();
        assertNotNull(w);
        worktimeUploadMesService.portParamInit();
        worktimeUploadMesService.delete(w);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testLastStatus() {
        Worktime w = worktimeService.findByPrimaryKey(8066);
        Worktime rowLastStatus = (Worktime) auditService.findLastStatusBeforeUpdate(Worktime.class, w.getId());
        System.out.println((int) w.getPartNoAttributeMaintain());
        System.out.println((int) rowLastStatus.getPartNoAttributeMaintain());
        System.out.println((int) '5');
        System.out.println(Objects.equals(w.getPartNoAttributeMaintain(), rowLastStatus.getPartNoAttributeMaintain()));
        System.out.println(Objects.equals((int) w.getPartNoAttributeMaintain(), (int) rowLastStatus.getPartNoAttributeMaintain()));

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testTrimModel() throws JsonProcessingException {
        List<Worktime> l = worktimeService.findAll(new PageInfo());
        assertEquals(10, l.size());

        l.stream().map((w) -> {
            w.setModelName(w.getModelName() + " ");
            return w;
        }).forEachOrdered((w) -> {
            this.removeModelNameExtraSpaceCharacter(w);
        });

        HibernateObjectPrinter.print(l);
    }

    private void removeModelNameExtraSpaceCharacter(Worktime w) {
        String modelName = w.getModelName();
        w.setModelName(removeModelNameExtraSpaceCharacter(modelName));
    }

    private String removeModelNameExtraSpaceCharacter(String modelName) {
        return modelName.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testPojoGetSetByName() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Session session = sessionFactory.getCurrentSession();
        Worktime w = (Worktime) session
                .createQuery("from Worktime w order by id desc")
                .setMaxResults(1)
                .uniqueResult();

        assertNotNull(w);

        Object modelName = PropertyUtils.getProperty(w, "modelName");
        assertEquals("HPC7442MB1707-T", modelName);

        Object t1 = PropertyUtils.getProperty(w, "t1");
        assertNotNull(t1);
        assertTrue(new BigDecimal(40).compareTo((BigDecimal) t1) == 0);

        PropertyUtils.setProperty(w, "t1", new BigDecimal(50));
        t1 = PropertyUtils.getProperty(w, "t1");
        assertNotNull(t1);
        assertTrue(new BigDecimal(50).compareTo((BigDecimal) t1) == 0);

    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testUpdateSetupTime() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Session session = sessionFactory.getCurrentSession();
        List<Worktime> l = session.createCriteria(Worktime.class).list();

        for (Worktime w : l) {
            System.out.println(w.getModelName());
            w.setDefaultSetupTime();
            session.merge(w);
        }

    }

    public String currencyFormat(BigDecimal n) {
        return NumberFormat.getNumberInstance().format(n);
    }

    @Autowired
    private StandardtimeUploadPort standardtimePort;

    @Autowired
    private WorktimeAutouploadSettingService worktimeAutouploadSettingService;

//    @Test
    @Transactional
    @Rollback(false)
    public void testWorktime() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, Exception {

        Session session = sessionFactory.getCurrentSession();
        List<Worktime> l = session.createCriteria(Worktime.class).list();

        l = l.stream().filter(p -> p.getModelName().matches("ADAM.*|WISE.*|USB.*")).collect(toList());

        for (Worktime w : l) {
            System.out.println(w.getModelName());

            List<WorktimeFormulaSetting> settings = w.getWorktimeFormulaSettings();
            WorktimeFormulaSetting setting = settings.get(0);
            setting.setAssyStation(0);
            setting.setPackingStation(0);
            w.setAssyStation(1);
            w.setPackingStation(1);

            session.update(setting);
            session.merge(w);
        }

        List<WorktimeAutouploadSetting> settings = worktimeAutouploadSettingService.findByPrimaryKeys(2, 4, 14, 16);
        standardtimePort.initSettings(settings);

        for (Worktime w : l) {
            w.setReasonCode("A0");
            standardtimePort.update(w);
        }

    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testWorktimeMacTotalQty() {
        Session session = sessionFactory.getCurrentSession();
        List<Worktime> l = session.createCriteria(Worktime.class).list();
        Worktime w = l.get(0);
        assertTrue(w.getMacTotalQty() == null);

        w.setMacTotalQty(0);
        session.merge(w);
        assertTrue(w.getMacTotalQty() == 0);
    }

    @Autowired
    private MaterialPropertyUploadPort materialPropertyUploadPort;
    
//    @Test
    @Transactional
    @Rollback(false)
    public void testWorktime1() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        List<Worktime> l = session.createCriteria(Worktime.class).add(Restrictions.between("id", 10739, 11172)).list();
        List<WorktimeMaterialPropertyUploadSetting> setting = session.createCriteria(WorktimeMaterialPropertyUploadSetting.class).list();
        assertEquals(l.size(), 434);
        
        materialPropertyUploadPort.initSettings(setting);

        for (Worktime w : l) {
            if (Objects.equals('N', w.getPartLink())) {
                w.setPartLink('Y');
                session.merge(w);
                materialPropertyUploadPort.update(w);
            }
        }
    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testHrcType() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        HrcType ht = session.get(HrcType.class, 1);
        HibernateObjectPrinter.print(ht);
    }

}
