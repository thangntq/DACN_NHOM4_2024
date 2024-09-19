package com.ManShirtShop.dto.product_Discount;

import java.util.List;

import com.ManShirtShop.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscoutRequest {
    private Integer id;
    private Integer percent;
    private List<Integer> productId;
    private Integer discountId;

}
