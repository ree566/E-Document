/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.BAB;
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

    public String generateMailBody() {
        List<Map> l = BasicService.getCountermeasureService().getUnFillCountermeasureBabs();
        StringBuilder sb = new StringBuilder();
        sb.append("<style>table {border-collapse: collapse;} table, th, td {border: 1px solid black; padding: 5px;}</style>");
        sb.append("<p>Dear 使用者:</p>");
        sb.append("<p>以下是亮燈頻率高於基準值，尚未回覆異常原因的工單列表</p>");
        sb.append("<p>請抽空至 藍燈系統 > 線平衡資料查詢頁面 > 檢視詳細 填寫相關異常因素，謝謝</p>");
        sb.append("<table>");
        sb.append("<tr><th>工單</th><th>機種</th><th>投入時間</th></tr>");
        for (Map m : l) {
            sb.append("<tr><td>")
                    .append(m.get("PO"))
                    .append("</td><td>")
                    .append(m.get("Model_name"))
                    .append("</td><td>")
                    .append(m.get("Btime"))
                    .append("</td></tr>");
        }
        sb.append("</table>");
        sb.append("<p>資料共計: ");
        sb.append(l.size());
        sb.append(" 筆</p>");
        return sb.toString();
    }
}
