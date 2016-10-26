/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.InputStream;
import static java.lang.System.out;
import java.util.Properties;
import org.json.JSONArray;
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

    private String testMail, mailServerUsername, mailServerPassword, mailServerLocation, mailServerPort;

    private double testStandard, babStandard, balanceDiff;

    private JSONArray targetMailLoop;
    private JSONObject targetCCLoop, systemAbnormalAlarmMailCC, responseUserPerLine;
    private String systemAbnormalAlarmMailTo;

    private int maxTestTable, maxTestRequiredPeople, limitBABData, balanceRoundingDigit;

    private boolean writeToDB, saveToOldDB, sendMailAlarmUser;

    private String endpointQuartzTrigger;

    private PropertiesReader() throws Exception {
        dataInit();
    }

    public static PropertiesReader getInstance() {
        if (p == null) {
            try {
                p = new PropertiesReader();
            } catch (Exception ex) {
                out.println("Can't read the property file.");
                log.error("Can't read the property file.");
            }
        }
        return p;
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

        testMail = properties.getProperty("testMail");
        testStandard = convertStringToDouble(properties.getProperty("standard"));
        babStandard = convertStringToDouble(properties.getProperty("balanceStandard"));
        balanceDiff = convertStringToDouble(properties.getProperty("balanceDifference"));
        targetMailLoop = new JSONArray(properties.getProperty("responseUnits.mailTo"));
        targetCCLoop = new JSONObject(properties.getProperty("responseUnits.mailCC"));
        responseUserPerLine = new JSONObject(properties.getProperty("responseUser.perLine"));
        mailServerUsername = properties.getProperty("mail.server.username");
        mailServerPassword = properties.getProperty("mail.server.password");
        mailServerLocation = properties.getProperty("mail.server.location");
        mailServerPort = properties.getProperty("mail.server.port");
        sendMailAlarmUser = convertStringToBoolean(properties.getProperty("send.mail.alarm.user"));
        systemAbnormalAlarmMailTo = properties.getProperty("systemAbnormalAlarm.mailTo");
        systemAbnormalAlarmMailCC = new JSONObject(properties.getProperty("systemAbnormalAlarm.mailCC"));
        maxTestTable = convertStringToInteger(properties.getProperty("maxTestTable"));
        maxTestRequiredPeople = convertStringToInteger(properties.getProperty("maxTestRequiredPeople"));
        limitBABData = convertStringToInteger(properties.getProperty("limitBABData"));
        balanceRoundingDigit = convertStringToInteger(properties.getProperty("balanceRoundingDigit"));
        writeToDB = convertStringToBoolean(properties.getProperty("result.write.to.database"));
        saveToOldDB = convertStringToBoolean(properties.getProperty("result.save.to.oldServer"));
        endpointQuartzTrigger = properties.getProperty("endpoint.quartz.trigger");

        logTheSystemSetting();
    }

    private void logTheSystemSetting() {
        out.println("Set test lineType standard is : " + testStandard);
        out.println("Set bab lineType standard is : " + babStandard);
        out.println("Set balanceDiff(Need to send mail when balance is diff to prev input bab) is : " + balanceDiff);
        out.println("System abnormal alarm to : " + targetMailLoop);
        out.println("Set cc mail setting is : " + targetCCLoop);
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
        out.println("The max test required people in test lineType is  : " + maxTestRequiredPeople);
        out.println("The minimum data collection need to save to database : " + limitBABData);
        out.println("The balance rounding digit is : " + balanceRoundingDigit);
        out.println("Other save result setting : "
                + new JSONObject()
                .put("writeToDB", writeToDB)
                .put("saveToOldDB", saveToOldDB)
        );

        out.println("The endpoint quartz trigger : " + endpointQuartzTrigger);
    }

    private int convertStringToInteger(String number) {
        return (number != null && !"".equals(number)) ? Integer.parseInt(number) : 0;
    }

    private double convertStringToDouble(String number) {
        return (number != null && !"".equals(number)) ? Double.parseDouble(number) : 0;
    }

    private boolean convertStringToBoolean(String str) {
        return (str != null && !"".equals(str)) ? str.equals("true") : false;
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

    public JSONObject getTargetCCLoop() {
        return targetCCLoop;
    }

    public JSONArray getTargetMailLoop() {
        return targetMailLoop;
    }

    public JSONObject getSystemAbnormalAlarmMailCC() {
        return systemAbnormalAlarmMailCC;
    }

    public JSONObject getResponseUserPerLine() {
        return responseUserPerLine;
    }

    public String getTestMail() {
        return testMail;
    }

    public int getMaxTestTable() {
        return maxTestTable;
    }

    public int getMaxTestRequiredPeople() {
        return maxTestRequiredPeople;
    }

    public int getLimitBABData() {
        return limitBABData;
    }

    public int getBalanceRoundingDigit() {
        return balanceRoundingDigit;
    }

    public boolean isWriteToDB() {
        return writeToDB;
    }

    public boolean isSaveToOldDB() {
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

    public boolean isSendMailAlarmUser() {
        return sendMailAlarmUser;
    }

    public String getEndpointQuartzTrigger() {
        return endpointQuartzTrigger;
    }

}
