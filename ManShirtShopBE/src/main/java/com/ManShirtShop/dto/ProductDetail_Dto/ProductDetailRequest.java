package com.ManShirtShop.dto.ProductDetail_Dto;

import java.util.List;

import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRequest {
    private int id;
    // private String barCode;
    private int quantity;

    private Integer color;
    private Integer size;
    private Integer product;
    private List<ProductImageRequest> lstProductImage;
}
