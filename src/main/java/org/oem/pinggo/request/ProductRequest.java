package org.oem.pinggo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;
@Data
@ToString
public class ProductRequest {

    @Schema(example = "Ultimate Tool")
    private @NotNull String name;

    @Schema(example = "Description about Ultimate Tool")
    private @NotNull String description;

    @Schema(example = "20")
    private int quantity;

}
