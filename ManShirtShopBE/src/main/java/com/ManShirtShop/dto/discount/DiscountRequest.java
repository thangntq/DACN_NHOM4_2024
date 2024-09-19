package com.ManShirtShop.dto.discount;

import java.time.LocalDateTime;
import java.util.List;

import com.ManShirtShop.dto.product_Discount.ProductDiscoutRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscountRequest {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;  
    private List<ProductDiscoutRequest> lstProductDiscount;


}
