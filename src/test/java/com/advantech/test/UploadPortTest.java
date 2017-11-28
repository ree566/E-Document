/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.service.FlowService;
import com.advantech.service.PreAssyService;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.FlowUploadPort;
import com.advantech.webservice.port.MaterialPropertyUploadPort;
import com.advantech.webservice.port.ModelResponsorUploadPort;
import com.advantech.webservice.port.SopUploadPort;
import com.advantech.webservice.port.StandardtimeUploadPort;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
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
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UploadPortTest {

    private Worktime w;

    //Port for ie
    @Autowired
    private StandardtimeUploadPort standardtimePort;

    //Port for spe
    @Autowired
    private FlowUploadPort flowUploadPort;

    @Autowired
    private ModelResponsorUploadPort mappingUserPort;

    @Autowired
    private SopUploadPort sopPort;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private PreAssyService preAssyService;

    @Autowired
    private MaterialPropertyUploadPort materialPropertyUploadPort;

    @Before
    public void initTestData() {
        w = worktimeService.findByModel("TEST-MODEL-2");
    }

    @Test
    @Rollback(true)
    public void testStandardtimeUpload() throws Exception {
        assertNotNull(w);
        standardtimePort.initSettings();
        standardtimePort.update(w);
    }

//    @Test
//    @Rollback(true)
    public void testFlowUpload() throws Exception {

    }

//    @Test
    public void testPartMappingUserUpload() throws Exception {
//        Map result = mappingUserPort.transformData(w);
//        assertEquals(1, result.size());
//        mappingUserPort.upload(w);
    }

//    @Test
    public void testSopUpload() throws Exception {
//        List<Worktime> l = worktimeService.findAll();
//        for (Worktime worktime : l) {
//            System.out.println("Upload " + worktime.getModelName());
//        sopPort.upload(w);
//        }
    }

    @Autowired
    private WorktimeAutouploadSettingService worktimeAutouploadSettingService;

    //暫時用
//    @Test
    public void testStandardtimeUpload2() {
        PageInfo info = new PageInfo();
        info.setSearchField("modifiedDate");
        info.setSearchOper("gt");
        info.setSearchString("2017-09-03");
        info.setRows(Integer.MAX_VALUE);
        List<Worktime> l = worktimeService.findAll(info);
        assertEquals(124, l.size());

        standardtimePort.initSettings(newArrayList(worktimeAutouploadSettingService.findByPrimaryKey(16)));

        l.forEach((worktime) -> {
            try {
                System.out.println("Upload model: " + worktime.getModelName());
                standardtimePort.update(worktime);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }

//    @Test
//    @Rollback(true)
    public void testMaterialPropertyUploadPort() throws Exception {

    }
}
