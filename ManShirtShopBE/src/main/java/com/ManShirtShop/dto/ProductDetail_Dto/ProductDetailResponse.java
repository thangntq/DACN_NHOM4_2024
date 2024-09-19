package com.ManShirtShop.dto.ProductDetail_Dto;

import java.sql.Timestamp;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.size_dto.SizeResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetailResponse {
    private int id;

    private int status;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String updateBy;

    private String createBy;

    private String barCode;
    private int quantity;

    private ColorResponse color;
    private SizeResponse size;
    private ProductReponse product;
}
