/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.excel.ExcelGenerator;
import com.advantech.helper.SpringExpressionUtils;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeMaterialPropertyUploadSetting;
import com.advantech.service.FlowService;
import com.advantech.service.WorktimeMaterialPropertyUploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.MaterialFlowQueryPort;
import com.advantech.webservice.port.MaterialPropertyValueQueryPort;
import com.advantech.webservice.port.ModelResponsorQueryPort;
import com.advantech.webservice.port.SopQueryPort;
import com.advantech.webservice.root.Section;
import com.advantech.webservice.unmarshallclass.MaterialFlow;
import com.advantech.webservice.unmarshallclass.MaterialPropertyValue;
import com.advantech.webservice.unmarshallclass.ModelResponsor;
import com.advantech.webservice.unmarshallclass.SopInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
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

    @Autowired
    private MaterialPropertyValueQueryPort materialPropertyValueQueryPort;

    @Autowired
    private WorktimeMaterialPropertyUploadSettingService worktimeMaterialPropertyUploadSettingService;

    @Autowired
    private SpringExpressionUtils expressionUtils;

    @Autowired
    private SopQueryPort sopQueryPort;

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
//        c.setMaxResults(500);
        l = c.list();
    }

    //*************************************
    @Test
    public void testResponsor() throws Exception {
        ExcelGenerator generator = new ExcelGenerator();
        List<Map> errors = new ArrayList();
        for (Worktime w : l) {
            System.out.println("ModelName testing: " + w.getModelName() + " responsor...");
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
            generator.outputExcel("testResponsor_conflict");
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
            System.out.println("ModelName testing: " + w.getModelName() + " flow...");
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
            generator.outputExcel("testFlow_conflict");
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

    //*************************************
    @Test
    public void testMaterialProperty() throws Exception {
        List<WorktimeMaterialPropertyUploadSetting> matSettingLocal = worktimeMaterialPropertyUploadSettingService.findAll();
        List<Map> errors = new ArrayList();
        ExcelGenerator generator = new ExcelGenerator();
        for (Worktime worktime : l) {
            System.out.println("Testing " + worktime.getModelName() + " materialProperty...");
            List<MaterialPropertyValue> props = materialPropertyValueQueryPort.query(worktime);

            Map m = new LinkedHashMap();
            m.put("modelName", worktime.getModelName());
            boolean diff = false;

            for (WorktimeMaterialPropertyUploadSetting setting : matSettingLocal) {

                MaterialPropertyValue prop = props
                        .stream()
                        .filter(s -> ObjectUtils.compare(setting.getMatPropNo(), s.getMatPropertyNo()) == 0)
                        .findFirst()
                        .orElse(null);

                String mainFormulaValue = getValueFromFormula(worktime, setting.getFormula());
                String affFormulaValue = getValueFromFormula(worktime, setting.getAffFormula());

                m.put(setting.getColumnName() + "_mainValue_local",
                        ObjectUtils.compare(mainFormulaValue, setting.getDefaultValue()) == 0 && setting.getUploadWhenDefault() == 0 ? null : mainFormulaValue);
                m.put(setting.getColumnName() + "_mainValue_remote", prop == null ? null : prop.getValue());

                if (setting.getAffFormula() != null) {
                    m.put(setting.getColumnName() + "_affValue_local", "0".equals(affFormulaValue) ? null : affFormulaValue);
                    m.put(setting.getColumnName() + "_affValue_remote", prop == null ? null : prop.getAffPropertyValue());
                }

                /*
                    remote == null 且 local != default
                    remote != local 且 local != default (remote 可能為 null)
                 */
                if (prop == null && ObjectUtils.compare(mainFormulaValue, setting.getDefaultValue()) != 0) {
                    diff = true;
                } else if (prop != null && (ObjectUtils.compare(mainFormulaValue, prop.getValue()) != 0
                        || ObjectUtils.compare(affFormulaValue, prop.getAffPropertyValue()) != 0)) {
                    diff = true;
                }
            }

            if (diff) {
                System.out.println("Diff add");
                errors.add(m);
            } else {
                m.clear();
                m = null;
            }
        }
        if (!errors.isEmpty()) {
            generator.generateWorkBooks(errors);
            generator.outputExcel("testMaterialProperty_conflict");
        }
    }

    private String getValueFromFormula(Worktime w, String formula) {
        Object o = expressionUtils.getValueFromFormula(w, formula);

        if (o != null) {
            if (o instanceof BigDecimal && ((BigDecimal) o).signum() == 0) {
                return "0";
            } else if ((o instanceof Character || o instanceof String) && "".equals(o.toString().trim())) {
                return "";
            } else {
                return o.toString();
            }
        }
        return null;
    }

    //*************************************
    
    private final String regex = "[(\\r\\n|\\n),\" ]+";
    
    @Test
    public void testSop() throws Exception {
        List<Map> errors = new ArrayList();
        ExcelGenerator generator = new ExcelGenerator();
        String[] testingType = {"ASSY", "T1"};
        sopQueryPort.setTypes(testingType);

        for (Worktime worktime : l) {
            System.out.println("Testing " + worktime.getModelName() + " sop...");
            List<SopInfo> sops = sopQueryPort.query(worktime);

            Map m = new LinkedHashMap();
            m.put("modelName", worktime.getModelName());
            boolean diff = false;

            Set<String> totalSop = new HashSet();
            Set<String> assySop = toSops(worktime.getAssyPackingSop());
            Set<String> testSop = toSops(worktime.getTestSop());
            totalSop.addAll(assySop);
            totalSop.addAll(testSop);

            if (totalSop.size() != sops.size()) {

                totalSop.removeIf(s -> "".equals(s));
                String localValue = totalSop.stream()
                        .map(n -> n)
                        .collect(Collectors.joining(","));

                sops.removeIf(s -> "".equals(s.getSopName()));
                String remoteValue = sops.stream()
                        .map(n -> n.getSopName())
                        .collect(Collectors.joining(","));

                m.put("_sop_local", localValue);
                m.put("_sop_remote", remoteValue);

                diff = true;

            }

            if (diff) {
                System.out.println("Diff add");
                errors.add(m);
            } else {
                m.clear();
                m = null;
            }

        }
        if (!errors.isEmpty()) {
            generator.generateWorkBooks(errors);
            generator.outputExcel("testSop_conflict");
        }
    }

    private Set<String> toSops(String st) {
        Set s = new HashSet();
        if (st != null && !"".equals(st)) {
            String[] sops = st.split(regex);
            s = new HashSet(Arrays.asList(sops));
        }
        return s;
    }
}
