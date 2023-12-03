package org.oem.pinggo.repository;

import org.oem.pinggo.model.entity.ProfitOfDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ProfitOfDayRepository extends JpaRepository<ProfitOfDay, Long> {
    @Query("select (count(p) > 0) from ProfitOfDay p where p.seller.id = :id and p.date = :date")
    boolean existsBySellerIdAndDate(@Param("id") Long id, @Param("date") LocalDate date);
}