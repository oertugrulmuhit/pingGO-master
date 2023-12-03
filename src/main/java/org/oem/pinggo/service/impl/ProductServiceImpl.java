package org.oem.pinggo.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.exception.InsufficientResourceException;
import org.oem.pinggo.model.dtos.ProductDTO;
import org.oem.pinggo.model.entity.Product;
import org.oem.pinggo.model.entity.Seller;
import org.oem.pinggo.exception.ItemOwnerException;
import org.oem.pinggo.exception.NoSuchElementFoundException;
import org.oem.pinggo.repository.ProductRepository;
import org.oem.pinggo.request.ProductRequest;
import org.oem.pinggo.service.ProductService;
import org.oem.pinggo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final Translator translator;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public @NotNull List<ProductDTO> findAll() {
        final long currentUserId = userService.getCurrentUserId();
        log.info("{}  is getting  all products", currentUserId);
        List<ProductDTO> productDTOList = productRepository.findAll().stream()
                .map(ProductDTO::new)
                .toList();
        return productDTOList;
    }

    @Override
    public @NotNull Product findById(long productId) {
        final long currentUserId = userService.getCurrentUserId();
        log.info("{}  is getting  product with id {} ", currentUserId, productId);
        final Product product = productRepository.findById(productId).orElseThrow(() -> {
            String errorMessage = translator.toLocale("product.not.found.with.id.exception", productId);
            log.error("Product is not found : {}", errorMessage);
            return new NoSuchElementFoundException(errorMessage);
        });
        return product;
    }

    @Override
    @Transactional
    public @NotNull Product decreaseQuantity(long productId, int decreaseValue) {
        final Product product = findById(productId);

        if (decreaseValue > product.getQuantity()) {
            String errorMessage = translator.toLocale("product.insufficient.with.id.quantity.exception", product.getId(), decreaseValue);
            log.error("order is not created : {}", errorMessage);
            throw new InsufficientResourceException(errorMessage);
        }
        product.setQuantity(product.getQuantity() - decreaseValue);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void increaseQuantityForReturn(@NotNull Product product, int increaseValue) {
        product.setQuantity(product.getQuantity() + increaseValue);
        productRepository.save(product);
    }

    @Override
    public @NotNull ProductDTO getProduct(long productId) {
        final long currentUserId = userService.getCurrentUserId();
        log.info("{}  is getting  product with id {} ", currentUserId, productId);
        final Product product = findById(productId);
        return new ProductDTO(product);
    }

    @Override
    @Transactional
    public @NotNull ProductDTO update(long productId, @NotNull ProductRequest productUpdateRequest) {
        final Seller currentUser = userService.getCurrentSeller();
        log.info("{}  is editing product with id {} as {} ", currentUser.getId(), productId, productUpdateRequest);
        final Product product = findById(productId);
        validateSellerIsOwnerOfProduct(currentUser.getId(), productId);
        product.setDescription(productUpdateRequest.getDescription());
        product.setName(productUpdateRequest.getName());
        product.setQuantity(productUpdateRequest.getQuantity());
        Product saved = productRepository.save(product);
        log.info("{} is edited by {}", productId, currentUser.getId());
        return new ProductDTO(saved);
    }

    @Override
    @Transactional
    public void delete(long productId) {
        final Seller currentUser = userService.getCurrentSeller();
        log.info("{}  is deleting order with id {} ", currentUser.getId(), productId);
        validateSellerIsOwnerOfProduct(currentUser.getId(), productId);
        productRepository.deleteById(productId);
        log.info("{} is deleted by {}", currentUser.getId(), productId);
    }

    @Override
    public void validateSellerIsOwnerOfProduct(long sellerId, long productId) {
        log.info("check: {}  is seller of product with id  {} ", sellerId, productId);
        final boolean isExist = productRepository.existsByIdAndSellerId(productId, sellerId);
        if (!isExist) {
            String errorMessage = translator.toLocale("with.id.product.is.from.different.seller", productId);
            log.error("{} seller is trying to edit {} : {}", sellerId, productId, errorMessage);
            throw new ItemOwnerException(errorMessage);
        }
    }

    @Override
    @Transactional
    public @NotNull ProductDTO create(@NotNull ProductRequest productRequest) {
        final Seller currentSeller = userService.getCurrentSeller();
        log.info("{}  is creating product with {} ", currentSeller.getId(), productRequest);
        final Product product = new Product();
        product.setSeller(currentSeller);
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        Product created = productRepository.save(product);
        log.info("{} is created by {}", created.getId(), currentSeller.getId());
        return new ProductDTO(created);
    }


    @Override
    public @NotNull List<ProductDTO> findWithDesc(@NotNull String desc) {
        final List<ProductDTO> productDTOList = productRepository.findByDescriptionContainsIgnoreCase(desc).stream()
                .map(ProductDTO::new)
                .toList();
        return productDTOList;
    }

    @Override
    public @NotNull List<ProductDTO> findWithName(@NotNull String name) {
        final List<ProductDTO> productDTOList = productRepository.findByNameContainsIgnoreCase(name).stream()
                .map(ProductDTO::new)
                .toList();
        return productDTOList;
    }

    @Override
    public @NotNull List<ProductDTO> findWithQuantity(int quantity) {
        return productRepository.findByQuantityGreaterThanEqual(quantity).stream()
                .map(ProductDTO::new)
                .toList();
    }
}
