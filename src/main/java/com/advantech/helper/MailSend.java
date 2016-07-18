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
import org.json.JSONObject;
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

    private final JSONArray cc;

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

        JSONObject mails = properties.getCc();
        cc = mails.getJSONArray("maillist");

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

    public boolean sendMailWithoutSender(Class cls, String to, String subject, String text) throws MessagingException {
        return sendMailWithSender(cls, mailServerAddress, to, subject, text);
    }

    public boolean sendMailWithSender(Class cls, String from, String to, String subject, String text) throws MessagingException {
        boolean b = handleSettingsAndSendMail(from, to, subject, text);
        log.info(cls.getName() + (b ? " send success" : " send fail"));
        return b;
    }

    private boolean handleSettingsAndSendMail(String from, String to, String subject, String text) throws MessagingException {
        if(!isSendMailAlarmUser){
            log.info("The setting in options.properties is false, send mail job abandon!");
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
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to + companyAddr));

        for (Object o : cc) {
            String mail = o.toString() + companyAddr;
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail));
        }

        message.setSubject(subject, "UTF-8");
        message.setSentDate(new Date());
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(text, "text/html;charset=UTF-8");
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
//        System.out.println();
    }

    //Get the Host address.
    private String getHostAddr() throws UnknownHostException, SocketException {
        return InetAddress.getLocalHost().getHostAddress();  // often returns "127.0.0.1"
    }
}
