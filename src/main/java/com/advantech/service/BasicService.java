/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.test.TestClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class BasicService {

    private static final Logger log = LoggerFactory.getLogger(BasicService.class);

    private static BABService babService = null;
    private static FBNService fbnService = null;
    private static LineBalanceService lineBalanceService = null;
    private static LineService lineService = null;
    private static TestService testService = null;
    private static PrepareScheduleService prepareScheduleService = null;
    private static TestClassService testClassService = null;
    private static CountermeasureService countermeasureService = null;
    private static IdentitService identitService = null;
    private static BABLoginStatusService babLoginStatusService = null;
    private static ActionCodeMappingService actionCodeMappingService = null;
    private static LineOwnerMappingService lineOwnerMappingService = null;
    private static ModelResponsorService modelResponsorService = null;
    private static WorkTimeService workTimeService = null;
    private static PassStationService passStationService = null;
    private static CellProcessService cellProcessService = null;

    static {
        babService = new BABService();
        fbnService = new FBNService();
        lineBalanceService = new LineBalanceService();
        lineService = new LineService();
        testService = new TestService();
        prepareScheduleService = new PrepareScheduleService();
        testClassService = new TestClassService();
        countermeasureService = new CountermeasureService();
        identitService = new IdentitService();
        babLoginStatusService = new BABLoginStatusService();
        actionCodeMappingService = new ActionCodeMappingService();
        lineOwnerMappingService = new LineOwnerMappingService();
        modelResponsorService = new ModelResponsorService();
        workTimeService = new WorkTimeService();
        passStationService = new PassStationService();
        cellProcessService = new CellProcessService();
    }

    public static BABService getBabService() {
        return babService;
    }

    public static FBNService getFbnService() {
        return fbnService;
    }

    public static LineBalanceService getLineBalanceService() {
        return lineBalanceService;
    }

    public static LineService getLineService() {
        return lineService;
    }

    public static TestService getTestService() {
        return testService;
    }

    public static PrepareScheduleService getPrepareScheduleService() {
        return prepareScheduleService;
    }

    public static TestClassService getTestClassService() {
        return testClassService;
    }

    public static CountermeasureService getCountermeasureService() {
        return countermeasureService;
    }

    public static IdentitService getIdentitService() {
        return identitService;
    }

    public static BABLoginStatusService getBabLoginStatusService() {
        return babLoginStatusService;
    }

    public static ActionCodeMappingService getActionCodeMappingService() {
        return actionCodeMappingService;
    }

    public static LineOwnerMappingService getLineOwnerMappingService() {
        return lineOwnerMappingService;
    }

    public static ModelResponsorService getModelResponsorService() {
        return modelResponsorService;
    }

    public static WorkTimeService getWorkTimeService() {
        return workTimeService;
    }

    public static PassStationService getPassStationService() {
        return passStationService;
    }

    public static CellProcessService getCellProcessService() {
        return cellProcessService;
    }

}
