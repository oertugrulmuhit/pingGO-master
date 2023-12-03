package org.oem.pinggo.model.dtos;

import lombok.Data;
import org.oem.pinggo.model.entity.ProfitOfDay;

import java.time.format.DateTimeFormatter;

@Data
public class ProfitOfDayDTO {

    private long id;

    private long sellerId;

    private String date;

    private long totalProfitForTheDay;

    public ProfitOfDayDTO(ProfitOfDay entity) {
        this.id = entity.getId();
        this.sellerId = entity.getSeller().getId();
        this.date = entity.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.totalProfitForTheDay = entity.getTotalProfitForTheDay();
    }
}
