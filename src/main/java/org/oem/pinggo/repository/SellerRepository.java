package org.oem.pinggo.repository;

import org.oem.pinggo.model.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Override
    boolean existsById(Long aLong);
}