package org.oem.pinggo.service;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.dtos.MessageResponse;
import org.oem.pinggo.model.dtos.UserInfoResponse;
import org.oem.pinggo.request.LoginRequest;
import org.oem.pinggo.request.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @NotNull ResponseEntity<UserInfoResponse> authenticateUser(@NotNull LoginRequest loginRequest);

    @Transactional
    @NotNull ResponseEntity<MessageResponse> registerUser(@NotNull HttpServletRequest request, SignupRequest signUpRequest);

    @NotNull ResponseEntity<MessageResponse> logoutUser();

    @Transactional
    @NotNull ResponseEntity<MessageResponse> verify(@NotNull String token);

    @Transactional
    @NotNull ResponseEntity<MessageResponse> resendToken(@NotNull HttpServletRequest request, @NotNull String existingToken);

    @Transactional
    @NotNull ResponseEntity<MessageResponse> forgetPassword(@NotNull String mail);
}