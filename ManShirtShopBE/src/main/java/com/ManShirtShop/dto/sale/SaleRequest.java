package com.ManShirtShop.dto.sale;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.ManShirtShop.dto.discount.DiscountRequest;
import com.ManShirtShop.dto.discount.DiscountResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    
    private Integer id;

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private List<DiscountRequest> discount;
}
