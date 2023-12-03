package org.oem.pinggo.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderJob {

    private final OrderService orderService;

    @Transactional
    @Scheduled(cron = "0/40 * * * * ?")
    public void deliverOrder() {
        log.info("order deliver job started");
        orderService.deliverOrder();
        log.info("order deliver job finished");
    }
}
