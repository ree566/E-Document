/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.Worktime;
import com.advantech.model.WorktimeAutouploadSetting;
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
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.transaction.Transactional;
import static junit.framework.Assert.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
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

    private List<Worktime> worktimes;

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

    @Autowired
    private WorktimeAutouploadSettingService worktimeAutouploadSettingService;

    @Autowired
    private PendingService pendingService;

    @Before
    public void initTestData() {
//        w = worktimeService.findByModel("PDCW240A10BGE0-ES");
        worktimes = worktimeService.findAll();
//        worktimes = newArrayList(w);
//        worktimes = worktimes.stream().filter(o -> o.getTwm2Flag() == 1).collect(toList());
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
        List<Worktime> l = worktimes;
        assertNotNull(l.get(0));
        List<WorktimeAutouploadSetting> settings = worktimeAutouploadSettingService.findAll();
        standardtimePort.initSettings(settings);

        for (Worktime worktime : l) {
            System.out.println("Upload model: " + worktime.getModelName());
            worktime.setReasonCode("A6");
            standardtimePort.update(worktime);
        }
    }

//    @Test
    @Rollback(true)
    public void testFlowUpload() throws Exception {
        List<Worktime> l = worktimes;
        for (Worktime worktime : l) {
            System.out.println("Upload model: " + worktime.getModelName());
            flowUploadPort.update(worktime);
        }
    }

    @Autowired
    private SessionFactory factory;

    @Test
    public void testPartMappingUserUpload() throws Exception {
        List<Worktime> l = this.worktimes;
        l.forEach((worktime) -> {
            try {
                System.out.println("Upload model: " + worktime.getModelName());
                mappingUserPort.update(worktime);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

//    @Test
    public void testSopUpload() throws Exception {
        List<Worktime> l = this.worktimes;
        for (Worktime worktime : l) {
            System.out.println("Upload " + worktime.getModelName());
            sopPort.update(worktime);
        }
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

//    @Test
    @Rollback(true)
    public void testMaterialPropertyUploadPort() throws Exception {

        List<Worktime> l = worktimes;
        materialPropertyUploadPort.initSettings();

        for (Worktime worktime : l) {
            System.out.println("Upload " + worktime.getModelName());
            materialPropertyUploadPort.update(worktime);
        }

//        materialPropertyUploadPort.update(worktime);
//        materialPropertyUploadPort.delete(worktime);
    }

    @Autowired
    private StandardTimeUpload standardTimeUpload;

//    @Test
//    @Rollback(true)
    public void testStandardTimeUploadJob() {
        standardTimeUpload.uploadToMes();
    }

//    @Test
    @Rollback(true)
    public void testStandardTimeUpload() throws Exception {
        Session session = factory.getCurrentSession();
        List<Worktime> l = session.createCriteria(Worktime.class).add(Restrictions.like("modelName", "BB", MatchMode.START)).list();
        assertEquals(453, l.size());
        List<WorktimeAutouploadSetting> settings = worktimeAutouploadSettingService.findByPrimaryKeys(2, 4);
        standardtimePort.initSettings(settings);
        l.forEach((worktime) -> {
            try {
                worktime.setReasonCode("A0");
                System.out.println(worktime.getModelName());
                standardtimePort.update(worktime);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
}
