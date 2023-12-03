package org.oem.pinggo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.model.dtos.UserDetailsDTO;
import org.oem.pinggo.model.entity.User;
import org.oem.pinggo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final Translator translator;

    @Override
    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                            String errorMessage = translator.toLocale("user.not.found.with.username.exception", username);
                            log.error(" searching failed with {}  : {}", username, errorMessage);
                            return new UsernameNotFoundException(errorMessage);
                        }
                );
        final UserDetailsDTO userDetails = UserDetailsDTO.build(user);
        return userDetails;
    }

}
