/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.MailManager;
import com.advantech.model.User;
import com.advantech.model.Worktime;
import com.advantech.service.AuditService;
import com.advantech.service.UserNotificationService;
import com.advantech.webservice.port.StandardtimeUploadPort;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import org.hibernate.envers.RevisionType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng Upload standard time to mes
 */
@Component
public class StandardTimeUpload {

    private static final Logger log = LoggerFactory.getLogger(StandardTimeUpload.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private MailManager mailManager;

    @Autowired
    private StandardtimeUploadPort port;

    private List<String> checkField;

    @Value("#{contextParameters[pageTitle] ?: ''}")
    private String pageTitle;

    private int uploadFailMaxAllow = 5;

    @PostConstruct
    public void init() {
        initCheckFieldNames();
    }

    private void initCheckFieldNames() {
        checkField = newArrayList(
                "modelName",
                "totalModule", "cleanPanel", "assy", "t1", "t2",
                "t3", "t4", "hiPotLeakage", "coldBoot", "warmBoot",
                "vibration", "upBiRi", "downBiRi", "packing", "assyStation",
                "packingStation"
        );
    }

    public void uploadToMes() {
        List<String> errorMessages = new ArrayList();
        List<Worktime> modifiedWorktimes = this.findFieldChangeInDate(new DateTime().minusDays(1), new DateTime());

        log.info("Begin upload standardtime to mes: " + modifiedWorktimes.size() + " datas.");

        port.initSettings();

        int failCount = 0;

        for (Worktime w : modifiedWorktimes) {
            try {
                port.update(w);
            } catch (Exception e) {
                String errorMessage = w.getModelName() + " upload fail: " + e.getMessage();
                errorMessages.add(errorMessage);
                log.error(errorMessage);

                failCount++;

                if (failCount == uploadFailMaxAllow) {
                    String failMissionMessage = "Reaching maxinum upload fail allow count "
                            + uploadFailMaxAllow + ", abort mission.";
                    log.error(failMissionMessage);
                    errorMessages.add(failMissionMessage);
                    break;
                }
            }
        }

        this.notifyUser(errorMessages);

        log.info("Upload standardtime job finished.");
    }

    public void notifyUser(List<String> l) {
        String[] to = getMailByNotification("worktime_upload_alarm");
        String[] cc = new String[0];

        String subject = "【" + pageTitle + "系統訊息】大表上傳";
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

    public List<Worktime> findFieldChangeInDate(DateTime sD, DateTime eD) {
        List<Worktime> checkResult = new ArrayList();
        List<Object[]> result = auditService.findByFieldChangedInDate(Worktime.class, null, checkField, sD.toDate(), eD.toDate());

        Map<Integer, List<Object[]>> g = result.stream().collect(Collectors.groupingBy(o -> ((Worktime) o[0]).getId()));

        g.forEach((k, v) -> {
            if (!v.isEmpty()) {
                Object[] rec;

                //Last standardtime mod record(First add or Mod with reason)
                List<Object[]> withReason = v.stream()
                        .filter(o -> RevisionType.ADD.equals(o[2])
                        || (RevisionType.MOD.equals(o[2])
                        && ((Worktime) o[0]).getReasonCode() != null || !"0".equals(((Worktime) o[0]).getReasonCode())))
                        .collect(toList());

                rec = withReason.get(withReason.size() - 1);
                Worktime w = (Worktime) rec[0];

                checkResult.add(w);
            }
        });

        return checkResult;
    }
}
