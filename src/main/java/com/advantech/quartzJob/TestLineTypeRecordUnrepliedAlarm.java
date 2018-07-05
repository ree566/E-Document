/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.MailManager;
import com.advantech.model.TestRecord;
import com.advantech.model.User;
import com.advantech.service.TestRecordService;
import com.advantech.service.UserService;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng
 */
public class TestLineTypeRecordUnrepliedAlarm extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(TestLineTypeRecordUnrepliedAlarm.class);
    private final DecimalFormat formatter = new DecimalFormat("#.##%");
    private final DatetimeGenerator dg = new DatetimeGenerator("yyyy-MM-dd HH:mm");

    private final TestRecordService testRecordService;
    private final UserService userService;
    private final MailManager mailManager;

    private final String notificationName = "testRecord_unReplied_alarm";
    private final String subject = "[藍燈系統]未填寫異常回覆工單列表(測試線別) ";

    public TestLineTypeRecordUnrepliedAlarm() {
        testRecordService = (TestRecordService) ApplicationContextHelper.getBean("testRecordService");
        userService = (UserService) ApplicationContextHelper.getBean("userService");
        mailManager = (MailManager) ApplicationContextHelper.getBean("mailManager");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        try {
            //Link the mailloop to send daily mail
            sendMail();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public void sendMail() throws Exception {
        List<User> users = userService.findByUserNotification(notificationName);
        String[] mailLoop = users.stream().map(u -> u.getUsername()).toArray(String[]::new);
        String[] ccMailLoop = {};

        if (mailLoop.length != 0) {

            log.info("Begin send testRecord alarm...");
            log.info("Users " + Arrays.toString(mailLoop));

            // when user sitefloor is not setting, turn user's mail to mail cc loop
            String mailBody = this.generateMailBody();
            if (!"".equals(mailBody)) {
                //有資料再寄信
                mailManager.sendMail(mailLoop, ccMailLoop, subject, mailBody);
            }
            
        }
    }

    public String generateMailBody() {
        DateTime now = new DateTime();
        List<TestRecord> l = testRecordService.findByDate(now.minusWeeks(1), now, true);
        if (l.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("<style>table {border-collapse: collapse;} table, th, td {border: 1px solid black; padding: 5px;}</style>");
            sb.append("<p>Dear 使用者:</p>");
            sb.append("<p>以下是效率高於基準值，尚未回覆異常原因的紀錄列表</p>");
            sb.append("<p>請於 <mark><strong style='color: red'>今日下班前</strong></mark> 至 藍燈系統 > 測試 > 測試線別紀錄 填寫工單異常因素，謝謝</p>");
            sb.append("<table>");
            sb.append("<tr><th>id</th><th>工號</th><th>姓名</th><th>效率</th><th>桌次</th><th>儲存時間</th></tr>");
            l.forEach((t) -> {
                sb.append("<tr><td>")
                        .append(t.getId())
                        .append("</td><td>")
                        .append(t.getUserId())
                        .append("</td><td>")
                        .append(t.getUserName())
                        .append("</td><td>")
                        .append(formatter.format(t.getProductivity()))
                        .append("</td><td>")
                        .append(t.getTestTable().getName())
                        .append("</td><td>")
                        .append(dg.dateFormatToString(t.getLastUpdateTime()))
                        .append("</td></tr>");
            });
            sb.append("</table>");

            sb.append("<p>資料共計: ");
            sb.append(l.size());
            sb.append(" 筆</p>");
            return sb.toString();
        }

    }

}
