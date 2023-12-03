package org.oem.pinggo.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.oem.pinggo.model.dtos.ProfitOfDayDTO;
import org.oem.pinggo.service.ProfitOfDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/api/profitofday")
@RequiredArgsConstructor
public class ProfitOfDayController {
    private final ProfitOfDayService profitOfDayService;

    @GetMapping()
    public ResponseEntity<List<ProfitOfDayDTO>> revenues() {
        final List<ProfitOfDayDTO> profitOfDayDTOS = profitOfDayService.findAll();
        return ResponseEntity.ok(profitOfDayDTOS);
    }
    @Operation(summary = "updater, for test", description = "for testing, dont wait scheduled task")
    @PutMapping("/update")
    public ResponseEntity<?> update() {
        profitOfDayService.calculateDailyRevenue();
        return ResponseEntity.ok("updated");
    }
}
