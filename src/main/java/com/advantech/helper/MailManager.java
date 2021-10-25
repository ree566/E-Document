/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author Wei.Cheng
 */
public class MailManager {

    private static final Logger log = LoggerFactory.getLogger(MailManager.class);

    private String hostName;
    
    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @PostConstruct
    protected void initHostName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            hostName = env.get("COMPUTERNAME");
        } else if (env.containsKey("HOSTNAME")) {
            hostName = env.get("HOSTNAME");
        } else {
            hostName = "Unknown";
        }
        hostName = hostName + "@advantech.com.tw";
    }

    public boolean sendMail(String[] to, String subject, String text) throws MessagingException {
        return this.sendMail(to, new String[0], subject, text);
    }

    public boolean sendMail(String[] to, String[] cc, String subject, String text) throws MessagingException {
        boolean flag = false;

        // Do the business calculations...
        // Call the collaborators to persist the order...
        // Create a thread safe "copy" of the template message and customize it
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setText(text, true);
        helper.setTo(to);
        helper.setCc(cc);
        helper.setSubject(subject);
        helper.setFrom(hostName);

        try {
            this.mailSender.send(mimeMessage);
            flag = true;
        } catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }

        return flag;
    }
}
