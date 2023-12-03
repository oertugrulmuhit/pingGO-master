package org.oem.pinggo.model.dtos;

import lombok.Data;
import org.oem.pinggo.model.entity.Product;

@Data
public class ProductDTO {

    private long id;

    private String name;

    private String description;

    private int quantity;

    private long ownerId;

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.ownerId = entity.getSeller().getId();
        this.description = entity.getDescription();
        this.name = entity.getName();
        this.quantity = entity.getQuantity();
    }

}
