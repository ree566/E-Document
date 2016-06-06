/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.quartzJob.DataTransformer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class PropertiesReader {

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private static PropertiesReader p;

    private String testMail, txtLocation, testTxtName, babTxtName, outputFilenameExt;

    private double testStandard, babStandard, balanceDiff;

    private JSONObject cc, responseUnits, systemAbnormalAlarmMailCC;
    private String systemAbnormalAlarmMailTo;

    private int maxTestTable, limitBABData, balanceRoundingDigit;

    private PropertiesReader() {
        try {
            dataInit();
        } catch (IOException | JSONException ex) {
            log.error(ex.toString());
        }
    }

    public static PropertiesReader getInstance() {
        if (p == null) {
            p = new PropertiesReader();
        }
        return p;
    }

    private void dataInit() throws IOException, JSONException {
        String configFile = "options.properties";
        Properties properties = new Properties();
        InputStream is = DataTransformer.class.getResourceAsStream(configFile);
        properties.load(is);

        testMail = properties.getProperty("testMail");
        txtLocation = properties.getProperty("outputTxtPath");
        testTxtName = properties.getProperty("outputTestName");
        babTxtName = properties.getProperty("outputBABName");
        outputFilenameExt = properties.getProperty("outputFilenameExt");
        testStandard = convertStringToDouble(properties.getProperty("standard"));
        babStandard = convertStringToDouble(properties.getProperty("balanceStandard"));
        balanceDiff = convertStringToDouble(properties.getProperty("balanceDifference"));
        cc = new JSONObject(properties.getProperty("ccMails"));
        responseUnits = new JSONObject(properties.getProperty("responseUnits"));
        systemAbnormalAlarmMailTo = properties.getProperty("systemAbnormalAlarm.mailTo");
        systemAbnormalAlarmMailCC = new JSONObject(properties.getProperty("systemAbnormalAlarm.mailCC"));
        maxTestTable = convertStringToInteger(properties.getProperty("maxTestTable"));
        limitBABData = convertStringToInteger(properties.getProperty("limitBABData"));
        balanceRoundingDigit = convertStringToInteger(properties.getProperty("balanceRoundingDigit"));
        is.close();
        properties.clear();
    }

    private int convertStringToInteger(String number) {
        return (number != null && !"".equals(number)) ? Integer.parseInt(number) : 0;
    }

    private double convertStringToDouble(String number) {
        return (number != null && !"".equals(number)) ? Double.parseDouble(number) : 0;
    }

    public static Logger getLog() {
        return log;
    }

    public double getTestStandard() {
        return testStandard;
    }

    public double getBabStandard() {
        return babStandard;
    }

    public double getBalanceDiff() {
        return balanceDiff;
    }

    public String getSystemAbnormalAlarmMailTo() {
        return systemAbnormalAlarmMailTo;
    }

    public JSONObject getCc() {
        return cc;
    }

    public JSONObject getResponseUnits() {
        return responseUnits;
    }

    public JSONObject getSystemAbnormalAlarmMailCC() {
        return systemAbnormalAlarmMailCC;
    }

    public String getTxtLocation() {
        return txtLocation;
    }

    public String getTestTxtName() {
        return testTxtName;
    }

    public String getBabTxtName() {
        return babTxtName;
    }

    public String getOutputFilenameExt() {
        return outputFilenameExt;
    }

    public String getTestMail() {
        return testMail;
    }

    public int getMaxTestTable() {
        return maxTestTable;
    }

    public int getLimitBABData() {
        return limitBABData;
    }

    public int getBalanceRoundingDigit() {
        return balanceRoundingDigit;
    }

    public static void main(String arg0[]) {

        PropertiesReader p = getInstance();
        System.out.println(p.testMail);
        System.out.println(p.txtLocation);
        System.out.println(p.testTxtName);
        System.out.println(p.babTxtName);
        System.out.println(p.outputFilenameExt);
        System.out.println(p.testMail);
        System.out.println(p.testStandard);
        System.out.println(p.babStandard);
        System.out.println(p.balanceDiff);

    }
}
