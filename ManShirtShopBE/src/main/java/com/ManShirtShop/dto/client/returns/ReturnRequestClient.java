package com.ManShirtShop.dto.client.returns;

import com.ManShirtShop.dto.client.oderDetailDto.OrderDetailResponseClientTemp;
import com.ManShirtShop.dto.order_the_store.OrderDetailResponseAdminTemp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequestClient {
    private Integer id ;
    private String note;
    private String reason;
    private String video;
    private Integer orderId;
    private List<OrderDetailResponseClientTemp> orderDetailResponseClientTemps;
}
