package org.oem.pinggo.repository;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NotNull Optional<User> findByEmail(@NotNull String email);

    @NotNull Optional<User> findByUsername(@NotNull String username);

    boolean existsByUsername(@NotNull String username);

    boolean existsByEmail(@NotNull String email);
}
