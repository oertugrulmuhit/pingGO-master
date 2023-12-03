package org.oem.pinggo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;

@ToString
@Data
public class OrderRequest implements Serializable {

    @Schema(description = "how much do you want to order ", example = "5")
    private int quantity;

    @Schema(description = "which product do you want to order ", example = "1")
    private long productID;
}