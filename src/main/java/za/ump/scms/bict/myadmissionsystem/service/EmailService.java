package za.ump.scms.bict.myadmissionsystem.service;

import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.Serializable;
import java.util.Properties;

@Stateless // Makes it an EJB
public class EmailService implements Serializable {

    @Asynchronous
    public void sendEmail(String to, String subject, String message) {
        final String username = "admissionsystem44@gmail.com";
        final String password = "tkqs pubx jkqi kxwj";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(message, "text/html; charset=utf-8");


            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace(); // Logging is better here than FacesContext
        }
    }
}
