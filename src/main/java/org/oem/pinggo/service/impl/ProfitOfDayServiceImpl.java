package org.oem.pinggo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.oem.pinggo.model.dtos.ProfitOfDayDTO;
import org.oem.pinggo.model.entity.Order;
import org.oem.pinggo.model.entity.ProfitOfDay;
import org.oem.pinggo.model.entity.Seller;
import org.oem.pinggo.model.enums.OrderStatus;
import org.oem.pinggo.repository.ProfitOfDayRepository;
import org.oem.pinggo.service.OrderService;
import org.oem.pinggo.service.ProfitOfDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfitOfDayServiceImpl implements ProfitOfDayService {

    private final ProfitOfDayRepository profitOfDayRepository;
    private final OrderService orderService;

    @Override
    public @NotNull List<ProfitOfDayDTO> findAll() {
        final List<ProfitOfDayDTO> profitOfDayDTOList = profitOfDayRepository.findAll().stream().map(ProfitOfDayDTO::new).toList();
        return profitOfDayDTOList;
    }

    @Override
    @Transactional
    public void calculateDailyRevenue() {
        final LocalDate now = LocalDate.now();
        final LocalDateTime todayMorning = now.atTime(0, 0, 0);
        final LocalDateTime yesterdayMorning = todayMorning.minusDays(1);
        final List<Order> orderList = orderService.findByStatusAndStatusChangingTime(OrderStatus.DELIVERED, yesterdayMorning, todayMorning);
        final List<ProfitOfDay> willSave = new ArrayList<>();
        final Map<Seller, List<Order>> sellerAndOrders = orderList.stream()
                .collect(groupingBy(o -> o.getProduct().getSeller(), toList()));
        sellerAndOrders.forEach((seller, orders) -> {
            boolean existRecordForDateAndSeller = profitOfDayRepository.existsBySellerIdAndDate(seller.getId(), LocalDate.from(yesterdayMorning));
            if (!existRecordForDateAndSeller) {
                final long summed = orders.stream().mapToLong(Order::getQuantity).sum();
                final ProfitOfDay profitOfDay = new ProfitOfDay();
                profitOfDay.setTotalProfitForTheDay(summed);
                profitOfDay.setSeller(seller);
                profitOfDay.setDate(LocalDate.from(yesterdayMorning));
                willSave.add(profitOfDay);
                log.info("user: {} 's daily revenue is {} ", seller.getId(), summed);
            }
        });
        profitOfDayRepository.saveAll(willSave);
    }
}
