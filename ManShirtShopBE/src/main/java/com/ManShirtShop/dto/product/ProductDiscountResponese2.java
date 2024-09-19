package com.ManShirtShop.dto.product;

import com.ManShirtShop.dto.discount.DiscountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscountResponese2 {
    private Integer id;
    private Integer percent;
    private Integer productId;
    private Integer discountId;
    private Integer status;
}
