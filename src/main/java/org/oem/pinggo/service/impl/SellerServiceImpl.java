package org.oem.pinggo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.model.entity.Seller;
import org.oem.pinggo.model.entity.User;
import org.oem.pinggo.repository.SellerRepository;
import org.oem.pinggo.request.SellerRequest;
import org.oem.pinggo.service.SellerService;
import org.oem.pinggo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final UserService userService;
    private final Translator translator;

    @Override
    @Transactional
    public @NotNull String createOrUpdate(@NotNull SellerRequest sellerRequest) {
        final User currentUser = userService.getCurrentUser();
        log.info("{} is will be seller with {} ", currentUser.getId(), sellerRequest);
        final String message;
        Seller seller = sellerRepository.findById(currentUser.getId())
                .orElse(new Seller(currentUser));
        seller.setBusinessAddress(sellerRequest.getBusinessAddress());
        seller.setBusinessName(sellerRequest.getBusinessName());
        sellerRepository.save(seller);
        log.info("{} is seller with {} ", currentUser.getId(), sellerRequest);
        message = translator.toLocale("seller.account.created");
        return message;
    }
}
