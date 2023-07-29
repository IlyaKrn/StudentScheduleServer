package com.ilyakrn.studentscheduleserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class MailService {

    @Value("${mail.transport.protocol}")
    private String protocol;
    @Value("${mail.smtps.auth}")
    private String auth;
    @Value("${mail.smtps.host}")
    private String host;
    @Value("${mail.smtps.user}")
    private String user;
    @Value("${mail.smtps.password}")
    private String password;

    public void sendSimpleMessage(String email, String subject, String msg){
        try {
            final Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", protocol);
            properties.setProperty("mail.smtps.auth", auth);
            properties.setProperty("mail.smtps.host", host);
            properties.setProperty("mail.smtps.user", user);
            properties.setProperty("mail.smtps.password", password);
            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);
            String from = properties.getProperty("mail.smtps.user");
            message.setFrom(new InternetAddress(from.substring(0, from.indexOf("@"))));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setText(msg);

            Transport tr = session.getTransport();
            tr.connect(null, properties.getProperty("mail.smtps.password"));
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

        } catch (MessagingException e) {

        }
    }

}
