package com.ManShirtShop.dto.Statistic;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalResponsee {
    private Double totalByDay;
    private Integer totalByMonth;
    private Long totalByYear;
    private Integer orderInProgress;
}
