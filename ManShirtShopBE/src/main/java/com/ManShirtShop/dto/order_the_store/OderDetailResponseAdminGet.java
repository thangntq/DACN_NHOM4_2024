package com.ManShirtShop.dto.order_the_store;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponseGet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OderDetailResponseAdminGet {
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
    private ProductDetailResponseGet productDetailId;
}
