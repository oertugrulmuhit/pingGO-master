package org.oem.pinggo.service;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.entity.Seller;
import org.oem.pinggo.model.entity.User;

public interface UserService {

    boolean existsByUsername(@NotNull String username);

    void save(@NotNull User user);

    boolean existsByEmail(@NotNull String email);

    @NotNull User findByEmail(@NotNull String mail);

    @NotNull User getCurrentUser();

    long getCurrentUserId();

    @NotNull Seller getCurrentSeller();
}
