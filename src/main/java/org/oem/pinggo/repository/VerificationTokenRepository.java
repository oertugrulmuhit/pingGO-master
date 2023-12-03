package org.oem.pinggo.repository;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @NotNull Optional<VerificationToken> findByToken(@NotNull String token);
}
