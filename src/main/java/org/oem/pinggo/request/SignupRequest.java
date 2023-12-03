package org.oem.pinggo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
@Data
public class SignupRequest {

    @Size(min = 3, max = 20)
    @Schema(description = "username for register", example = "mars")
    private @NotNull String username;

    @Size(max = 50)
    @Email
    @Schema(description = "mail for validation", example = "mars@g.co")
    private @NotNull String email;

    @Size(max = 50)
    @Schema(description = "name for register", example = "Mars Adam")
    private @NotNull String name;

    @Size(min = 6, max = 40)
    @Schema(description = "password for login", example = "123456")
    private @NotNull String password;
}
