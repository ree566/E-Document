/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.ExcelGenerator;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.FlowService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.MaterialFlowQueryPort;
import com.advantech.webservice.port.ModelResponsorQueryPort;
import com.advantech.webservice.root.Section;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import com.advantech.webservice.unmarshallclass.ModelResponsor;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.transaction.Transactional;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
 * @author Wei.Cheng Only check the field which MES has provide the XML query
 * port.
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
//    "classpath:hibernate.cfg.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(true)
public class RemoteAndLocalDataTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ModelResponsorQueryPort responsorQryPort;

    @Autowired
    private WorktimeService worktimeService;

    private List<Worktime> l;

    @Autowired
    private FlowService flowService;

    @Autowired
    private MaterialFlowQueryPort materialFlowQueryPort;

    @Before
    public void init() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(Worktime.class);
        c.setFetchMode("userByEeOwnerId", FetchMode.EAGER);
        c.setFetchMode("userByQcOwnerId", FetchMode.EAGER);
        c.setFetchMode("userBySpeOwnerId", FetchMode.EAGER);
        c.setFetchMode("preAssy", FetchMode.EAGER);
        c.setFetchMode("flowByBabFlowId", FetchMode.EAGER);
        c.setFetchMode("flowByTestFlowId", FetchMode.EAGER);
        c.setFetchMode("flowByPackingFlowId", FetchMode.EAGER);
//        c.add(Restrictions.eq("id", 8724));
        l = c.list();
    }

//    @Test
    public void test() throws Exception {
        ExcelGenerator generator = new ExcelGenerator();
        List<Map> errors = new ArrayList();
        for (Worktime w : l) {
            System.out.println("ModelName testing: " + w.getModelName());
            List<ModelResponsor> mesData = responsorQryPort.query(w);

            if (!isUserTheSame(w, mesData)) {
                Map m = new HashMap();
                m.put("modelName", w.getModelName());
                m.put("speOwner_local", w.getUserBySpeOwnerId().getJobnumber());
                User eeOwner = w.getUserByEeOwnerId();
                m.put("eeOwner_local", eeOwner == null ? "n/a" : eeOwner.getJobnumber());
                m.put("qcOwner_local", w.getUserByQcOwnerId().getJobnumber());

                Predicate<ModelResponsor> speFilter = resp -> resp.getDeptId() == 19;
                Predicate<ModelResponsor> eeFilter = resp -> resp.getDeptId() == 17;
                Predicate<ModelResponsor> qcFilter = resp -> resp.getDeptId() == 15;
                ModelResponsor speOwnerRemote = FluentIterable.from(mesData).filter(speFilter).first().orNull();
                ModelResponsor eeOwnerRemote = FluentIterable.from(mesData).filter(eeFilter).first().orNull();
                ModelResponsor qcOwnerRemote = FluentIterable.from(mesData).filter(qcFilter).first().orNull();
                m.put("speOwner_remote", speOwnerRemote == null ? "n/a" : speOwnerRemote.getUserNo());
                m.put("eeOwner_remote", eeOwnerRemote == null ? "n/a" : eeOwnerRemote.getUserNo());
                m.put("qcOwner_remote", qcOwnerRemote == null ? "n/a" : qcOwnerRemote.getUserNo());

                errors.add(m);
                System.out.println(w.getModelName() + " testing fail.");
            } else {
                System.out.println(w.getModelName() + " testing pass.");
            }
        }
        if (!errors.isEmpty()) {
            generator.generateWorkBooks(errors);
            generator.outputExcel("conflict");
        }
    }

    private boolean isUserTheSame(Worktime w, List<ModelResponsor> l) {
        Set<String> s1 = new HashSet();
        Set<String> s2 = new HashSet();
        User eeOwner = w.getUserByEeOwnerId();
        if (eeOwner != null) {
            s1.add(eeOwner.getJobnumber());
        }
        s1.add(w.getUserBySpeOwnerId().getJobnumber());
        s1.add(w.getUserByQcOwnerId().getJobnumber());

        l.forEach((m) -> {
            s2.add(m.getUserNo());
        });

        return s1.containsAll(s2);
    }

    @Test
    public void testFlow() throws Exception {
        ExcelGenerator generator = new ExcelGenerator();
        List<Map> errors = new ArrayList();
        String na = "n/a";
        for (Worktime w : l) {
            System.out.println("ModelName testing: " + w.getModelName());
            List<MaterialFlow> mesData = materialFlowQueryPort.query(w);

            if (!isFlowNameTheSame(w, mesData)) {
                Map m = new HashMap();
                m.put("modelName", w.getModelName());
                m.put("preAssy_local", w.getPreAssy() == null ? na : w.getPreAssy().getName());
                m.put("babFlow_local", w.getFlowByBabFlowId() == null ? na : ("empty".equals(w.getFlowByBabFlowId().getName()) ? na : w.getFlowByBabFlowId().getName()));
                m.put("testFlow_local", w.getFlowByTestFlowId() == null ? na : w.getFlowByTestFlowId().getName());
                m.put("packingFlow_local", w.getFlowByPackingFlowId() == null ? na : w.getFlowByPackingFlowId().getName());

                MaterialFlow m_preAssy = mesData.stream().filter(ma -> ma.getUnitNo().equals(Section.PREASSY.getCode())).findFirst().orElse(null);
                MaterialFlow m_babFlow = mesData.stream().filter(ma -> ma.getUnitNo().equals(Section.BAB.getCode())).findFirst().orElse(null);
                MaterialFlow m_testFlow = mesData.stream().filter(ma -> ma.getUnitNo().equals(Section.TEST.getCode())).findFirst().orElse(null);
                MaterialFlow m_packageFlow = mesData.stream().filter(ma -> ma.getUnitNo().equals(Section.PACKAGE.getCode())).findFirst().orElse(null);

                m.put("preAssy_remote", m_preAssy == null ? na : m_preAssy.getFlowRuleName());
                m.put("babFlow_remote", m_babFlow == null ? na : m_babFlow.getFlowRuleName());
                m.put("testFlow_remote", m_testFlow == null ? na : m_testFlow.getFlowRuleName());
                m.put("packingFlow_remote", m_packageFlow == null ? na : m_packageFlow.getFlowRuleName());

                errors.add(m);
            } 
        }
        if (!errors.isEmpty()) {
            generator.generateWorkBooks(errors);
            generator.outputExcel("conflict");
        }
    }

    private boolean isFlowNameTheSame(Worktime w, List<MaterialFlow> l) throws Exception {
        for (Section section : Section.values()) {
            MaterialFlow mf = l.stream()
                    .filter(materialFlow -> materialFlow.getUnitNo().equals(section.getCode()))
                    .findFirst()
                    .orElse(null);
            switch (section) {
                case PREASSY:
                    if (compareFlow(w.getPreAssy(), mf) == false) {
                        System.out.println(section + " is different");
                        return false;
                    }
                    break;
                case BAB:
                    if (compareFlow(w.getFlowByBabFlowId(), mf) == false) {
                        System.out.println(section + " is different");
                        return false;
                    }
                    break;
                case TEST:
                    if (compareFlow(w.getFlowByTestFlowId(), mf) == false) {
                        System.out.println(section + " is different");
                        return false;
                    }
                    break;
                case PACKAGE:
                    if (compareFlow(w.getFlowByPackingFlowId(), mf) == false) {
                        System.out.println(section + " is different");
                        return false;
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private boolean compareFlow(Object flowBean, MaterialFlow mf) throws Exception {
        if ((flowBean == null || Objects.equals("empty", BeanUtils.getProperty(flowBean, "name"))) && mf == null) {
            return true;
        } else if (flowBean != null && mf != null && Objects.equals(BeanUtils.getProperty(flowBean, "name"), mf.getFlowRuleName())) {
            return true;
        }
        return false;
    }
}
