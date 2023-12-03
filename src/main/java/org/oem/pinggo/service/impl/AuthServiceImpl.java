package org.oem.pinggo.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.model.dtos.MessageResponse;
import org.oem.pinggo.model.dtos.UserDetailsDTO;
import org.oem.pinggo.model.dtos.UserInfoResponse;
import org.oem.pinggo.model.entity.User;
import org.oem.pinggo.model.entity.VerificationToken;
import org.oem.pinggo.exception.TimeoutVerificationTokenException;
import org.oem.pinggo.request.LoginRequest;
import org.oem.pinggo.request.SignupRequest;
import org.oem.pinggo.security.JwtUtils;
import org.oem.pinggo.service.AuthService;
import org.oem.pinggo.service.MailService;
import org.oem.pinggo.service.UserService;
import org.oem.pinggo.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final Translator translator;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final VerificationTokenService verificationTokenService;
    private final JwtUtils jwtUtils;

    @Value("${server.port}")
    private String port;

    @Override
    public @NotNull ResponseEntity<UserInfoResponse> authenticateUser(@NotNull LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();
        final ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new UserInfoResponse(jwtCookie.toString(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail()));
    }

    @Override
    @Transactional
    public @NotNull ResponseEntity<MessageResponse> registerUser(@NotNull HttpServletRequest request, @NotNull SignupRequest signUpRequest) {
        final String message;
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            message = translator.toLocale("error.username.is.already.taken", signUpRequest.getUsername());
            log.info("try duplicate username : {}", message);
            return ResponseEntity.badRequest().body(new MessageResponse(message));
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            message = translator.toLocale("error.email.is.already.in.use", signUpRequest.getEmail());
            log.info("try duplicate email : {}", message);
            return ResponseEntity.badRequest().body(new MessageResponse(message));
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getName(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        userService.save(user);
        final String token = user.getVerificationToken().getToken();
        final SimpleMailMessage constructEmailMessage = constructEmailMessage(request, signUpRequest.getEmail(), token);
        mailService.sendMail(constructEmailMessage);
        message = translator.toLocale("user.registered.successfully.with", signUpRequest.getUsername());
        log.info("user registered with {}:{} ", signUpRequest.getUsername(), message);
        return ResponseEntity.ok(new MessageResponse(message + "http://localhost:" + port + "/api/auth/verify/" + token));
    }

    @Override
    public @NotNull ResponseEntity<MessageResponse> logoutUser() {
        final ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        final String message = translator.toLocale("you.have.been.signed.out");
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse(message));
    }

    @Override
    @Transactional
    public @NotNull ResponseEntity<MessageResponse> verify(@NotNull final String token) {
        final VerificationToken verificationToken = verificationTokenService.findByToken(token);
        User user = verificationToken.getUser();
        boolean isTimeout = verificationToken.getExpiryDate().isBefore(LocalDateTime.now());
        if (isTimeout) {
            final String errorMessage = translator.toLocale("timeout.token", token, verificationToken.getExpiryDate());
            log.error("errorMessage");
            throw new TimeoutVerificationTokenException(errorMessage);
        }
        user.setEnabled(true);
        userService.save(user);
        final String message = translator.toLocale("validated.token");
        return ResponseEntity.ok(new MessageResponse(message));
    }

    // User activation - verification
    @Override
    @Transactional
    public @NotNull ResponseEntity<MessageResponse> resendToken(@NotNull final HttpServletRequest request, @NotNull final String existingToken) {
        final VerificationToken newToken = generateNewVerificationToken(existingToken);
        final User user = newToken.getUser();
        mailService.sendMail((constructResendVerificationTokenEmail(request, newToken, user.getEmail())));
        final String message = translator.toLocale("message.resendToken");
        return ResponseEntity.ok(new MessageResponse(message));
    }

    @Transactional
    public @NotNull VerificationToken generateNewVerificationToken(@NotNull final String existingVerificationToken) {
        final VerificationToken vToken = verificationTokenService.findByToken(existingVerificationToken);
        vToken.setToken(UUID.randomUUID().toString());
        vToken.setExpiryDate(LocalDateTime.now().plusHours(2));
        return verificationTokenService.save(vToken);
    }

    @Override
    @Transactional
    public @NotNull ResponseEntity<MessageResponse> forgetPassword(@NotNull final String mail) {
        log.info("new password will be sent {} ",mail);
        final User user = userService.findByEmail(mail);
        final String rawPassword = UUID.randomUUID().toString().substring(0, 10);
        final String password = encoder.encode(rawPassword);
        user.setPassword(password);
        userService.save(user);
        mailService.sendMail(constructNewPasswordEmail(mail, rawPassword));
        final String message = translator.toLocale("randomly.created.password.is.sent.via.mail");
        return ResponseEntity.ok(new MessageResponse(message));
    }

    private @NotNull String getAppUrl(@NotNull final HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private @NotNull SimpleMailMessage constructResendVerificationTokenEmail(final @NotNull HttpServletRequest request, @NotNull final VerificationToken newToken, @NotNull String mail) {
        final String confirmationUrl = getAppUrl(request) + "/api/auth/verify/" + newToken.getToken();
        final String title = translator.toLocale("auth.resendTokenTitle");
        final String message = translator.toLocale("auth.resendToken");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(title);
        email.setText(message + " rn" + confirmationUrl);
        email.setFrom("${support.email");
        email.setTo(mail);
        return email;
    }

    private @NotNull SimpleMailMessage constructNewPasswordEmail(String mail, String password) {
        final String title = translator.toLocale("randomly.password");
        final String message = translator.toLocale("your.randomly.created.password.is");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(title);
        email.setText(message + " " + password);
        email.setFrom("${support.email");
        email.setTo(mail);
        return email;
    }

    private @NotNull SimpleMailMessage constructEmailMessage(final @NotNull HttpServletRequest request, final @NotNull String mail, final @NotNull String token) {
        final String subject = translator.toLocale("registration.confirmation");
        final String confirmationUrl = getAppUrl(request) + "/api/auth/verify/" + token;
        final String message = translator.toLocale("authRegSuccessLink");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(mail);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom("${support.email");
        return email;
    }
}