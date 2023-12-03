package org.oem.pinggo.controller;

import lombok.RequiredArgsConstructor;
import org.oem.pinggo.request.SellerRequest;
import org.oem.pinggo.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody SellerRequest sellerRequest) {
        final String created = sellerService.createOrUpdate(sellerRequest);
        return ResponseEntity.ok(created);
    }

}
