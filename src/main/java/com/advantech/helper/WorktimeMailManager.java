/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.controller.CrudAction;
import com.advantech.controller.WorktimeController;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.UserNotificationService;
import java.util.List;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class WorktimeMailManager {

    private static final Logger log = LoggerFactory.getLogger(WorktimeController.class);

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private MailManager mailManager;

    public void notifyUser(List<Worktime> l, String action) {
        String[] to = getMailByNotification("worktime_alarm");
        String[] cc = getMailByNotification("worktime_alarm_cc");

        String subject = "【系統訊息】大表異動";
        String text = generateTextBody(l, action);

        try {
            mailManager.sendMail(to, cc, subject, text);
        } catch (MessagingException ex) {
            log.error("Send mail fail at action " + action + ":" + ex.toString());
        }
    }

    private String[] getMailByNotification(String notification) {
        List<User> users = userNotificationService.findUsersByNotification(notification);
        String[] mails = new String[users.size()];
        for (int i = 0; i < mails.length; i++) {
            String mail = users.get(i).getEmail();
            if (mail != null && !"".equals(mail)) {
                mails[i] = mail;
            }
        }
        return mails;
    }

    private String generateTextBody(List<Worktime> l, final String action) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear All:</p>");
        sb.append("<p>大表");
        sb.append(action);
        sb.append("了相關機種清單如下</p>");
        for (Worktime w : l) {
            sb.append("<p>");
            sb.append(w.getModelName());
            sb.append("</p>");
        }
        if (action.equals(CrudAction.ADD.toString())) {
            sb.append("<p>請相關人員至本系統維護大表。</p>");
        }

        return sb.toString();
    }
}
