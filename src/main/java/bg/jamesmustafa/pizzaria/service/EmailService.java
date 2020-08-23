package bg.jamesmustafa.pizzaria.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String to, String subject, String text){
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            helper.setFrom("djemko123@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            message.setContent(text, "text/html");

            emailSender.send(message);
            LOGGER.info("An email has been sent to {}", to);

        }  catch (MessagingException e) {
            LOGGER.error("An exception occurred: {}", e.getMessage());
        }
    }
}
