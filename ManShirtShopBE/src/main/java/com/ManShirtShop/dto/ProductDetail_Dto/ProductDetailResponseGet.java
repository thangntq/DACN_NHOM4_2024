package com.ManShirtShop.dto.ProductDetail_Dto;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.product.ProductReponse;
import com.ManShirtShop.dto.product.ProductReponseGetAll;
import com.ManShirtShop.dto.size_dto.SizeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDetailResponseGet {
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
    private ProductReponseGetAll product;
    private Integer discount;
}
