/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.User;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author Wei.Cheng Regist by spring context
 */
public class MailManager {

    private static final Logger log = LoggerFactory.getLogger(MailManager.class);

    private JavaMailSender mailSender;

    private String hostIp;

    @Value("${send.mail.alarm.user: true}")
    private boolean sendMailAlarmUser;

    @PostConstruct
    protected void init() {
        try {
            hostIp = getHostAddr();
        } catch (UnknownHostException | SocketException ex) {
            log.error(ex.toString());
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendMail(List<User> to, List<User> cc, String subject, String text) {
//        List<String> toMails = to.stream().map(User::getEmail).collect(Collectors.toList());
//        List<String> ccMails = ccstream().map(User::getEmail).collect(Collectors.toList());
//
//        return sendMail(toMails.toArray(), ccMails.toArray(), subject, text);
        return false;
    }

    public boolean sendMail(String[] to, String subject, String text) throws MessagingException {
        return this.sendMail(to, new String[0], subject, text);
    }

    public boolean sendMail(String[] to, String[] cc, String subject, String text) throws MessagingException {
        if (!sendMailAlarmUser) {
            return false;
        }

        // Do the business calculations...
        // Call the collaborators to persist the order...
        // Create a thread safe "copy" of the template message and customize it
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setText(text, true);
        helper.setTo(to);
        helper.setCc(cc);
        helper.setSubject(subject);
        helper.setFrom("kevin@" + hostIp);

        try {
            this.mailSender.send(mimeMessage);
            return true;
        } catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
            return false;
        }
    }

    //Get the Host address.
    private String getHostAddr() throws UnknownHostException, SocketException {
        return InetAddress.getLocalHost().getHostAddress();  // often returns "127.0.0.1"
    }
}
