/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Flow;
import com.advantech.model.Cobot;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeAutouploadSetting;
import com.advantech.model.WorktimeFormulaSetting;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.service.WorktimeUploadMesService;
import com.advantech.webservice.port.FlowUploadPort;
import com.advantech.webservice.port.MaterialPropertyUploadPort;
import com.advantech.webservice.port.MtdTestIntegrityQueryPort;
import com.advantech.webservice.port.StandardtimeUploadPort;
import com.advantech.webservice.unmarshallclass.MtdTestIntegrity;
import com.fasterxml.jackson.core.JsonProcessingException;
import static com.google.common.collect.Lists.newArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
import static org.junit.Assert.*;
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

    @Test
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

//    @Test
    @Transactional
    @Rollback(false)
    public void testWorktime2() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        List<String> models = newArrayList(
                "MIT-W101-Q24DNB00E",
                "MIT-W101-Q14DNBF2E",
                "MIT-W101-Q04DNB00E",
                "MIT-W101-Q04DNBF2E",
                "MIT-W101-Q04DEBF2E",
                "MIT-W101-Q24DNA00E",
                "MIT-W101-Q04DNA00E",
                "MIT-W101-Q24DNW00E",
                "MIT-W101-Q24DNBF2E",
                "MIT-W101-Q04DNAF2E",
                "MIT-W101-Q04DNW00E",
                "MIT-W101-Q04DNWF2E",
                "MITW101SOR10A0E-ES",
                "MITW101BOV10A0E-ES",
                "MIT-W101-Q04DCA00E",
                "MIT-W101-Q04DCAF2E",
                "MITW101SRL10A0E-ES",
                "MITW101LVN10A0E-ES",
                "MIT-W101-BOV10A0E",
                "MIT-W101-BOV20A0E",
                "MIT-W101-LVN10A0E",
                "MIT-W101-LVN10A1E",
                "MIT-W101-ZIM10A0E",
                "MIT-W101-COV20A0E",
                "MIT-W101-COV20A1E",
                "MITW101COV20A2E-ES",
                "MIT-W101-COV20A2E"
        );
        List<Worktime> l = session.createCriteria(Worktime.class).add(Restrictions.in("modelName", models)).list();

        for (Worktime w : l) {
            System.out.println(w.getModelName());
            w.setMacPrintedQty(2);
            session.merge(w);
        }
    }

    @Autowired
    private FlowUploadPort flowPort;

//    @Test
    @Transactional
    @Rollback(false)
    public void testWorktime3() throws Exception {
        Session session = sessionFactory.getCurrentSession();

        List<String> models = newArrayList(
                "SEP-ACBPI3-AE", "SEP-ACBPIA-AE", "SEP-ACBPP5-AE", "SEP-ACBU10-AE", "SEP-AID1TP-AE", "SEP-AID2R2-AE", "SEP-AID2R4-AE",
                "SEP-AID2U3-AE", "SEP-AID4R2-AE", "SEP-AID4R4-AE", "SEP-AIDAUD-AE", "SEP-AIDDIO-AE", "SEP-AIDFCA-AE", "SEP-AIDFPB-AE",
                "SEP-AIDGAN-AE", "SEP-AIDPOE-AE", "SEP-AIDRAM-AE", "SEP-AMEAPC-AE", "SEP-AMEDPC-AE", "SEP-AMEFFT-AE", "SEP-AMEFKT-AE",
                "SEP-AMEMEH-AE", "SEP-AMEMHD-AE", "SEP-AMEP10-AE", "SEP-AMEP16-AE", "SEP-AMEP18-AE", "SEP-AMEP21-AE", "SEP-AMEV10-AE",
                "SEP-AMEV15-AE", "SEP-AMEVS2-AE", "SEP-AMEVS4-AE", "SEP-AMFSTN-AE", "SEP-AMGR15-AE", "SEP-AMGW10-AE", "SEP-AMGW15-AE",
                "SEP-AMGW19-AE", "SEP-AMGW22-AE", "SEP-AOSW81-AE", "SEP-AOSWN7-AE", "SEP-AOSWS7-AE", "SEP-APSA15-AE", "SEP-APSA18-AE",
                "SEP-APSA60-AE", "SEP-APSUBT-AE", "SEP-ASRC16-AE", "SEP-ASRC32-AE", "SEP-ASRH1T-AE", "SEP-ASRH50-AE", "SEP-ASRS24-AE",
                "SEP-ASRS80-AE", "SEP-ASRU32-AE", "SES-ACBMOK-AE", "SES-ACBPI3-AE", "SES-ACBPIA-AE", "SES-ACBPP5-AE", "SES-ACBU10-AE",
                "SES-AID2R2-AE", "SES-AID2R4-AE", "SES-AID2U3-AE", "SES-AID4R2-AE", "SES-AID4R4-AE", "SES-AIDAUD-AE", "SES-AIDC5M-AE",
                "SES-AIDDIO-AE", "SES-AIDFCA-AE", "SES-AIDFPB-AE", "SES-AIDGAN-AE", "SES-AIDRAM-AE", "SES-AIDWAN-AE", "SES-AMEFKT-AE",
                "SES-AMEMBD-AE", "SES-AMEMEH-AE", "SES-AMEMHD-AE", "SES-AMEMPB-AE", "SES-AMEMPS-AE", "SES-AMEP10-AE", "SES-AMEP16-AE",
                "SES-AMEP18-AE", "SES-AMEP21-AE", "SES-AMEV10-AE", "SES-AMEV15-AE", "SES-AMEVS2-AE", "SES-AMEVS4-AE", "SES-APSA15-AE",
                "SES-APSA18-AE", "SES-APSA60-AE", "SES-APSUBT-AE", "SES-ASRC32-AE", "SES-ASRH1T-AE", "SES-ASRH50-AE", "SES-ASRS24-AE",
                "SES-ASRS80-AE", "SES-ASRU32-AE", "SES-PS318W-B081A", "SES-AIDLDR-AE", "SES-AIDLDT-AE", "SES-AIDLDV-AE", "SEP-AIDLDR-AE",
                "SEP-AIDLDT-AE", "SEP-AIDLDV-AE", "SES-ASRM32-AE", "SES-ASRM64-AE", "SES-AIDTPM-AE", "SES-AMEV12-AE", "SES-ASRM25-AE",
                "SEP-AIDTPM-AE", "SEP-AMEV12-AE", "SEP-ASRM25-AE", "SEP-ASRM32-AE", "SEP-ASRM64-AE", "SEP-AOSW1E-AE", "SEP-AOSW1P-AE",
                "SEP-AOSW1U-AE", "SEP-AIDDVA-AE", "SEP-AIDDVI-AE", "SES-AIDDVA-AE", "SES-AIDDVI-AE", "SES-AID4GE-AE", "SES-AIDAUM-AE",
                "SES-AMEMBO-AE", "SEP-AID4GE-AE", "SEP-AID4GU-AE", "SEP-AIDAUM-AE", "SEP-AMEP12-AE", "SES-AMEP12-AE", "SES-AMEHDK-AE",
                "SEP-AMEHDK-AE", "SEP-ASRC16-BE", "SES-AID4GU-AE", "SEP-ASWMCF-AE", "SES-ASWMCF-AE", "SEP-AIDETM-AE", "SEP-ACBMCK-AE",
                "SES-ACBMCK-AE", "SES-ASRC16-AE", "SEP-AMGR12-AE", "SEP-AMGW12-AE", "SEP-ASRS12-AE", "SEP-ASRS25-AE", "SEP-AC4G5M-AE",
                "SEP-AMEDIN-AE", "SEP-ASRM12-AE", "SES-ABCTPM-AE", "SES-AC4G5M-AE", "SES-ACBWAN-AE", "SES-AIDWA2-AE", "SES-AMEBSC-AE",
                "SES-AMEDIN-AE", "SES-ASDI16-AE", "SES-ASDI64-AE", "SES-ASRM12-AE", "SEP-BMI121-B0B1AE", "SEP-AID2M2-AE", "SEP-ACBWAN-AE",
                "SEP-AID8AI-AE", "SES-AID2M2-AE", "SES-AID8AI-AE", "SES-AID1TP-AE", "SES-APSA60-BE", "SEP-APSA60-BE"
        );

        List<Worktime> l = session.createCriteria(Worktime.class).add(Restrictions.in("modelName", models)).list();

        Flow flow = session.get(Flow.class, 246);
        for (Worktime w : l) {
            System.out.println(w.getModelName());

            w.setFlowByPackingFlowId(flow);
            w.setWeight(BigDecimal.ONE);

            session.merge(w);
            flowPort.update(w);
        }

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testWorktimeTestStationInfo() throws Exception {

        Session session = sessionFactory.getCurrentSession();
        Worktime w = session.get(Worktime.class, 11336);

        Integer i = 1;
        assertEquals(w.getT1ItemsQty(), i);
        assertEquals(w.getT1StatusQty(), i);

        assertTrue(w.getT2ItemsQty() == null);
        assertTrue(w.getT2StatusQty() == null);

//        HibernateObjectPrinter.print(w.getWorktimeTestStationInfos());
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testWorktimeForBatchUpdate() throws Exception {
        Session session = sessionFactory.getCurrentSession();

        List<Worktime> l = session.createCriteria(Worktime.class).list();
        List<BigDecimal> filterValues = newArrayList(new BigDecimal(0.0800),
                new BigDecimal(0.0500),
                new BigDecimal(0.1000),
                new BigDecimal(0.5000));

        assertTrue(l.size() > 5000);

        DecimalFormat df = new DecimalFormat("#,###.0000");

        List<Worktime> filterDatas = l.stream()
                .filter(w -> {
                    for (BigDecimal v : filterValues) {
                        BigDecimal value = w.getWeightAff();
                        if (v.setScale(4, RoundingMode.HALF_DOWN).compareTo(value.setScale(4, RoundingMode.HALF_DOWN)) == 0) {
                            System.out.printf("\t \t %s / %s \r\n", df.format(v), df.format(value));
                            return true;
                        }
                        System.out.printf("%s / %s \r\n", df.format(v), df.format(value));
                    }
                    return false;
                })
                .collect(toList());

        assertEquals(1626, filterDatas.size());

        List<WorktimeMaterialPropertyUploadSetting> setting = newArrayList(session.get(WorktimeMaterialPropertyUploadSetting.class, 22));
        materialPropertyUploadPort.initSettings(setting);

        for (Worktime w : filterDatas) {
            w.setWeightAff(new BigDecimal(0.04));
            try {
                materialPropertyUploadPort.update(w);
            } catch (Exception ex) {
                System.out.println(w.getModelName());
                System.out.println(ex);
            }
            session.merge(w);
        }

    }

    @Autowired
    private MtdTestIntegrityQueryPort mtdTestIntegrityQueryPort;

//    @Test
    @Transactional
    @Rollback(false)
    public void testRetrieveTEstIntegrity() {
        Session session = sessionFactory.getCurrentSession();

        List<Worktime> l = session.createCriteria(Worktime.class).list();

        l.forEach(w -> {
            List<MtdTestIntegrity> mesTestData;
            try {
                mesTestData = mtdTestIntegrityQueryPort.query(w);
                MtdTestIntegrity t1Data = mesTestData.stream().filter(t -> "T1".equals(t.getStationName())).findFirst().orElse(null);
                MtdTestIntegrity t2Data = mesTestData.stream().filter(t -> "T2".equals(t.getStationName())).findFirst().orElse(null);

                if (t1Data != null) {
                    w.setT1ItemsQty(t1Data.getItemCnt());
                    w.setT1StatusQty(t1Data.getStateCnt());
                }

                if (t2Data != null) {
                    w.setT2ItemsQty(t2Data.getItemCnt());
                    w.setT2StatusQty(t2Data.getStateCnt());
                }
                session.merge(w);
            } catch (Exception ex) {
                System.out.println(w.getModelName());
                System.out.println(ex);
            }
        });
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testMachineWorktime() {
        Session session = sessionFactory.getCurrentSession();
        List<Worktime> l = session
                .createQuery("select w from Worktime w", Worktime.class)
                .getResultList();
        assertEquals(5615, l.size());

        l.forEach(w -> {
            if (!w.getCobots().isEmpty()) {
                this.worktimeService.setCobotWorktime(w);
                session.save(w);
            }
        });
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testModAssyStationAndPkgStation() {
        Session session = sessionFactory.getCurrentSession();

        List<Worktime> l = session
                .createQuery("select w from Worktime w inner join w.worktimeFormulaSettings", Worktime.class)
                .getResultList();

        BigDecimal five = new BigDecimal(5);

        List<Worktime> needModWorktime = l.stream()
                .filter(w -> (w.getAssy().compareTo(five) <= 0 && w.getAssyStation() == 1)
                || (w.getPacking().compareTo(five) <= 0 && w.getPackingStation() == 1))
                .collect(toList());

        assertEquals(needModWorktime.size(), 2620);

        needModWorktime.forEach(w -> {
            System.out.println(w.getModelName());
            WorktimeFormulaSetting formula = w.getWorktimeFormulaSettings().get(0);
            if (w.getAssy().compareTo(five) >= 0) {
                formula.setAssyStation(1);
                w.setDefaultAssyStation();
            }
            if (w.getPacking().compareTo(five) >= 0) {
                formula.setPackingStation(1);
                w.setDefaultPackingStation();
            }
            session.save(formula);
            session.save(w);
        });
    }

}
