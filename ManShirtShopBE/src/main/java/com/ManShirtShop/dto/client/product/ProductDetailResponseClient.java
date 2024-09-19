package com.ManShirtShop.dto.client.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponseClient {
    private int id;
    private String barCode;
    private int quantity;
    public Integer productId;
    private ColorResponseClient color;
    private SizeResponseClient size;
    private Integer status;

}
