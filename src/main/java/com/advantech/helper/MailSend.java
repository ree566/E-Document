/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 當組別異常時，發出異常訊息給使用者(呼叫此class)
 */
package com.advantech.helper;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class MailSend {

    private static MailSend instance;

    private final String mailHost;
    private final String mailPort;
    private final String mailServerAddress;
    private final String password;
    private final String companyAddr = "@advantech.com.tw";

    private static boolean isSendMailAlarmUser;

    private final Properties props;
    private static final Logger log = LoggerFactory.getLogger(MailSend.class);

    private MailSend() {
        PropertiesReader properties = PropertiesReader.getInstance();
        String hostaddr = null;
        try {
            String hostSetting = properties.getMailServerLocation();
            hostaddr = new ParamChecker().checkInputVal(hostSetting) ? hostSetting : getHostAddr();
        } catch (UnknownHostException | SocketException ex) {
            log.error(ex.toString());
        }
        mailHost = hostaddr;
        mailPort = properties.getMailServerPort();
        mailServerAddress = properties.getMailServerUsername() + "@" + hostaddr;
        password = properties.getMailServerPassword();
        props = new Properties();
        props.setProperty("mail.smtp.host", mailHost);
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", mailPort);
        isSendMailAlarmUser = properties.isSendMailAlarmUser();
    }

    public static MailSend getInstance() {
        if (instance == null) {
            instance = new MailSend();
        }
        return instance;
    }

    public boolean sendMail(String to, String subject, String content) throws MessagingException {
        return sendMail(new JSONArray().put(to), new JSONArray(), subject, content);
    }

    public boolean sendMail(String to, JSONArray cc, String subject, String content) throws MessagingException {
        return sendMail(new JSONArray().put(to), cc, subject, content);
    }

    public boolean sendMail(JSONArray to, JSONArray cc, String subject, String content) throws MessagingException {
        if (!isSendMailAlarmUser) {
            log.info("The setting in options.properties is false, send mail abandon!");
            return false;
        } else if (to.length() == 0) {
            log.info("The target mailLoop is empty, send mail abandon!");
            return false;
        }

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailServerAddress, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailServerAddress));

        for (int i = 0; i < to.length(); i++) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.get(i) + companyAddr));
        }

        for (int i = 0; i < cc.length(); i++) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc.get(i) + companyAddr));
        }

        message.setSubject(subject, "UTF-8");
        message.setSentDate(new Date());
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(content, "text/html;charset=UTF-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);
        message.setContent(multipart);
        Transport transport = session.getTransport("smtp");
        transport.connect(mailHost, Integer.parseInt(mailPort), mailServerAddress, password);
        Transport.send(message);
        return true;
    }

    //測試main
    public static void main(String[] arg0) {
        MailSend m = MailSend.getInstance();
        System.out.println(m.mailHost);
        System.out.println(m.mailPort);
    }

    //Get the Host address.
    private String getHostAddr() throws UnknownHostException, SocketException {
        return InetAddress.getLocalHost().getHostAddress();  // often returns "127.0.0.1"
    }
}
