package com.memories.api.email;

import com.memories.api.config.MemoriesProperties;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    private JavaMailSenderImpl mailSender;
    private final MemoriesProperties memoriesProperties;

    public EmailService(MemoriesProperties memoriesProperties) {
        this.memoriesProperties = memoriesProperties;
    }

    @PostConstruct
    public void init() {
        this.mailSender = new JavaMailSenderImpl();
        mailSender.setHost(memoriesProperties.getEmail().host());
        mailSender.setPort(memoriesProperties.getEmail().port());
        mailSender.setUsername(memoriesProperties.getEmail().username());
        mailSender.setPassword(memoriesProperties.getEmail().password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", "true");
    }

    public void sendActivationEmail(String email, String activationToken) {
        String activationEmail = """
                 <html>
                    <body>
                        <h1>Activate Account</h1>
                        <p>Thank you for registration. Please activate your account to login.</p>
                        <a href="${url}">Activate Account</a>
                    </body>
                 </html>
                """;

        String activationURI = memoriesProperties.getClient().host() + "/activation/" + activationToken;
        String mailBody = activationEmail.replace("${url}", activationURI);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

        try {
            message.setFrom(memoriesProperties.getEmail().from());
            message.setTo(email);
            message.setSubject("Account Activation");
            message.setText(mailBody, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        this.mailSender.send(mimeMessage);
    }
}
