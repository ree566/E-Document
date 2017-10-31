/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 每兩個小時幫忙把BAB資料做儲存
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.entity.FBN;
import com.advantech.entity.Line;
import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.FBNService;
import com.advantech.service.LineOwnerMappingService;
import com.advantech.service.LineService;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class CheckSensor extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(CheckSensor.class);

    private BAB bab;
    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("yy/MM/dd HH:mm:ss ");
    private final int excludeHour = 12;
    private int expireTime;
    private int detectPeriod;
    private JSONArray responsors;

    @Autowired
    private FBNService fbnService;

    @Autowired
    private LineService lineService;

    @Autowired
    private LineOwnerMappingService lineOwnerMappingService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            checkSensorAndSendMail();
        } catch (MessagingException ex) {
            log.error(ex.toString());
        }
    }

    //定時查看sensor資料是否又暫停or異常
    private void checkSensorAndSendMail() throws MessagingException {

        List<FBN> sensorStatus = fbnService.getSensorStatus(bab.getId());
        DateTime currentTime = new DateTime();

        int period;

        //所有13點開始，且13:30以前的感應器都去看與12:00相差多久，避開中午休息時間
        if (currentTime.getHourOfDay() == 13 && currentTime.getMinuteOfHour() < 30) {
            period = new Period(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").parseDateTime(bab.getBtime()), new DateTime().withTime(12, 00, 0, 0)).toStandardMinutes().getMinutes();
        } else {
            period = periodToNow(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").parseDateTime(bab.getBtime()));
        }

        //開線後N分鐘開始監聽
        if (period >= expireTime && currentTime.getHourOfDay() != excludeHour) {
            if (bab.getPeople() != sensorStatus.size()) {
                //看是第幾站沒有資料，回報
                Integer lostStation = sensorStatus.size() + 1;
                sendMail(bab.getLineName(), "訊號遺失於站別 " + lostStation);
            } else {
                //看看時間距離現在多久了，超過N秒沒動作，回報

                //按照ID排序，取最大值(最後更新時間)
                Collections.sort(sensorStatus, new Comparator<FBN>() {
                    @Override
                    public int compare(final FBN object1, final FBN object2) {
                        return Integer.compare(object1.getId(), object2.getId());
                    }
                });

                FBN lastStatus = sensorStatus.get(sensorStatus.size() - 1);
                String date = (String) lastStatus.getLogDate();
                String time = (String) lastStatus.getLogTime();

                int periodTime = periodToNow(date + time);
                if (isExpire(periodTime)) {
                    sendMail(bab.getLineName(), "Sensor已經超過 " + periodTime + " 分鐘沒有動作，最後一次感應在TagName " + lastStatus.getTagName());
                }
            }
        }
    }

    private int periodToNow(DateTime time) {
        DateTime currentTime = new DateTime();
        return Minutes.minutesBetween(time, currentTime).getMinutes();
    }

    private int periodToNow(String time) {
        DateTime checkedTime = dtf.parseDateTime(time);
        return periodToNow(checkedTime);
    }

    private boolean isExpire(int time) {
        return time > expireTime;
    }

    private void sendMail(String tagName, String message) throws MessagingException {
        if (responsors != null && responsors.length() != 0) {
            JSONArray ccLoop = new JSONArray(PropertiesReader.getInstance().getSystemAbnormalAlarmMailTo());
            addExtraMailToCCLoop(ccLoop);
            String subject = "[藍燈系統]Sensor異常訊息";
            String mailBody = generateMailBody(tagName, message);
            MailSend.getInstance().sendMail(responsors, ccLoop, subject, mailBody);
        }
    }

    //Add the mail which want sensor_alarm and lineId is not setting per sitefloor
    private void addExtraMailToCCLoop(JSONArray arr) {
        Line line = lineService.getLine(bab.getLine());

        List<Map> l = lineOwnerMappingService.getLineNotSetting();

        for (Map m : l) {
            if (m.containsKey("sitefloor") && m.containsKey("sensor_alarm") && m.containsKey("user_name")) {
                Integer line_id = (Integer) m.get("line_id");
                Integer sitefloor = (Integer) m.get("sitefloor");
                Integer sensor_alarm = (Integer) m.get("sensor_alarm");
                String user_name = (String) m.get("user_name");

                if ((line_id != null && line_id == line.getId()) || (line_id == null && sensor_alarm == 1 && (sitefloor == null || sitefloor == line.getSitefloor()))) {
                    arr.put(user_name);
                }
            }
        }
    }

    private String generateMailBody(String tagName, String message) {
        return new StringBuilder()
                .append("<p>時間 <strong>")
                .append(new Date())
                .append(" Sensor異常訊息如下</strong></p>")
                .append("<p style='color:red'>")
                .append(tagName)
                .append(" ")
                .append(message)
                .append("</p>")
                .append("<p>請協助確認感應器是否正常，謝謝。</p>")
                .append("<p>詳情請至系統目錄/CalculatorWSApplication/pages/admin/SysInfo後台頁面上方的感應器頁籤查看該線別狀態</p>")
                .append("<p>本系統將再 ")
                .append(detectPeriod)
                .append(" 分鐘之後再次確認此線別感應器狀態。</p>")
                .toString();
    }

    public void setBab(BAB bab) {
        this.bab = bab;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public void setDetectPeriod(int detectPeriod) {
        this.detectPeriod = detectPeriod;
    }

    public void setResponsors(JSONArray responsors) {
        this.responsors = responsors;
    }

}
