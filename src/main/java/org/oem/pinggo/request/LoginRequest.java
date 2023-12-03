package org.oem.pinggo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
@Data
public class LoginRequest {

    @Schema(example = "mars")
    private @NotNull String username;

    @Schema(example = "123456")
    private @NotNull String password;


}
