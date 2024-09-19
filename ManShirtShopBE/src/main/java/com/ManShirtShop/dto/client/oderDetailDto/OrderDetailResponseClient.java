package com.ManShirtShop.dto.client.oderDetailDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import com.ManShirtShop.entities.Order;
import com.ManShirtShop.entities.ProductDetail;
import com.ManShirtShop.entities.Return;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseClient {
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
    private ProductDetailResponse productDetailId;
    private boolean checkRating;
}
