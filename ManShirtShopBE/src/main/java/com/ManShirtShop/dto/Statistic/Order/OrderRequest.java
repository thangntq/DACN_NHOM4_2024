package com.ManShirtShop.dto.Statistic.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    Integer startYear;
    Integer endYear;
}
