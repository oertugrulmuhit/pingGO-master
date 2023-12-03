package org.oem.pinggo.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.model.dtos.ProductDTO;
import org.oem.pinggo.request.ProductRequest;
import org.oem.pinggo.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/product")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        final List<ProductDTO> productDTOS = productService.findAll();
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@Parameter(description = "id of product you want to inspect", example = "1") @PathVariable long productId) {
        final ProductDTO productDTO = productService.getProduct(productId);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> update(@Parameter(description = "id of product you want to edit", example = "1") @PathVariable long productId, @RequestBody ProductRequest productUpdateRequest) {
        final ProductDTO updated = productService.update(productId, productUpdateRequest);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductRequest productRequest) {
        final ProductDTO created = productService.create(productRequest);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable long productId) {
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/desc/{desc}")
    public ResponseEntity<List<ProductDTO>> getByDesc(@Parameter(description = "search with descriptions", example = "too") @PathVariable String desc) {
        final List<ProductDTO> productDTOS = productService.findWithDesc(desc);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDTO>> getByName(@Parameter(description = "search with names", example = "desc") @PathVariable String name) {
        final List<ProductDTO> productDTOS = productService.findWithName(name);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/quantity/{quantity}")
    public ResponseEntity<List<ProductDTO>> getByQuantity(@Parameter(description = "search with quantity ", example = "5") @PathVariable int quantity) {
        final List<ProductDTO> productDTOS = productService.findWithQuantity(quantity);
        return ResponseEntity.ok(productDTOS);
    }
}
