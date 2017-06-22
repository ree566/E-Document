/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author Wei.Cheng
 */
public class MailManager {

    private static final Logger log = LoggerFactory.getLogger(MailManager.class);
    
    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public boolean testMail(String[] to, String[] cc, String subject, String text) {
        boolean flag = false;

        // Do the business calculations...
        // Call the collaborators to persist the order...
        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(to);
        msg.setCc(cc);
        msg.setSubject(subject);
        msg.setText(text);
        try {
            this.mailSender.send(msg);
            flag = true;
        } catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }

        return flag;
    }
}
