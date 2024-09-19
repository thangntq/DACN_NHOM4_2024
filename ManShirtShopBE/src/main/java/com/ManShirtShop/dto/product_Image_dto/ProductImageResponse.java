package com.ManShirtShop.dto.product_Image_dto;

import java.sql.Timestamp;
import java.util.List;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.entities.Color;
import com.ManShirtShop.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    private int id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private String urlImage;
    private Boolean mainImage;
    private ColorResponse color;
    // private Product product;
//     private ColorResponse color;
    private int productId;
    private int status;
}
