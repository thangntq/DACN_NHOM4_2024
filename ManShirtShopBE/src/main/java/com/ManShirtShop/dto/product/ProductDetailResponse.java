package com.ManShirtShop.dto.product;

import java.sql.Timestamp;

import com.ManShirtShop.dto.Color_Dto.ColorResponse;
import com.ManShirtShop.dto.size_dto.SizeResponse;
import com.ManShirtShop.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private int id;

    private int status;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String updateBy;

    private String createBy;

    private String barCode;
    private int quantity;
    public Integer productId;
    private ColorResponse color;
    private SizeResponse size;

}
