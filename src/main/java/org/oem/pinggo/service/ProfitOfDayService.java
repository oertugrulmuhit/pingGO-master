package org.oem.pinggo.service;

import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.dtos.ProfitOfDayDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfitOfDayService {

    @Transactional
    void calculateDailyRevenue();

    @NotNull List<ProfitOfDayDTO> findAll();
}
