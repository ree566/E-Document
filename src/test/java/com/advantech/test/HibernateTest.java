/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Worktime;
import com.advantech.model.WorktimeAutouploadSetting;
import com.advantech.service.AuditService;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.WorktimeStandardtimeUploadPort;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.SessionFactory;
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

    //CRUD testing.
//    @Transactional
//    @Rollback(true)
//    @Test
    public void test() throws Exception {
        Worktime w = worktimeService.findByPrimaryKey(8384);
        List<WorktimeAutouploadSetting> l = settingService.findAll();
        Map<String, String> m = port.transformData(w, l);
        for (Map.Entry<String, String> entry : m.entrySet()) {
            String field = entry.getKey();
            String xmlString = entry.getValue();
            System.out.println("--- " + field + " ---");
            System.out.println(xmlString);
        }
    }

}
