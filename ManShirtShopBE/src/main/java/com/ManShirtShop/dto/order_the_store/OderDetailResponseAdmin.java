package com.ManShirtShop.dto.order_the_store;

import java.sql.Timestamp;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OderDetailResponseAdmin {
    private Integer id;
    private int status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String updateBy;
    private String createBy;
    private Integer quantity;
    private double unitprice;
    private Integer returnQuantity;
    private Integer discount;
    private String note;
    // private Integer order;
    private ProductDetailResponse productDetailId;
}
