package org.oem.pinggo.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.service.ProfitOfDayService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class ProfitOfDayJob {

    private final ProfitOfDayService profitOfDayService;

    @Transactional
    @Scheduled(cron = "1 0 0 * * ?")
    public void calculateDailyRevenue() {
        log.info("job started: fetch all delivered latest day to calculate revenue.");
        profitOfDayService.calculateDailyRevenue();
        log.info("job finished : daily revenues saved.");

    }
}
