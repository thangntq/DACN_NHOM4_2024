package com.ManShirtShop.dto.client.product_detail_client;

import java.time.LocalDateTime;
import java.util.List;

import com.ManShirtShop.dto.client.product.ProductDetailResponseClient;
import com.ManShirtShop.dto.client.product.ProductImageResponseClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductFullResponseClient {
    private Integer id;
    private String code;
    private String name;
    private double price;
    private String description;
    private double weight;

    private boolean laster;
    private boolean sale;
    private Double rating;
    private Integer discount;
    private Integer soluongdaban;
    private String category;
    private String collar;
    private String design;
    private String form;
    private String material;
    private String sleeve;
    public LocalDateTime end_date;
    private List<ProductDetailResponseClient> productDetail;

    private List<ProductImageResponseClient> productImage;
}
