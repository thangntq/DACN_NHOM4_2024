package com.ManShirtShop.dto.Statistic.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalMonthResponse {
    Integer month;
    private Double total;
    private Integer orderSucsses;
    private Long productCount;
}
