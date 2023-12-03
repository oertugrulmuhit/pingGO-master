package org.oem.pinggo.service;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.dtos.ProductDTO;
import org.oem.pinggo.model.entity.Product;
import org.oem.pinggo.request.ProductRequest;

import java.util.List;

public interface ProductService {

    @NotNull List<ProductDTO> findAll();


    @NotNull Product findById(long productId);

    @Transactional
    @NotNull Product decreaseQuantity(long productId, int decreaseValue);

    @Transactional
    void increaseQuantityForReturn(@NotNull Product product, int increaseValue);

    @NotNull ProductDTO getProduct(long productId);

    @Transactional
    @NotNull ProductDTO update(long productId, @NotNull ProductRequest productUpdateRequest);

    @Transactional
    void delete(long productId);

    @NotNull List<ProductDTO> findWithDesc(@NotNull String desc);

    @NotNull List<ProductDTO> findWithName(@NotNull String name);

    @NotNull List<ProductDTO> findWithQuantity(int quantity);

    void validateSellerIsOwnerOfProduct(long userId, long productId);

    @NotNull ProductDTO create(@NotNull ProductRequest productRequest);
}
