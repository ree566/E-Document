/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 每兩個小時幫忙把BAB資料做儲存
 */
package com.advantech.quartzJob;

import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BABService;
import com.advantech.service.BasicService;
import com.advantech.service.FBNService;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import org.quartz.Job;
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

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            checkSensor();
        } catch (MessagingException ex) {
            log.error(ex.toString());
        }
    }

    //定時查看sensor資料是否又暫停or異常
    private void checkSensor() throws MessagingException {
        BABService babService = BasicService.getBabService();
        FBNService fbnService = BasicService.getFbnService();
        List isUsedBAB = babService.getProcessingBAB();
        List l = fbnService.getSensorDataInDay();
        if (!isUsedBAB.isEmpty() && l.isEmpty()) {
            sendMail();
        }
    }

    private void sendMail() throws MessagingException {

        String targetMail = PropertiesReader.getInstance().getTestMail();
        String subject = "[藍燈系統]Sensor異常訊息";
        String mailBody = generateMailBody();
        MailSend.getInstance().sendMailWithoutSender(this.getClass(), targetMail, subject, mailBody);

    }

    private String generateMailBody() {
        return new StringBuilder()
                .append("<p>時間 <strong>")
                .append(new Date())
                .append("</strong> 距離工單開啟已經一段時間沒有資料寫入，</p>")
                .append("<p>請協助確認感應器是否正常，謝謝。</p>")
                .toString();
    }
}
