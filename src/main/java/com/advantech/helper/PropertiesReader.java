/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.InputStream;
import static java.lang.System.out;
import java.util.Properties;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class PropertiesReader {

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private static PropertiesReader instance;

    private String testMail, mailServerUsername, mailServerPassword, mailServerLocation, mailServerPort;

    private Double testStandard, babStandard, balanceDiff;

    private JSONObject systemAbnormalAlarmMailCC;
    private String systemAbnormalAlarmMailTo;

    private Integer numLampMaxTestRequiredPeople, numLampGroupStart, numLampGroupEnd, numLampSpecCuttingGroup;
    private Integer numLampMinStandardTime, numLampMinQuantity, numLampMinTotalStandardTime;

    private Integer maxTestTable;

    private Integer babSaveToRecordStandardQuantity, balanceRoundingDigit;

    private Double cellStandardMin, cellStandardMax;

    private Boolean writeToDB, saveToOldDB, sendMailAlarmUser;

    private String endpointQuartzTrigger;

    private Integer sensorDetectExpireTime, sensorDetectPeriod;

    private PropertiesReader() throws Exception {
        dataInit();
    }

    public static PropertiesReader getInstance() {
        if (instance == null) {
            try {
                instance = new PropertiesReader();
            } catch (Exception ex) {
                out.println("Can't read the property file.");
                log.error("Can't read the property file.");
            }
        }
        return instance;
    }

    private void dataInit() throws Exception {
        String configFile = "/options.properties";
        Properties properties = new Properties();
        InputStream is = this.getClass().getResourceAsStream(configFile);
        properties.load(is);

        loadParams(properties);

        is.close();
        properties.clear();
    }

    private void loadParams(Properties properties) {
        maxTestTable = convertStringToInteger(properties.getProperty("test.maxTable"));

        numLampMaxTestRequiredPeople = convertStringToInteger(properties.getProperty("numLamp.test.maxRequiredPeople"));
        numLampGroupStart = convertStringToInteger(properties.getProperty("numLamp.balanceDetect.groupStart"));
        numLampGroupEnd = convertStringToInteger(properties.getProperty("numLamp.balanceDetect.groupEnd"));
        numLampSpecCuttingGroup = convertStringToInteger(properties.getProperty("numLamp.balanceDetect.specCuttingGroup"));
        numLampMinStandardTime = convertStringToInteger(properties.getProperty("numLamp.mininum.standardTime"));
        numLampMinQuantity = convertStringToInteger(properties.getProperty("numLamp.mininum.quantity"));
        numLampMinTotalStandardTime = convertStringToInteger(properties.getProperty("numLamp.mininum.totalStandardTime"));

        babSaveToRecordStandardQuantity = convertStringToInteger(properties.getProperty("bab.saveToRecord.quantity"));
        babStandard = convertStringToDouble(properties.getProperty("bab.lineBalance.standard"));
        balanceRoundingDigit = convertStringToInteger(properties.getProperty("bab.lineBalance.roundingDigit"));
        balanceDiff = convertStringToDouble(properties.getProperty("bab.lineBalance.difference"));

        testStandard = convertStringToDouble(properties.getProperty("test.productivity.standard"));

        cellStandardMin = convertStringToDouble(properties.getProperty("cell.standard.min"));
        cellStandardMax = convertStringToDouble(properties.getProperty("cell.standard.max"));

        testMail = properties.getProperty("mail.testMail");
        mailServerUsername = properties.getProperty("mail.server.username");
        mailServerPassword = properties.getProperty("mail.server.password");
        mailServerPort = properties.getProperty("mail.server.port");
        mailServerLocation = properties.getProperty("mail.server.location");

        writeToDB = convertStringToBoolean(properties.getProperty("result.write.to.database"));
        saveToOldDB = convertStringToBoolean(properties.getProperty("result.save.to.oldServer"));
        sendMailAlarmUser = convertStringToBoolean(properties.getProperty("send.mail.alarm.user"));
        endpointQuartzTrigger = properties.getProperty("endpoint.quartz.trigger");

        systemAbnormalAlarmMailTo = properties.getProperty("systemAbnormalAlarm.mailTo");
        systemAbnormalAlarmMailCC = new JSONObject(properties.getProperty("systemAbnormalAlarm.mailCC"));

        sensorDetectExpireTime = convertStringToInteger(properties.getProperty("sensorDetect.expireTime"));
        sensorDetectPeriod = convertStringToInteger(properties.getProperty("sensorDetect.period"));

        logTheSystemSetting();
    }

    private void logTheSystemSetting() {
        out.println("Set test lineType standard is : " + testStandard);
        out.println("Set bab lineType standard is : " + babStandard);
        out.println("Set balanceDiff(Need to send mail when balance is diff to prev input bab) is : " + balanceDiff);
        out.println("The mail info setting -> : "
                + new JSONObject()
                        .put("mailServerUsername", mailServerUsername)
                        .put("mailServerPassword", mailServerPassword)
                        .put("mailServerLocation", mailServerLocation)
                        .put("mailServerPort", mailServerPort)
                        .toString()
        );
        out.println("Need to send mail when system abnormal? : " + sendMailAlarmUser);
        out.println("Abnormal mail setting : "
                + new JSONObject()
                        .put("systemAbnormalAlarmMailTo", systemAbnormalAlarmMailTo)
                        .put("systemAbnormalAlarmMailCC", systemAbnormalAlarmMailCC)
        );
        out.println("The max table setting in test lineType is : " + maxTestTable);
        out.println("The max test required people in test lineType is  : " + numLampMaxTestRequiredPeople);
        out.println("The minimum data collection need to save to database : " + babSaveToRecordStandardQuantity);
        out.println("The balance rounding digit is : " + balanceRoundingDigit);
        out.println("Other save result setting : "
                + new JSONObject()
                        .put("writeToDB", writeToDB)
                        .put("saveToOldDB", saveToOldDB)
        );

        out.println("The endpoint quartz trigger : " + endpointQuartzTrigger);
    }

    private Integer convertStringToInteger(String number) {
        return (number != null && !"".equals(number)) ? Integer.parseInt(number) : 0;
    }

    private Double convertStringToDouble(String number) {
        return (number != null && !"".equals(number)) ? Double.parseDouble(number) : 0;
    }

    private Boolean convertStringToBoolean(String str) {
        return (str != null && !"".equals(str)) ? str.equals("true") : false;
    }

    public Double getTestStandard() {
        return testStandard;
    }

    public Double getBabStandard() {
        return babStandard;
    }

    public Double getBalanceDiff() {
        return balanceDiff;
    }

    public String getSystemAbnormalAlarmMailTo() {
        return systemAbnormalAlarmMailTo;
    }

    public JSONObject getSystemAbnormalAlarmMailCC() {
        return systemAbnormalAlarmMailCC;
    }

    public String getTestMail() {
        return testMail;
    }

    public Integer getMaxTestTable() {
        return maxTestTable;
    }

    public Integer getNumLampMaxTestRequiredPeople() {
        return numLampMaxTestRequiredPeople;
    }

    public Integer getNumLampGroupStart() {
        return numLampGroupStart;
    }

    public Integer getNumLampGroupEnd() {
        return numLampGroupEnd;
    }

    public Integer getNumLampSpecCuttingGroup() {
        return numLampSpecCuttingGroup;
    }

    public Integer getNumLampMinStandardTime() {
        return numLampMinStandardTime;
    }

    public Integer getNumLampMinQuantity() {
        return numLampMinQuantity;
    }

    public Integer getNumLampMinTotalStandardTime() {
        return numLampMinTotalStandardTime;
    }

    public Integer getBabSaveToRecordStandardQuantity() {
        return babSaveToRecordStandardQuantity;
    }

    public Integer getBalanceRoundingDigit() {
        return balanceRoundingDigit;
    }

    public Boolean isWriteToDB() {
        return writeToDB;
    }

    public Boolean isSaveToOldDB() {
        return saveToOldDB;
    }

    public String getMailServerUsername() {
        return mailServerUsername;
    }

    public String getMailServerPassword() {
        return mailServerPassword;
    }

    public String getMailServerLocation() {
        return mailServerLocation;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public Boolean isSendMailAlarmUser() {
        return sendMailAlarmUser;
    }

    public String getEndpointQuartzTrigger() {
        return endpointQuartzTrigger;
    }

    public Double getCellStandardMin() {
        return cellStandardMin;
    }

    public Double getCellStandardMax() {
        return cellStandardMax;
    }

    public Integer getSensorDetectExpireTime() {
        return sensorDetectExpireTime;
    }

    public Integer getSensorDetectPeriod() {
        return sensorDetectPeriod;
    }

}
