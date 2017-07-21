/*
  This file is part of the NetView open source project
  Copyright (c) 2017 NetView authors
  Licensed under The MIT License
 */
package zesp03.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import zesp03.common.core.Config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class AdminMailServiceImpl implements AdminMailService {
    private static final Logger log = LoggerFactory.getLogger(AdminMailServiceImpl.class);

    @Override
    public boolean send(String targetMail, String subject, String htmlBody) {
        final String username = Config.getAdminMailUsername();
        final String password = Config.getAdminMailPassword();
        final String host = Config.getAdminMailSmtpHost();
        final int port = Config.getAdminMailSmtpPort();
        final Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.connectiontimeout", 2000);
        props.put("mail.smtp.timeout", 2000);

        final Session session = Session.getDefaultInstance(
                props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );
        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(targetMail)
            );
            message.setSubject(subject);
            message.setText(htmlBody);
            Transport.send(message);
        }
        catch (MessagingException exc) {
            log.warn("failed to send e-mail as {}: {}", username, exc.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
