package org.oem.pinggo.repository;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @NotNull List<Product> findByQuantityGreaterThanEqual(int quantity);

    @NotNull List<Product> findByNameContainsIgnoreCase(@NotNull String name);

    @NotNull List<Product> findByDescriptionContainsIgnoreCase(@NotNull String description);

    @Query("select (count(p) > 0) from Product p where p.id = :pID and p.seller.id = :sID")
    boolean existsByIdAndSellerId(@Param("pID") long pID, @Param("sID") long sID);

}