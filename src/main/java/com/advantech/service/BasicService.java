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

    static {
        babService = new BABService();
        fbnService = new FBNService();
        lineBalanceService = new LineBalanceService();
        lineService = new LineService();
        testService = new TestService();
        prepareScheduleService = new PrepareScheduleService();
        testClassService = new TestClassService();
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

}
