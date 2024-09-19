package com.ManShirtShop.dto.client.oderDto;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponseGet;
import com.ManShirtShop.dto.client.product_detail_client.ProductDetailResponseGetClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OderDetailResponseClientGet {
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
    private ProductDetailResponseGetClient productDetailId;
}
