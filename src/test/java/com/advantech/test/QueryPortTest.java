/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.FlowRuleQueryPort;
import com.advantech.webservice.port.MaterialFlowQueryPort;
import com.advantech.webservice.port.MesUserInfoQueryPort;
import com.advantech.webservice.port.ModelResponsorQueryPort;
import com.advantech.webservice.port.SopQueryPort;
import com.advantech.webservice.unmarshallclass.FlowRule;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import static junit.framework.Assert.*;
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
@Rollback(true)
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
    private WorktimeService worktimeService;

    private Worktime w;

    @Before
    public void init() {
        w = worktimeService.findByPrimaryKey(5352);
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
        Map m = flowRuleQueryPort.transformData(w);
        assertEquals(4, m.size());
        HibernateObjectPrinter.print(m);

        List<FlowRule> l = flowRuleQueryPort.query(w);
        HibernateObjectPrinter.print(l);
        assertEquals(4, l.size());
        assertEquals("PRE_ASSY-", l.get(0).getName());
        assertEquals("BAB_ASSY-VB-H1-LK-T1-BI", l.get(1).getName());
        assertEquals("TEST_T2-T3(T1-BI)", l.get(2).getName());
        assertEquals("PI-PKG", l.get(3).getName());

        HibernateObjectPrinter.print(l);
    }

    @Test
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
}
