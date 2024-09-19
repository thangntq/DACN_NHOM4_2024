package com.ManShirtShop.dto.client.oderDetailDto;

import java.time.LocalDateTime;
import com.ManShirtShop.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetailRequestClient {
    private Integer id;
    private Integer quantity;
    private double unitprice;
    private int returnQuantity;
    // private LocalDateTime updateDate;
    // private String updateName;
    private String note;
    private Integer order;
    private Integer productDetail;
    private Integer returnId;
}
