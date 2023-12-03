package org.oem.pinggo.service;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.entity.VerificationToken;

public interface VerificationTokenService {

    @NotNull VerificationToken findByToken(@NotNull String token);

    @NotNull VerificationToken save(@NotNull VerificationToken vToken);
}
