/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 每兩個小時幫忙把BAB資料做儲存
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
import com.advantech.entity.FBN;
import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BasicService;
import static java.lang.System.out;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class CheckSensor implements Job {

    private static final Logger log = LoggerFactory.getLogger(CheckSensor.class);

    private BAB currentBab;
    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("yy/MM/dd HH:mm:ss ");
    private final int excludeHour = 12;
    private int expireTime;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
            BAB bab = (BAB) dataMap.get("dataMap");
            this.currentBab = bab;
            this.expireTime = (int) dataMap.get("expireTime");
            checkSensorAndSendMail(currentBab);
        } catch (MessagingException ex) {
            log.error(ex.toString());
        }
    }

    //定時查看sensor資料是否又暫停or異常
    private void checkSensorAndSendMail(BAB b) throws MessagingException {

        List<FBN> sensorStatus = BasicService.getFbnService().getSensorStatus(b.getId());
        int period = periodToNow(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").parseDateTime(b.getBtime()));

        //開線後10分鐘開始看
        if (period >= expireTime && new DateTime().getHourOfDay() != excludeHour) {
            if (b.getPeople() != sensorStatus.size()) {
                //看是第幾站沒有資料，回報
                Integer lostStation = sensorStatus.size() + 1;
//                sendMail(b.getLineName(), "Lost the signal at station " + lostStation);
                out.println(b.getLineName() + " lost the signal at station " + lostStation);
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
//                    sendMail(b.getLineName(), "Sensor is expired almost " + periodTime + " minutes on " + lastStatus.getTagName());
                    out.println(b.getLineName() + "'s sensor is expired almost " + periodTime + " minutes on " + lastStatus.getTagName());
                } else {
                    out.println(b.getLineName() + " sensor is normal processing...");
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
        String targetMail = PropertiesReader.getInstance().getTestMail();

        String subject = "[藍燈系統]Sensor異常訊息";
        String mailBody = generateMailBody(tagName, message);
        MailSend.getInstance().sendMail(targetMail, subject, mailBody);
    }

    private String generateMailBody(String tagName, String message) {
        return new StringBuilder()
                .append("<p>時間 <strong>")
                .append(new Date())
                .append("</strong> sensor異常訊息如下</p>")
                .append("<p>")
                .append(tagName)
                .append("</p>")
                .append("<p>")
                .append(message)
                .append("</p>")
                .append("<p>請協助確認感應器是否正常，謝謝。</p>")
                .toString();
    }
}
