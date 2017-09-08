/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.model.Worktime;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.MaterialFlowUploadPort;
import com.advantech.webservice.port.PartMappingUserUploadPort;
import com.advantech.webservice.port.SopUploadPort;
import com.advantech.webservice.port.StandardtimeUploadPort;
import java.util.Map;
import javax.transaction.Transactional;
import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
//@Transactional
public class UploadPortTest {

    private Worktime w;

    //Port for ie
    @Autowired
    private StandardtimeUploadPort standardtimePort;

    //Port for spe
    @Autowired
    private MaterialFlowUploadPort materialPort;

    @Autowired
    private PartMappingUserUploadPort mappingUserPort;

    @Autowired
    private SopUploadPort sopPort;

    @Autowired
    private WorktimeService worktimeService;

//    @Before
    public void initTestData() {
        w = worktimeService.findByModel("HIT-W121-AWM1E");
    }

//    @Test
    public void testStandardtimeUpload() throws Exception {
        standardtimePort.initSettings();
        Map result = standardtimePort.transformData(w);
        assertEquals(17, result.size());
    }

//    @Test
    public void testMaterialFlowUpload() throws Exception {
        Map result = materialPort.transformData(w);
        assertEquals(4, result.size());
    }

//    @Test
    public void testPartMappingUserUpload() throws Exception {
        Map result = mappingUserPort.transformData(w);
        assertEquals(3, result.size());
        System.out.println(result.get("speOwner").toString());
        mappingUserPort.upload(w);
    }

//    @Test
    public void testSopUpload() throws Exception {
        Map result = sopPort.transformData(w);
        assertEquals(2, result.size());
    }
}
