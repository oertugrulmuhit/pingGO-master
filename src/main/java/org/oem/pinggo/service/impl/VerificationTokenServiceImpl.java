package org.oem.pinggo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.exception.InvalidVerificationTokenException;
import org.oem.pinggo.model.entity.VerificationToken;
import org.oem.pinggo.repository.VerificationTokenRepository;
import org.oem.pinggo.service.VerificationTokenService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final Translator translator;
    @Override
    public @NotNull VerificationToken findByToken(@NotNull String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> {
            final String errorMessage = translator.toLocale("invalid.token", token);
            log.error("token validation error : {}", errorMessage);
            return new InvalidVerificationTokenException(errorMessage);
        });
    }

    @Override
    public @NotNull VerificationToken save(@NotNull VerificationToken vToken) {
        return verificationTokenRepository.save(vToken);
    }
}
