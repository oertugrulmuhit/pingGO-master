package org.oem.pinggo.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    @Async
    void sendMail(@NotNull SimpleMailMessage simpleMailMessage);
}
