/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.MailManager;
import com.advantech.jqgrid.PageInfo;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.UserNotificationService;
import com.advantech.service.WorktimeService;
import com.advantech.webservice.WorktimeStandardtimeUploadPort;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Wei.Cheng Upload standard time to mes
 */
public class StandardTimeUpload {

    private static final Logger log = LoggerFactory.getLogger(StandardTimeUpload.class);

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private MailManager mailManager;

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private WorktimeStandardtimeUploadPort port;

    private DateTimeFormatter df;

    private PageInfo tempInfo;

    @PostConstruct
    public void init() {
        df = DateTimeFormat.forPattern("yyyy-MM-dd");
        tempInfo = new PageInfo();
        tempInfo.setRows(Integer.MAX_VALUE);
        tempInfo.setSearchField("modifiedDate");
        tempInfo.setSearchOper("gt");
    }

    public void uploadToMes() {
        List<String> errorMessages = new ArrayList();
        this.updatePageInfo();
        List<Worktime> modifiedWorktimes = worktimeService.findAll(tempInfo);

        for (Worktime w : modifiedWorktimes) {
            try {
                port.uploadStandardTime(w);
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
                log.error(e.toString());
            }
        }

        this.notifyUser(errorMessages);
    }

    public void updatePageInfo() {
        tempInfo.setSearchString(df.print(new DateTime().minusWeeks(1)));
    }

    public void notifyUser(List<String> l) {
        String[] to = getMailByNotification("worktime_upload_alarm");
        String[] cc = new String[0];

        String subject = "【系統訊息】大表上傳";
        String text = generateTextBody(l);

        try {
            mailManager.sendMail(to, cc, subject, text);
        } catch (MessagingException ex) {
            log.error("Send mail fail when upload mes job fail." + ex.toString());
        }
    }

    private String generateTextBody(List<String> l) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear All:</p>");
        if (l.isEmpty()) {
            sb.append("<p>大表標工上傳成功於下列時間</p><p>");
            sb.append(new DateTime());
            sb.append("</p>");
        } else {
            sb.append("<p>上傳至大表發生了異常，訊息清單如下</p>");
            for (String st : l) {
                sb.append("<p>");
                sb.append(st);
                sb.append("</p>");
            }
            sb.append("<p>請相關人員至系統確認大表設定是否正確。</p>");
        }
        return sb.toString();
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
}
