package com.ManShirtShop.dto.discount;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.ManShirtShop.dto.product_Discount.ProductDiscountResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiscountResponse {

    private Integer id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private Integer status;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<ProductDiscountResponse> productDiscount;
}
