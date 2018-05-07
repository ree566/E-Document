/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Pending;
import com.advantech.model.Worktime;
import com.advantech.quartzJob.StandardTimeUpload;
import com.advantech.service.FlowService;
import com.advantech.service.PendingService;
import com.advantech.service.PreAssyService;
import com.advantech.service.WorktimeAutouploadSettingService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.FlowUploadPort;
import com.advantech.webservice.port.MaterialPropertyUploadPort;
import com.advantech.webservice.port.ModelResponsorUploadPort;
import com.advantech.webservice.port.SopUploadPort;
import com.advantech.webservice.port.StandardtimeUploadPort;
import java.util.List;
import javax.transaction.Transactional;
import static junit.framework.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ModelResponsorUploadPort modelResponsorUploadPort;

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

    @Autowired
    private WorktimeAutouploadSettingService worktimeAutouploadSettingService;

    @Autowired
    private PendingService pendingService;

    @Before
    public void initTestData() {
//        w = worktimeService.findByModel("TEST-MODEL-2");
    }

    @Value("${WORKTIME.UPLOAD.INSERT: true}")
    private boolean isInserted;

    @Value("${WORKTIME.UPLOAD.UPDATE: true}")
    private boolean isUpdated;

    @Value("${WORKTIME.UPLOAD.DELETE: true}")
    private boolean isDeleted;

    @Value("${WORKTIME.UPLOAD.SOP: true}")
    private boolean isUploadSop;

    @Value("${WORKTIME.UPLOAD.RESPONSOR: true}")
    private boolean isUploadResponsor;

    @Value("${WORKTIME.UPLOAD.FLOW: true}")
    private boolean isUploadFlow;

    @Value("${WORKTIME.UPLOAD.MATPROPERTY: true}")
    private boolean isUploadMatProp;

//    @Test
//    @Rollback(true)
    public void uploadParamTest() {
        assertTrue(isInserted);
        assertTrue(isUpdated);
        assertFalse(isDeleted);
        assertTrue(isUploadSop);
        assertTrue(isUploadResponsor);
        assertTrue(isUploadFlow);
        assertFalse(isUploadMatProp);
    }

//    @Test
    @Rollback(true)
    public void testStandardtimeUpload() throws Exception {
        List<Worktime> l = worktimeService.findByPrimaryKeys(8768);
        assertNotNull(l.get(0));
//        List<WorktimeAutouploadSetting> settings = worktimeAutouploadSettingService.findByPrimaryKeys(19, 20, 21, 22);
        standardtimePort.initSettings();

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

    //暫時用
//    @Test
    public void testStandardtimeUpload2() throws Exception {
        PageInfo info = new PageInfo();
        info.setSearchField("modifiedDate");
        info.setSearchOper("gt");
        info.setSearchString("2017-11-26");
        info.setRows(Integer.MAX_VALUE);
        List<Worktime> l = worktimeService.findAll(info);

        standardtimePort.initSettings();

        for (Worktime worktime : l) {
            System.out.println(worktime.getModelName());
            standardtimePort.update(worktime);
        }
    }

    //@Test
    @Rollback(true)
    public void testMaterialPropertyUploadPort() throws Exception {

        Integer[] ids = {6189,
            6231,
            6232,
            6233,
            7723,
            7996,
            8109,
            8110,
            8209,
            8416,
            8419,
            8440,
            8499,
            8503,
            8504,
            8505,
            8534,
            8628,
            8805};
        List<Worktime> l = worktimeService.findByPrimaryKeys(ids);
        materialPropertyUploadPort.initSetting();

        for (Worktime worktime : l) {
            materialPropertyUploadPort.update(worktime);
        }

//        materialPropertyUploadPort.update(worktime);
//        materialPropertyUploadPort.delete(worktime);
    }

    @Autowired
    private StandardTimeUpload standardTimeUpload;

    @Test
    @Rollback(true)
    public void testStandardTimeUploadJob() {
        standardTimeUpload.uploadToMes();
    }
    
//    @Test
    @Rollback(true)
    public void testModelResponsorUploadPort() throws Exception{
        Worktime obj = new Worktime();
        obj.setModelName("TEST-MODEL-2");
        modelResponsorUploadPort.delete(obj);
    }
}
