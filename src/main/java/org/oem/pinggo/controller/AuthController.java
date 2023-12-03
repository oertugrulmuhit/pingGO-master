package org.oem.pinggo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.oem.pinggo.model.dtos.MessageResponse;
import org.oem.pinggo.request.LoginRequest;
import org.oem.pinggo.request.SignupRequest;
import org.oem.pinggo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "User", description = "User management APIs")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "For Singin", description = "username:admin , password:123456 ")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @Operation(summary = "For Singup", description = "username:randomly , password:randomly, email:valid randomly, role:[admin, mod ..etc] ")
    @ApiResponse(description = "data of user")
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(HttpServletRequest request, @Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(request, signUpRequest);
    }

    @GetMapping("/forgetPassword")
    public ResponseEntity<MessageResponse> forgetPassword(@RequestParam @Email String mail) {
        return authService.forgetPassword(mail);
    }

    @GetMapping("/signout")
    public ResponseEntity<MessageResponse> logoutUser() {
        return authService.logoutUser();
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<MessageResponse> verify(@Parameter(description = "Validation code") @PathVariable String token) {
        return authService.verify(token);
    }

    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<MessageResponse> resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
        return authService.resendToken(request, existingToken);
    }
}