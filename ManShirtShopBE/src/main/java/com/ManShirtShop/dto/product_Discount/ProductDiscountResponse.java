package com.ManShirtShop.dto.product_Discount;

import com.ManShirtShop.dto.discount.DiscountResponse;
import com.ManShirtShop.dto.product.ProductReponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscountResponse {

    private Integer id;
    private Integer percent;
    private ProductReponse productId;
    private Integer discountId;

}
