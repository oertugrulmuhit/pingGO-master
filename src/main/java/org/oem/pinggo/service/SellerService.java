package org.oem.pinggo.service;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.request.SellerRequest;
import org.springframework.transaction.annotation.Transactional;

public interface SellerService {

    @Transactional
    @NotNull String createOrUpdate(@NotNull SellerRequest sellerRequest);
}
