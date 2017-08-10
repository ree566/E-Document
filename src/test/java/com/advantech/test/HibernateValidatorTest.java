/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.BusinessGroup;
import com.advantech.model.Flow;
import com.advantech.model.PreAssy;
import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
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
public class HibernateValidatorTest {

    @Autowired
    private WorktimeService worktimeService;

    private static Validator validator;
    private Map errors;

    @Before
    public void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    @Test
//    @Transactional
    public void testDataInServer() {
        Worktime w = worktimeService.findByModel("EBC-AC04M8G-U7A2E");
        assertNotNull(w);
        Map m = this.validateObject(w);

        assertNotEquals(0, m.size());
    }

    @Test
    public void testPreAssy() {
        Worktime w = new Worktime();
        PreAssy p = new PreAssy();
        p.setName("PRE_ASSY");
        w.setPreAssy(p);

        String[] testFields = {"cleanPanel"}, testFields2 = {"preAssy"};

        System.out.println("Testing when error cleanPanel...");
        errors = this.validateObject(w);
        checkError(testFields);

        w.setPreAssy(null);
        w.setCleanPanel(BigDecimal.TEN);

        System.out.println("Testing when error preAssy...");
        errors = this.validateObject(w);
        checkError(testFields2);

        System.out.println("Testing when all setting(no errors)");
        w.setPreAssy(p);
        errors = this.validateObject(w);
        System.out.println(new Gson().toJson(w));
        assertEquals(10, errors.size());
    }

    @Test
    public void testBabFlow() {
        Worktime w = new Worktime();
        Flow f = new Flow();
        f.setName("ADVS_BI");
        w.setFlowByBabFlowId(f);

        String[] testFields = {"upBiRi"}, testFields2 = {"babFlow"}, testFields3 = {"burnIn"};

        System.out.println("Testing when error upBiRi...");
        errors = this.validateObject(w);
        checkError(testFields);

        w.setFlowByBabFlowId(null);
        w.setUpBiRi(BigDecimal.TEN);

        System.out.println("Testing when error babFlow...");
        errors = this.validateObject(w);
        checkError(testFields2);

        w.setFlowByBabFlowId(f);
        w.setDownBiRi(BigDecimal.TEN);
        w.setBiCost(BigDecimal.TEN);

        System.out.println("Testing when error burnIn...");
        errors = this.validateObject(w);
        checkError(testFields3);

        System.out.println("Testing when error burnIn2...");
        w.setBurnIn("BO");
        errors = this.validateObject(w);
        checkError(testFields3);

        System.out.println("Testing when all setting(no errors)");
        w.setBurnIn("BI");
        errors = this.validateObject(w);
        System.out.println(new Gson().toJson(w));
        //少了Burn in 
        assertEquals(9, errors.size());
    }

    private Map validateObject(Worktime w) {
        System.out.println("---");
        Map m = new HashMap();
        Set<ConstraintViolation<Worktime>> constraintViolations = validator.validate(w);
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation violation : constraintViolations) {
                m.put(violation.getPropertyPath().toString(), violation.getMessage());
                System.out.println(violation.getPropertyPath().toString() + " = " + violation.getMessage());
            }
        }
        System.out.println("---");
        return m;
    }

    private void checkError(String[] checkFields) {
        for (String field : checkFields) {
            assertNotNull(errors.get(field));
        }
    }

    /*
        PRE_ASSY | cleanPanel | not_null_and_zero_message
        ASSY | assy | not_null_and_zero_message
        T1 | t1 | not_null_and_zero_message
        VB | vibration | not_null_and_zero_message
        "H1", " LK" | hiPotLeakage | not_null_and_zero_message
        CB | coldBoot | not_null_and_zero_message
        "BI", "RI" | "upBiRi", "downBiRi", "biCost" | not_null_and_zero_message
        BI | burnIn | 內容須為BI
        RI | burnIn | 內容須為RI
        T2 | t2 | not_null_and_zero_message
        T3 | t3 | not_null_and_zero_message
        T4 | t4 | not_null_and_zero_message
        PKG | packing | not_null_and_zero_message
     */
    
//    @Test
    public void testEsModel(){
        Worktime w = new Worktime();
        BusinessGroup bg = new BusinessGroup();
        bg.setName("ES");
        w.setBusinessGroup(bg);
        w.setModelName("S-ESSA");
        errors = this.validateObject(w);
    }
}
