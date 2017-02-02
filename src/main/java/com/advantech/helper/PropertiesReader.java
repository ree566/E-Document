/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.InputStream;
import static java.lang.System.out;
import java.util.Properties;
import org.apache.commons.lang3.builder.ToStringBuilder;
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

    private Double testStandardMin, testStandardMax;
    private Double assyStandard, packingStandard;
    private Double assyAlarmPercent, packingAlarmPercent;
    private Double balanceDiff;

    private String[] systemAbnormalAlarmMailTo;
    private String[] systemAbnormalAlarmMailCC;

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
        testStandardMin = convertStringToDouble(properties.getProperty("test.productivity.standard.min"));
        testStandardMax = convertStringToDouble(properties.getProperty("test.productivity.standard.max"));

        numLampMaxTestRequiredPeople = convertStringToInteger(properties.getProperty("numLamp.test.maxRequiredPeople"));
        numLampGroupStart = convertStringToInteger(properties.getProperty("numLamp.balanceDetect.groupStart"));
        numLampGroupEnd = convertStringToInteger(properties.getProperty("numLamp.balanceDetect.groupEnd"));
        numLampSpecCuttingGroup = convertStringToInteger(properties.getProperty("numLamp.balanceDetect.specCuttingGroup"));
        numLampMinStandardTime = convertStringToInteger(properties.getProperty("numLamp.mininum.standardTime"));
        numLampMinQuantity = convertStringToInteger(properties.getProperty("numLamp.mininum.quantity"));
        numLampMinTotalStandardTime = convertStringToInteger(properties.getProperty("numLamp.mininum.totalStandardTime"));

        assyStandard = convertStringToDouble(properties.getProperty("bab.assy.lineBalance.standard"));
        packingStandard = convertStringToDouble(properties.getProperty("bab.packing.lineBalance.standard"));
        assyAlarmPercent = convertStringToDouble(properties.getProperty("bab.assy.alarmPercent.standard"));
        packingAlarmPercent = convertStringToDouble(properties.getProperty("bab.packing.alarmPercent.standard"));
        babSaveToRecordStandardQuantity = convertStringToInteger(properties.getProperty("bab.saveToRecord.quantity"));
        balanceRoundingDigit = convertStringToInteger(properties.getProperty("bab.lineBalance.roundingDigit"));
        balanceDiff = convertStringToDouble(properties.getProperty("bab.lineBalance.difference"));

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

        systemAbnormalAlarmMailTo = separateMailLoop(properties.getProperty("systemAbnormalAlarm.mailTo"));
        systemAbnormalAlarmMailCC = separateMailLoop(properties.getProperty("systemAbnormalAlarm.mailCC"));

        sensorDetectExpireTime = convertStringToInteger(properties.getProperty("sensorDetect.expireTime"));
        sensorDetectPeriod = convertStringToInteger(properties.getProperty("sensorDetect.period"));

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

    private String[] separateMailLoop(String mailString) {
        return mailString == null ? new String[0] : mailString.replace(" ", "").split(",");
    }

    public Double getTestStandardMin() {
        return testStandardMin;
    }

    public Double getTestStandardMax() {
        return testStandardMax;
    }

    public Double getAssyStandard() {
        return assyStandard;
    }

    public Double getPackingStandard() {
        return packingStandard;
    }

    public Double getAssyAlarmPercent() {
        return assyAlarmPercent;
    }

    public Double getPackingAlarmPercent() {
        return packingAlarmPercent;
    }

    public Double getBalanceDiff() {
        return balanceDiff;
    }

    public String[] getSystemAbnormalAlarmMailTo() {
        return systemAbnormalAlarmMailTo;
    }

    public String[] getSystemAbnormalAlarmMailCC() {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    
}
