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
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Value("#{contextParameters.pageTitle ?: systemProperties['contextParameters.pageTitle']}")
    private String pageTitle;

    public void notifyUser(List<Worktime> l, String action) {
        String[] to = getMailByNotification("worktime_alarm");
        String[] cc = getMailByNotification("worktime_alarm_cc");

        String subject = "【" + pageTitle + "系統訊息】大表異動";
        String text = generateTextBody(l, action);

        try {
            mailManager.sendMail(to, cc, subject, text);
        } catch (MessagingException ex) {
            log.error("Send mail fail at action " + action + ":" + ex.toString());
        }
    }

    public void notifyUser2(List<String> modelNames, String action) {
        String[] to = getMailByNotification("worktime_alarm");
        String[] cc = getMailByNotification("worktime_alarm_cc");

        String subject = "【" + pageTitle + "系統訊息】大表異動";
        String text = generateTextBody2(modelNames, action);

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
        List<String> modelNames = l.stream().map(w -> w.getModelName()).collect(toList());
        return this.generateTextBody2(modelNames, action);
    }

    private String generateTextBody2(List<String> l, final String action) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear All:</p>");
        sb.append("<p>使用者 ");
        sb.append(user.getUsername());
        sb.append(" 異動了大表( Action:");
        sb.append(action);
        sb.append(" )相關機種清單如下</p>");
        for (String modelName : l) {
            sb.append("<p>");
            sb.append(modelName);
            sb.append("</p>");
        }
        if (action.equals(CrudAction.ADD.toString())) {
            sb.append("<p>請相關人員至本系統維護大表。</p>");
        }

        return sb.toString();
    }
}
