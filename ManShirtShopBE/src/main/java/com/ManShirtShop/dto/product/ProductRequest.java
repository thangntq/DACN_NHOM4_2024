package com.ManShirtShop.dto.product;

import java.util.List;

import com.ManShirtShop.dto.product_Image_dto.ProductImageRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private int id;

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
    private List<ProductImageRequest> productImages;
    private Integer discount;
}
