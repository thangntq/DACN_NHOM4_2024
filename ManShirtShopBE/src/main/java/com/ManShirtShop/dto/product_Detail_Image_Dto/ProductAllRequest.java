package com.ManShirtShop.dto.product_Detail_Image_Dto;

import java.util.List;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailRequest;
import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductAllRequest {
    private Integer id;
    private String name;
    private double price;
    private String description;
    private double weight;
    private Integer category;
    private Integer design;
    private Integer form;
    private Integer material;
    private Integer sleeve;
    private Integer collar;
    private Integer discount;
    private List<ProductDetailRequest> productDetail;
    private List<ProductImageRequest> productImage;
}
