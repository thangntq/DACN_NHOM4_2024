package com.ManShirtShop.dto.client.oderDetailDto;

import com.ManShirtShop.dto.ProductDetail_Dto.ProductDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseClientTemp {
    private Integer id;
    private int returnQuantity;
}
