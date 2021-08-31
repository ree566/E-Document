/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.MailManager;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.UserNotificationService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.port.SopQueryPort;
import com.advantech.webservice.unmarshallclass.SopInfo;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng Upload standard time to mes
 */
@Component
@Transactional
public class WorktimeFieldValueRetrieve {

    private static final Logger log = LoggerFactory.getLogger(WorktimeFieldValueRetrieve.class);

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private SopQueryPort sopQueryPort;

    @Autowired
    private MailManager mailManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Value("#{contextParameters[pageTitle] ?: ''}")
    private String pageTitle;

    public void retrieveFromMes() throws Exception {

        Session session = sessionFactory.getCurrentSession();

        List<Worktime> l = worktimeService.findAll();

        log.info("WorktimeFieldValueRetrieve job begin.");

        for (Worktime worktime : l) {
            sopQueryPort.setTypes("組包");
            List<SopInfo> assySops = sopQueryPort.query(worktime);

            sopQueryPort.setTypes("測試");
            List<SopInfo> testSops = sopQueryPort.query(worktime);

            assySops.removeIf(s -> "".equals(s.getSopName()) || s.getSopName().contains(","));
            testSops.removeIf(s -> "".equals(s.getSopName()) || s.getSopName().contains(","));

            String assySopStr = assySops.stream().sorted(Comparator.comparing(SopInfo::getSopName)).distinct().map(n -> n.getSopName()).collect(Collectors.joining(","));
            String testSopStr = testSops.stream().sorted(Comparator.comparing(SopInfo::getSopName)).distinct().map(n -> n.getSopName()).collect(Collectors.joining(","));

            worktime.setAssyPackingSop(assySopStr);
            worktime.setTestSop(testSopStr);

            session.merge(worktime);
        }

        notifyUser();

        log.info("WorktimeFieldValueRetrieve job finished.");
    }

    public void notifyUser() {
        String[] to = getMailByNotification("worktime_column_retrieve_alarm");
        String[] cc = new String[0];

        String subject = "【" + pageTitle + "系統訊息】大表同步";
        String text = generateTextBody();

        try {
            mailManager.sendMail(to, cc, subject, text);
        } catch (MessagingException ex) {
            log.error("Send mail fail when upload mes job fail." + ex.toString());
        }
    }

    private String generateTextBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear All:</p>");
        sb.append("<p>大表欄位同步成功於下列時間</p><p>");
        sb.append(new DateTime());
        sb.append("</p>");
        return sb.toString();
    }

    public String[] getMailByNotification(String notification) {
        List<User> users = userNotificationService.findUsersByNotification(notification);
        String[] mails = users.stream()
                .filter(u -> u.getEmail() != null || !"".endsWith(u.getEmail()))
                .map(u -> u.getEmail())
                .toArray(String[]::new);
        return mails;
    }

}
