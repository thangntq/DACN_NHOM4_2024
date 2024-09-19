package com.ManShirtShop.dto.faultProduct;

import com.ManShirtShop.dto.product.ProductDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FaultProductResponse {
    private Integer id;
    private String createBy;
    private Timestamp createTime;
    private Integer status;
    private String updateBy;
    private Timestamp updateTime;
    private String description;
    private Integer quantity;
    private ProductDetailResponse productDetailResponse;
}