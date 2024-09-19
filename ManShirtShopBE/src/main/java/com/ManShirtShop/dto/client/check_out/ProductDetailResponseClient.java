package com.ManShirtShop.dto.client.check_out;
import com.ManShirtShop.dto.client.product.ColorResponseClient;
import com.ManShirtShop.dto.client.product.SizeResponseClient;

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
    private String nameProduct;
    private String url;//
    private Double giaSp;
    private int discountSp;//
}

