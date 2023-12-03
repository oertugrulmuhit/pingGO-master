package org.oem.pinggo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    public MailServiceImpl(@NotNull JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendMail(@NotNull SimpleMailMessage simpleMailMessage) {
        javaMailSender.send(simpleMailMessage);
        log.info("Mail Sent Successfully... to {}",simpleMailMessage.getTo());
    }

}
