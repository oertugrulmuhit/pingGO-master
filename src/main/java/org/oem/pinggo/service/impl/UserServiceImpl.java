package org.oem.pinggo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.exception.UserNotFoundWithEmailException;
import org.oem.pinggo.model.dtos.UserDetailsDTO;
import org.oem.pinggo.model.entity.Seller;
import org.oem.pinggo.model.entity.User;
import org.oem.pinggo.exception.RequiredSellerException;
import org.oem.pinggo.repository.UserRepository;
import org.oem.pinggo.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final Translator translator;

    @Override
    public @NotNull User getCurrentUser() {

        User currentUser = userRepository.getReferenceById(getCurrentUserId());
        return currentUser;
    }

    @Override
    public long getCurrentUserId() {
        UserDetailsDTO currentUserDetails;
        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        if (isAuthenticated) {
            currentUserDetails = (UserDetailsDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            String errorMessage = translator.toLocale("user.not.logged.in.exception");
            log.error("Must be logged in: {}", errorMessage);
            throw new AccessDeniedException(errorMessage);
        }
        return currentUserDetails.getId();
    }

    @Override
    public @NotNull Seller getCurrentSeller() {
        final Seller seller = getCurrentUser().getSelleraccount();
        if (seller == null) {
            final String errorMessage = translator.toLocale("required.seller.account");
            log.error("access denied for : {}", errorMessage);

            throw new RequiredSellerException(errorMessage);
        }
        return seller;
    }

    @Override
    public boolean existsByUsername(@NotNull String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(@NotNull String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(@NotNull User user) {
        userRepository.save(user);
    }

    public @NotNull User findByEmail(@NotNull String mail) {
        final User user = userRepository.findByEmail(mail).orElseThrow(() -> {
            final String errorMessage = translator.toLocale("no.user.with.mail.address", mail);
            log.error(errorMessage);
            return new UserNotFoundWithEmailException(errorMessage);
        });
        return user;
    }
}
