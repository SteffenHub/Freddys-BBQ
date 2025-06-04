package com.freddys_bbq_mail;

import java.io.File;

import com.freddys_bbq_mail.model.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


/**
 * Implementation of the {@link EmailService} interface.
 * Handles the logic for sending emails using Spring Mail's {@link JavaMailSender}.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    /**
     * Sender's email address.
     * Injected from application properties ('spring.mail.username').
     * Defaults to "email@example.com" if not configured.
     */
    @Value("${spring.mail.username:email@example.com}")
    private String sender;

    /**
     * Constructor for dependency injection of {@link JavaMailSender}.
     * @param javaMailSender Spring's mail sender utility.
     */
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends a simple email without an attachment.
     * Checks if a sender email is configured.
     *
     * @param details The {@link EmailDetails} (recipient, message, subject).
     * @return A status message.
     */
    public String sendSimpleMail(EmailDetails details) {
        if (this.sender.equals("email@example.com")) {
            return "Error while Sending Mail: No sender E-Mail configured";
        }
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    /**
     * Sends an email with an attachment.
     * Uses {@link MimeMessage} and {@link MimeMessageHelper} for multipart messages.
     * Checks if an attachment path is provided in the details.
     *
     * @param details The {@link EmailDetails} (recipient, message, subject, attachment path).
     * @return A status message.
     */
    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        if (details.getAttachment() == null || details.getAttachment().isEmpty()) {
            return "Error while sending mail!!! No attachment provided.";
        }
        if (this.sender.equals("email@example.com")) {
            return "Error while Sending Mail: No sender E-Mail configured";
        }

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            if (!file.exists()) {
                return "Error while sending mail!!! Attachment file not found at: " + details.getAttachment();
            }

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        } catch (MessagingException e) {
            return "Error while sending mail!!! " + e.getMessage();
        } catch (Exception e) {
            return "Error while sending mail!!! An unexpected error occurred.";
        }
    }
}
