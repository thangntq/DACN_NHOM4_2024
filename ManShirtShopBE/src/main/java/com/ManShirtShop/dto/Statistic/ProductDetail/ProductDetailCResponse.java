package com.ManShirtShop.dto.Statistic.ProductDetail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailCResponse {
    List<ProductDetailCustomer> productDetailCustomer;
    Integer countOrder;
}
