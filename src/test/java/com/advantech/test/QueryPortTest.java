/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.StandardWorkReasonQueryPort;
import com.advantech.webservice.port.FlowRuleQueryPort;
import com.advantech.webservice.port.MaterialFlowQueryPort;
import com.advantech.webservice.port.MaterialPropertyQueryPort;
import com.advantech.webservice.port.MaterialPropertyValueQueryPort;
import com.advantech.webservice.port.MaterialPropertyUserPermissionQueryPort;
import com.advantech.webservice.port.MesUserInfoQueryPort;
import com.advantech.webservice.port.ModelResponsorQueryPort;
import com.advantech.webservice.port.SopQueryPort;
import com.advantech.webservice.port.StandardWorkTimeQueryPort;
import com.advantech.webservice.unmarshallclass.StandardWorkReason;
import com.advantech.webservice.unmarshallclass.FlowRule;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import com.advantech.webservice.unmarshallclass.MaterialProperty;
import com.advantech.webservice.unmarshallclass.MaterialPropertyUserPermission;
import com.advantech.webservice.unmarshallclass.MaterialPropertyValue;
import com.advantech.webservice.unmarshallclass.StandardWorkTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import static junit.framework.Assert.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
//    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class QueryPortTest {

    @Autowired
    private SopQueryPort sopQueryPort;

    @Autowired
    private ModelResponsorQueryPort mrQueryPort;

    @Autowired
    private MesUserInfoQueryPort mesUserQueryPort;

    @Autowired
    private FlowRuleQueryPort flowRuleQueryPort;

    @Autowired
    private MaterialFlowQueryPort materialFlowQueryPort;

    @Autowired
    private MaterialPropertyUserPermissionQueryPort materialPropertyUserPermissionQueryPort;

    @Autowired
    private MaterialPropertyQueryPort materialPropertyQueryPort;

    @Autowired
    private MaterialPropertyValueQueryPort materialPropertyValueQueryPort;

    @Autowired
    private StandardWorkReasonQueryPort errorGroupQueryPort;

    @Autowired
    private WorktimeService worktimeService;

    private Worktime w;

    @Autowired
    private StandardWorkTimeQueryPort worktimeQueryPort;

    @Before
    public void init() {
//        w = worktimeService.findByPrimaryKey(5352);
    }

//    @Test
    public void testSopQueryPort() throws Exception {
        sopQueryPort.setTypes("T1");
        List l = sopQueryPort.query(w);
        assertEquals(1, l.size());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testModelResponsorQueryPort() throws Exception {
        List l = mrQueryPort.query(w);
        assertEquals(3, l.size());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testMesUserInfoQueryPort() throws Exception {
        List l = mesUserQueryPort.query(w);
        assertEquals(3, l.size());
        HibernateObjectPrinter.print(l);

//        Map m = mesUserQueryPort.transformData(w);
//        HibernateObjectPrinter.print(m);
    }

//    @Test
    public void testFlowRuleQueryPort() throws Exception {
        FlowRule rule = flowRuleQueryPort.query("B", "empty");
        assertNull(rule);
        HibernateObjectPrinter.print(rule);
    }

//    @Test
    public void testMateriaFlowQueryPort() throws Exception {
        Map m = materialFlowQueryPort.transformData(w);
        assertEquals(4, m.size());
        HibernateObjectPrinter.print(m);

        List<MaterialFlow> l = materialFlowQueryPort.query(w);
        assertEquals(4, l.size());
        HibernateObjectPrinter.print(l);

        assertEquals(34200, l.get(0).getId());
        assertEquals(14360, l.get(0).getItemId());
        assertEquals(719, l.get(0).getFlowRuleId());
    }

//    @Test
    public void testMaterialPropertyUserPermissionQueryPort() throws Exception {
        List<MaterialPropertyUserPermission> l = materialPropertyUserPermissionQueryPort.query("A-7060");
        assertEquals(4, l.size());
        assertEquals("BD", l.get(0).getMaterialPropertyNo());
    }

//    @Test
    public void testMaterialPropertyQueryPort() throws Exception {
        List<MaterialProperty> l = materialPropertyQueryPort.query("FC");
        assertEquals(1, l.size());
        assertEquals("FC", l.get(0).getMatPropertyNo());
        System.out.println(l.get(0).getAffPropertyType());
    }

//    @Test
    public void testMaterialPropertyValueQueryPort() throws Exception {
        Worktime w1 = worktimeService.findByModel("2063002307");
        assertNotNull(w1);
        List<MaterialPropertyValue> l = materialPropertyValueQueryPort.query(w1);
        assertEquals(6, l.size());
        MaterialPropertyValue value = l.stream()
                .filter(v -> "KB".equals(v.getMatPropertyNo())).findFirst().orElse(null);
        assertNotNull(value);
        assertEquals(null, value.getAffPropertyValue());
        assertEquals("N", value.getValue());
    }

//    @Test
    public void testRetriveMatPropertyValue() throws Exception {
        List<Worktime> worktimes = worktimeService.findAll();
        for (Worktime worktime : worktimes) {
            System.out.println("Update " + worktime.getModelName());
            List<MaterialPropertyValue> l = materialPropertyValueQueryPort.query(worktime);
            MaterialPropertyValue acw = l.stream().filter(m1 -> "HA".equals(m1.getMatPropertyNo())).findFirst().orElse(null);
            MaterialPropertyValue llt = l.stream().filter(m1 -> "HL".equals(m1.getMatPropertyNo())).findFirst().orElse(null);
            MaterialPropertyValue testP = l.stream().filter(m1 -> "HP".equals(m1.getMatPropertyNo())).findFirst().orElse(null);
            MaterialPropertyValue ir = l.stream().filter(m1 -> "HI".equals(m1.getMatPropertyNo())).findFirst().orElse(null);
            if (acw != null) {
                worktime.setAcwVoltage(acw.getValue());
            }
            if (llt != null) {
                worktime.setLltValue(llt.getValue());
            }
            if (testP != null) {
                worktime.setTestProfile(testP.getValue());
            }
            if (ir != null) {
                worktime.setIrVoltage(ir.getValue());
            }
        }
        worktimeService.update(worktimes);
    }

    @Autowired
    private SessionFactory sessionFactory;

//    @Test
//    @Rollback(false)
    public void updateAffValue() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        List<Worktime> worktimes = worktimeService.findAll();
        for (Worktime worktime : worktimes) {
            System.out.println("Update " + worktime.getModelName());
            List<MaterialPropertyValue> l = materialPropertyValueQueryPort.query(worktime);
            MaterialPropertyValue weight = l.stream().filter(we -> "IW".equals(we.getMatPropertyNo())).findFirst().orElse(null);
            if (weight != null) {
                String affRemote = weight.getAffPropertyValue();
                if (NumberUtils.isNumber(affRemote)) {
                    worktime.setWeightAff(new BigDecimal(affRemote));
                } else {
                    worktime.setWeightAff(BigDecimal.ZERO);
                }
                session.merge(worktime);
            } else {
                System.out.println("IW not found");
            }
        }
    }

//    @Test
    @Rollback(false)
    public void testRetriveMatPropertyValue2() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        List<Worktime> worktimes = worktimeService.findAll();
        for (Worktime worktime : worktimes) {
            List<MaterialPropertyValue> l = materialPropertyValueQueryPort.query(worktime);
            MaterialPropertyValue bq = l.stream().filter(m1 -> "BQ".equals(m1.getMatPropertyNo())).findFirst().orElse(null);
            if (bq != null) {
                System.out.printf("ModelName: %s, BQ-Value: %s \n", worktime.getModelName(), bq.getValue());
                worktime.setBurnInQuantity(Integer.parseInt(bq.getValue()));
            } else {
                worktime.setBurnInQuantity(0);
            }
            session.merge(worktime);
        }
    }

//    @Test
    public void testErrorGroupQueryPort() throws Exception {
        List<StandardWorkReason> l = errorGroupQueryPort.query();

        assertTrue(l != null);
        assertEquals(9, l.size());

        StandardWorkReason eg = l.get(0);

        assertEquals("A0", eg.getId());
        assertEquals("預估工時修正", eg.getName());

        HibernateObjectPrinter.print(l);
    }

    @Test
    public void testStandardWorkTimeQueryPort() throws Exception {
        List<StandardWorkTime> l = this.worktimeQueryPort.query("2063002307");
        assertEquals(25, l.size());
        
        HibernateObjectPrinter.print(l);
    }

}
