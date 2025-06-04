package com.freddys_bbq_mail;

import com.freddys_bbq_mail.model.EmailDetails;

/**
 * Interface defining operations for the email sending service.
 */
public interface EmailService {

    /**
     * Sends a simple email without an attachment.
     *
     * @param details The {@link EmailDetails} object containing email information.
     * @return A status message indicating success or failure.
     */
    String sendSimpleMail(EmailDetails details);

    /**
     * Sends an email with an attachment.
     *
     * @param details The {@link EmailDetails} object containing email information, including the attachment path.
     * @return A status message indicating success or failure.
     */
    String sendMailWithAttachment(EmailDetails details);
}