package com.freddys_bbq_mail;

import com.freddys_bbq_mail.model.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
