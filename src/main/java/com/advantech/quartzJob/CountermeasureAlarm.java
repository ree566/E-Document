/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.MailSend;
import com.advantech.helper.PropertiesReader;
import com.advantech.service.BasicService;
import java.util.List;
import java.util.Map;
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
public class CountermeasureAlarm implements Job {

    private static final Logger log = LoggerFactory.getLogger(CountermeasureAlarm.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            //Link the mailloop to send daily mail
            sendMail();
        } catch (MessagingException ex) {
            log.error(ex.toString());
        }
    }

    private void sendMail() throws MessagingException {

        String targetMail = PropertiesReader.getInstance().getTestMail();
        if ("".equals(targetMail)) {
            return;
        }

        String subject = "[藍燈系統]未填寫異常回覆工單列表";
        MailSend.getInstance().sendMailWithoutSender(this.getClass(), targetMail, subject, generateMailBody());

    }

    private String generateMailBody() {
        List<Map> l = BasicService.getCountermeasureService().getUnFillCountermeasureBabs();
        StringBuilder sb = new StringBuilder();
        for (Map m : l) {
            sb.append("<p>")
                    .append(m.get("工單"))
                    .append(" / ")
                    .append(m.get("機種"))
                    .append(" / ")
                    .append(m.get("人數"))
                    .append(" / ")
                    .append(m.get("線別"))
                    .append(" / ")
                    .append(m.get("開始時間"))
                    .append("</p>");
        }
        return sb.toString();
    }
}
