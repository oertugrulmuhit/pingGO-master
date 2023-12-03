package org.oem.pinggo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SellerRequest {

    @Schema(description = "write name of company", example = "BooComp")
    private @NotNull String businessName;


    @Schema(description = "write address of company", example = "Street of BooComp")
    private @NotNull String businessAddress;


}
