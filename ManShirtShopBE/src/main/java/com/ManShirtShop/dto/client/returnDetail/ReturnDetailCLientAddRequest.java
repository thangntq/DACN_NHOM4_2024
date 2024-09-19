package com.ManShirtShop.dto.client.returnDetail;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClientTemp;
import com.ManShirtShop.dto.order_the_store.OrderDetailResponseAdminTemp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailCLientAddRequest {
    private Integer returnId ;
    private Integer orderId;
    private List<OrderDetailResponseClientTemp> orderDetailResponseClientTemps;
}
